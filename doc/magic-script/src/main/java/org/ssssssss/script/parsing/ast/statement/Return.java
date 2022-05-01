package org.ssssssss.script.parsing.ast.statement;

import org.ssssssss.script.compile.MagicScriptCompiler;
import org.ssssssss.script.parsing.Span;
import org.ssssssss.script.parsing.ast.Node;

import java.util.List;

public class Return extends Node {

	private final Node returnValue;

	public Return(Span span, Node returnValue) {
		super(span);
		this.returnValue = returnValue;
	}

	public Node getReturnValue() {
		return returnValue;
	}

	@Override
	public void visitMethod(MagicScriptCompiler compiler) {
		if(returnValue != null){
			returnValue.visitMethod(compiler);
		}
	}

	@Override
	public void compile(MagicScriptCompiler compiler) {
		List<Node> block = compiler.getFinallyBlock();
		if (returnValue == null) {    // return
			if (block != null) {    // 如果有finally块
				compiler.compile(block);    // 执行finally块
			}
			compiler.insn(ACONST_NULL);    // 压入 NULL
		} else {    // return expr;
			compiler.visit(returnValue);
			if (block != null) {    // 如果有finally块
				compiler.store(3)    // 保存返回结果
						.compile(block)    // 执行 finally
						.load3();    // 加载返回结果
			}
		}
		if(block != null){
			compiler.putFinallyBlock(block);
		}
		compiler.insn(ARETURN);     // 返回
	}
}