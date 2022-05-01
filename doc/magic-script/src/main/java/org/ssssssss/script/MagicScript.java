package org.ssssssss.script;


import org.ssssssss.script.compile.CompileCache;
import org.ssssssss.script.compile.MagicScriptCompileException;
import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.exception.MagicExitException;
import org.ssssssss.script.exception.MagicScriptException;
import org.ssssssss.script.functions.DynamicModuleImport;
import org.ssssssss.script.parsing.Parser;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.VarIndex;
import org.ssssssss.script.parsing.ast.Expression;
import org.ssssssss.script.parsing.ast.Node;
import org.ssssssss.script.parsing.ast.statement.Import;
import org.ssssssss.script.parsing.ast.statement.Return;
import org.ssssssss.script.parsing.ast.statement.VariableAccess;
import org.ssssssss.script.runtime.MagicScriptClassLoader;
import org.ssssssss.script.runtime.MagicScriptRuntime;
import org.ssssssss.script.runtime.MagicScriptVariableAccessRuntime;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MagicScript extends CompiledScript {

	public static final String CONTEXT_ROOT = "ROOT";

	public static final String DEBUG_MARK = "!# DEBUG\r\n";

	/**
	 * 所有语句
	 */
	private final List<Node> nodes;

	private final ScriptEngine scriptEngine;

	/**
	 * 存放所有变量定义
	 */
	private final Set<VarIndex> varIndices;

	/**
	 * 编译后的类
	 */
	private MagicScriptVariableAccessRuntime accessRuntime;

	/**
	 * 构造函数
	 */
	private Constructor<MagicScriptRuntime> constructor;

	private final boolean debug;

	private List<Span> spans;

	private String[] varNames;

	private static CompileCache compileCache;

	private MagicScript(List<Node> nodes, Set<VarIndex> varIndices, ScriptEngine scriptEngine, boolean debug) {
		this.nodes = nodes;
		this.varIndices = varIndices;
		this.scriptEngine = scriptEngine;
		this.debug = debug;
	}

	public static void setCompileCache(int capacity) {
		compileCache = new CompileCache(capacity);
	}

	/**
	 * 创建MagicScript
	 */
	public static MagicScript create(String source, ScriptEngine scriptEngine) {
		return create(false, source, scriptEngine);
	}

	/**
	 * 创建MagicScript
	 */
	public static MagicScript create(boolean expression, String source, ScriptEngine scriptEngine) {
		if (compileCache == null) {
			compileCache = new CompileCache(500);
		}
		return compileCache.get(source, () -> {
			Parser parser = new Parser();
			boolean debug = source.startsWith(DEBUG_MARK);
			String script = debug ? source.substring(DEBUG_MARK.length()) : source;
			List<Node> nodes = parser.parse(expression ? "return " + script : script);
			Set<VarIndex> varIndices = parser.getVarIndices();
			return new MagicScript(nodes, varIndices, scriptEngine, debug);
		});
	}

	public Object execute(MagicScriptContext context) {
		MagicScriptRuntime runtime = null;
		try {
			MagicScriptEngine.getDefaultImports().forEach((name, value) -> {
				if (value instanceof DynamicModuleImport) {
					context.set(name, ((DynamicModuleImport) value).getDynamicModule(context));
				} else {
					context.set(name, value);
				}
			});
			runtime = compile();
			return runtime.execute(context);
		} catch (MagicExitException e) {
			return e.getExitValue();
		} catch (MagicScriptCompileException e) {
			throw e;
		} catch (Throwable t) {
			MagicScriptError.transfer(runtime, t);
		}
		return null;
	}

	/**
	 * 编译
	 */
	public MagicScriptRuntime compile() throws MagicScriptCompileException {
		if (this.accessRuntime != null) {
			return this.accessRuntime;
		}
		if (nodes.size() == 1 && nodes.get(0) instanceof Return) {
			Return returnNode = (Return) nodes.get(0);
			if (returnNode.getReturnValue() instanceof VariableAccess) {
				return this.accessRuntime = new MagicScriptVariableAccessRuntime(((VariableAccess) returnNode.getReturnValue()).getVarIndex().getName());
			}
		}
		try {
			MagicScriptCompiler compiler = new MagicScriptCompiler(this.varIndices, this.debug);
			nodes.forEach(node -> node.visitMethod(compiler));
			// 如果只是一个表达式
			if (nodes.size() == 1 && nodes.get(0) instanceof Expression) {
				Node node = nodes.get(0);
				compiler.loadVars();
				compiler.compile(new Return(node.getSpan(), node));
			} else {
				// 根据是否有 import "xxx.xx.xx.*" 来分组
				Map<Boolean, List<Node>> nodeMap = nodes.stream().collect(Collectors.partitioningBy(it -> it instanceof Import && ((Import) it).isImportPackage()));
				// 编译需要的方法
				compiler.compile(nodeMap.get(Boolean.TRUE));    // 先编译 import "xxx.xxx.x.*"
				// 加载变量信息
				compiler.loadVars();
				// 编译其它语句
				compiler.compile(nodeMap.get(Boolean.FALSE));
			}
			Class<MagicScriptRuntime> clazz = new MagicScriptClassLoader(Thread.currentThread().getContextClassLoader()).load(compiler.getClassName(), compiler.bytecode());
			this.constructor = clazz.getConstructor();
			// 设置变量名字
			this.varNames = varIndices.stream().map(VarIndex::getName).toArray(String[]::new);
			// 设置所有Span
			this.spans = compiler.getSpans();
			return buildRuntime();
		} catch (MagicScriptException mse) {
			throw new MagicScriptCompileException(mse.getSimpleMessage(), mse);
		} catch (MagicScriptCompileException e) {
			throw e;
		} catch (Exception e) {
			throw new MagicScriptCompileException(e);
		}
	}

	private MagicScriptRuntime buildRuntime() {
		try {
			MagicScriptRuntime target = constructor.newInstance();
			// 设置变量名字
			target.setVarNames(this.varNames);
			// 设置所有Span
			target.setSpans(this.spans);
			return target;
		} catch (Exception e) {
			throw new MagicScriptCompileException(e);
		}
	}


	@Override
	public Object eval(ScriptContext context) {
		Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
		if (bindings.containsKey(CONTEXT_ROOT)) {
			Object root = bindings.get(CONTEXT_ROOT);
			if (root instanceof MagicScriptContext) {
				MagicScriptContext rootContext = (MagicScriptContext) root;
				return execute(rootContext);
			} else {
				throw new MagicScriptException("参数不正确！");
			}
		}
		MagicScriptContext magicScriptContext = new MagicScriptContext();
		magicScriptContext.putMapIntoContext(context.getBindings(ScriptContext.GLOBAL_SCOPE));
		magicScriptContext.putMapIntoContext(context.getBindings(ScriptContext.ENGINE_SCOPE));
		return execute(magicScriptContext);
	}

	@Override
	public ScriptEngine getEngine() {
		return scriptEngine;
	}
}
