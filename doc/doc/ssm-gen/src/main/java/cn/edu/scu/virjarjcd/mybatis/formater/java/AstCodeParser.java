package cn.edu.scu.virjarjcd.mybatis.formater.java;

import static org.mybatis.generator.api.dom.OutputUtilities.calculateImports;
import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.api.dom.OutputUtilities.newLine;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.TopLevelEnumeration;
import org.mybatis.generator.config.Context;

import cn.edu.scu.virjarjcd.dom.java.ParameterTopLevelClass;
import cn.edu.scu.virjarjcd.util.ImportsSorter;
import cn.edu.scu.virjarjcd.util.JDTCodeParser;

public class AstCodeParser implements JavaFormatter {

    // private Context context;
    @Override
    public void setContext(Context context) {
        // this.context = context;
    }

    @Override
    public String getFormattedContent(CompilationUnit compilationUnit) {
        String code;
		//调用import顺序优化代码
        if (compilationUnit instanceof TopLevelClass) {
            code = formatTopLevelClass((TopLevelClass) compilationUnit);
        } else if (compilationUnit instanceof Interface) {
            code = formatInterface((Interface) compilationUnit);
        } else if(compilationUnit instanceof TopLevelEnumeration){
			code = formatTopLevelEnumeration((TopLevelEnumeration) compilationUnit);
		}
		else{
            code = compilationUnit.getFormattedContent();
        }

		//真正调用eclipse组件格式化代码
        try {
            code = new JDTCodeParser().format(code);
        } catch (MalformedTreeException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return code;
    }

    private String formatTopLevelEnumeration(TopLevelEnumeration topLevelEnumeration) {
        StringBuilder sb = new StringBuilder();

        for (String fileCommentLine : topLevelEnumeration.getFileCommentLines()) {
            sb.append(fileCommentLine);
            newLine(sb);
        }

        if (topLevelEnumeration.getType().getPackageName() != null
                && topLevelEnumeration.getType().getPackageName().length() > 0) {
            sb.append("package "); //$NON-NLS-1$
            sb.append(topLevelEnumeration.getType().getPackageName());
            sb.append(';');
            newLine(sb);
            newLine(sb);
        }

        for (String staticImport : topLevelEnumeration.getStaticImports()) {
            sb.append("import static "); //$NON-NLS-1$
            sb.append(staticImport);
            sb.append(';');
            newLine(sb);
        }

        if (topLevelEnumeration.getStaticImports().size() > 0) {
            newLine(sb);
        }

        Set<String> importStrings = calculateImports(topLevelEnumeration.getImportedTypes());
        for (String importString : importStrings) {
            sb.append(importString);
            newLine(sb);
        }

        if (importStrings.size() > 0) {
            newLine(sb);
        }

        try {
            Method getFormattedContent = InnerEnum.class.getMethod("getFormattedContent", int.class);
            String content = (String) getFormattedContent.invoke(topLevelEnumeration, 0);
            sb.append(content);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();// do not happen
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String formatInterface(Interface interfaze) {
        StringBuilder sb = new StringBuilder();

        for (String commentLine : interfaze.getFileCommentLines()) {
            sb.append(commentLine);
            newLine(sb);
        }

        if (stringHasValue(interfaze.getType().getPackageName())) {
            sb.append("package "); //$NON-NLS-1$
            sb.append(interfaze.getType().getPackageName());
            sb.append(';');
            newLine(sb);
            newLine(sb);
        }
        // 这里重写import排序
        List<String> imports = new ArrayList<>();
        for (String staticImport : interfaze.getStaticImports()) {
            imports.add("import static " + staticImport + ";");
        }
        imports.addAll(calculateImports(interfaze.getImportedTypes()));
        List<String> sorted = new ImportsSorter(null).sort(imports);
        for (String sortedImport : sorted) {
            sb.append(sortedImport);
        }

        // for (String staticImport : interfaze.getStaticImports()) {
        //			sb.append("import static "); //$NON-NLS-1$
        // sb.append(staticImport);
        // sb.append(';');
        // newLine(sb);
        // }
        //
        // if (interfaze.getStaticImports().size() > 0) {
        // newLine(sb);
        // }
        //
        // Set<String> importStrings = calculateImports(interfaze.getImportedTypes());
        // for (String importString : importStrings) {
        // sb.append(importString);
        // newLine(sb);
        // }
        //
        // if (importStrings.size() > 0) {
        // newLine(sb);
        // }

        int indentLevel = 0;

        interfaze.addFormattedJavadoc(sb, indentLevel);
        interfaze.addFormattedAnnotations(sb, indentLevel);

        sb.append(interfaze.getVisibility().getValue());

        if (interfaze.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (interfaze.isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        sb.append("interface "); //$NON-NLS-1$
        sb.append(interfaze.getType().getShortName());

        if (interfaze.getSuperInterfaceTypes().size() > 0) {
            sb.append(" extends "); //$NON-NLS-1$

            boolean comma = false;
            for (FullyQualifiedJavaType fqjt : interfaze.getSuperInterfaceTypes()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }

                sb.append(fqjt.getShortName());
            }
        }

        sb.append(" {"); //$NON-NLS-1$
        indentLevel++;

        Iterator<org.mybatis.generator.api.dom.java.Method> mtdIter = interfaze.getMethods().iterator();
        while (mtdIter.hasNext()) {
            newLine(sb);
            org.mybatis.generator.api.dom.java.Method method = mtdIter.next();
            sb.append(method.getFormattedContent(indentLevel, true));
            if (mtdIter.hasNext()) {
                newLine(sb);
            }
        }

        indentLevel--;
        newLine(sb);
        javaIndent(sb, indentLevel);
        sb.append('}');

        return sb.toString();
    }

    private String formatTopLevelClass(TopLevelClass topLevelClass) {
        StringBuilder sb = new StringBuilder(1024);
        for (String fileCommentLine : topLevelClass.getFileCommentLines()) {
            sb.append(fileCommentLine);
            newLine(sb);
        }

        if (stringHasValue(topLevelClass.getType().getPackageName())) {
            sb.append("package "); //$NON-NLS-1$
            sb.append(topLevelClass.getType().getPackageName());
            sb.append(';');
            newLine(sb);
            newLine(sb);
        }

        // 这里重写import排序
        List<String> imports = new ArrayList<>();
        for (String staticImport : topLevelClass.getStaticImports()) {
            imports.add("import static " + staticImport + ";");
        }
        imports.addAll(calculateImports(topLevelClass.getImportedTypes()));
        List<String> sorted = new ImportsSorter(null).sort(imports);
        for (String sortedImport : sorted) {
            sb.append(sortedImport);
        }

        // for (String staticImport : topLevelClass.getStaticImports()) {
        //			sb.append("import static "); //$NON-NLS-1$
        // sb.append(staticImport);
        // sb.append(';');
        // newLine(sb);
        // }
        //
        // if (topLevelClass.getStaticImports().size() > 0) {
        // newLine(sb);
        // }
        //
        // Set<String> importStrings = calculateImports(topLevelClass.getImportedTypes());
        // for (String importString : importStrings) {
        // sb.append(importString);
        // newLine(sb);
        // }
        //
        // if (importStrings.size() > 0) {
        // newLine(sb);
        // }

        if (topLevelClass instanceof ParameterTopLevelClass) {
            ParameterTopLevelClass parameterTopLevelClass = (ParameterTopLevelClass) topLevelClass;
            sb.append(parameterTopLevelClass.getinnerclassFormattedContent(0));
        } else {
            try {
                Method getFormattedContent = InnerClass.class.getMethod("getFormattedContent", int.class);
                String content = (String) getFormattedContent.invoke(topLevelClass, 0);
                sb.append(content);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();// do not happen
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            // sb.append()
        }
        return sb.toString();

    }
}
