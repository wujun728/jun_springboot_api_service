package cn.edu.scu.virjarjcd.dom.java;

import static org.mybatis.generator.api.dom.OutputUtilities.calculateImports;
import static org.mybatis.generator.api.dom.OutputUtilities.newLine;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InitializationBlock;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ParameterTopLevelClass extends TopLevelClass {

	private List<String> typeArguments = new ArrayList<String>();
	public ParameterTopLevelClass(String fullTypeSpecification) {
		super(fullTypeSpecification);
		// TODO Auto-generated constructor stub
	}
	
	public void addTypeArgument(String typeArgument){
		this.typeArguments.add(typeArgument);
	}
	
	public String getFormattedContent() {
		StringBuilder sb = new StringBuilder();

		for (String fileCommentLine : this.getFileCommentLines()) {
			sb.append(fileCommentLine);
			newLine(sb);
		}

		if (stringHasValue(getType().getPackageName())) {
			sb.append("package "); //$NON-NLS-1$
			sb.append(getType().getPackageName());
			sb.append(';');
			newLine(sb);
			newLine(sb);
		}

		for (String staticImport : this.getStaticImports()) {
			sb.append("import static "); //$NON-NLS-1$
			sb.append(staticImport);
			sb.append(';');
			newLine(sb);
		}

		if (this.getStaticImports().size() > 0) {
			newLine(sb);
		}

		Set<String> importStrings = calculateImports(this.getImportedTypes());
		for (String importString : importStrings) {
			sb.append(importString);
			newLine(sb);
		}

		if (importStrings.size() > 0) {
			newLine(sb);
		}

		sb.append(getinnerclassFormattedContent(0));

		return sb.toString();
	}

	public String getinnerclassFormattedContent(int indentLevel) {
		StringBuilder sb = new StringBuilder();

		addFormattedJavadoc(sb, indentLevel);
		addFormattedAnnotations(sb, indentLevel);

		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append(getVisibility().getValue());

		if (isAbstract()) {
			sb.append("abstract "); //$NON-NLS-1$
		}

		if (isStatic()) {
			sb.append("static "); //$NON-NLS-1$
		}

		if (isFinal()) {
			sb.append("final "); //$NON-NLS-1$
		}

		sb.append("class "); //$NON-NLS-1$
		sb.append(getType().getShortName());
		if(this.typeArguments.size() >0){
			sb.append("<");
			Iterator<String> iterator = this.typeArguments.iterator();
			if(iterator.hasNext()){
				sb.append(iterator.next());
			}
			while(iterator.hasNext()){
				sb.append(",");
				sb.append(iterator.next());
			}
			sb.append(">");
		}

		if (this.getSuperClass() != null) {
			sb.append(" extends "); //$NON-NLS-1$
			sb.append(this.getSuperClass().getShortName());
		}

		if (this.getSuperInterfaceTypes().size() > 0) {
			sb.append(" implements "); //$NON-NLS-1$

			boolean comma = false;
			for (FullyQualifiedJavaType fqjt : this.getSuperInterfaceTypes()) {
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

		Iterator<Field> fldIter = this.getFields().iterator();
		while (fldIter.hasNext()) {
			OutputUtilities.newLine(sb);
			Field field = fldIter.next();
			sb.append(field.getFormattedContent(indentLevel));
			if (fldIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (this.getInitializationBlocks().size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<InitializationBlock> blkIter = this.getInitializationBlocks().iterator();
		while (blkIter.hasNext()) {
			OutputUtilities.newLine(sb);
			InitializationBlock initializationBlock = blkIter.next();
			sb.append(initializationBlock.getFormattedContent(indentLevel));
			if (blkIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (this.getMethods().size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<Method> mtdIter = this.getMethods().iterator();
		while (mtdIter.hasNext()) {
			OutputUtilities.newLine(sb);
			Method method = mtdIter.next();
			sb.append(method.getFormattedContent(indentLevel, false));
			if (mtdIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (this.getInnerClasses().size() > 0) {
			OutputUtilities.newLine(sb);
		}
		Iterator<InnerClass> icIter = this.getInnerClasses().iterator();
		while (icIter.hasNext()) {
			OutputUtilities.newLine(sb);
			InnerClass innerClass = icIter.next();
			sb.append(innerClass.getFormattedContent(indentLevel));
			if (icIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		if (this.getInnerEnums().size() > 0) {
			OutputUtilities.newLine(sb);
		}

		Iterator<InnerEnum> ieIter = this.getInnerEnums().iterator();
		while (ieIter.hasNext()) {
			OutputUtilities.newLine(sb);
			InnerEnum innerEnum = ieIter.next();
			sb.append(innerEnum.getFormattedContent(indentLevel));
			if (ieIter.hasNext()) {
				OutputUtilities.newLine(sb);
			}
		}

		indentLevel--;
		OutputUtilities.newLine(sb);
		OutputUtilities.javaIndent(sb, indentLevel);
		sb.append('}');

		return sb.toString();
}
}
