package cn.edu.scu.virjarjcd.dom.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * mybatis codegen 不支持泛型方法，通过这个子类对泛型方法功能增强
 * @author virjar
 *
 */
public class VirjarMethod extends Method {
	 private List<String> bodyLines;

	    private boolean constructor;

	    private FullyQualifiedJavaType returnType;

	    private String name;

	    private List<Parameter> parameters;

	    private List<FullyQualifiedJavaType> exceptions;
	    
	    private boolean isSynchronized;
	    
	    private boolean isNative;
	    
	    private List<String> typeParameters;

	    /**
	     *  
	     */
	    public VirjarMethod() {
	        // use a default name to avoid malformed code
	        this("bar"); //$NON-NLS-1$
	    }
	    
	    public VirjarMethod(String name) {
	        super();
	        bodyLines = new ArrayList<String>();
	        parameters = new ArrayList<Parameter>();
	        exceptions = new ArrayList<FullyQualifiedJavaType>();
	        typeParameters = new ArrayList<String>();
	        this.name = name;
	    }
	    
	    /**
	     * Copy constructor.  Not a truly deep copy, but close enough
	     * for most purposes.
	     * 
	     * @param original
	     */
	    public VirjarMethod(VirjarMethod original) {
	        super(original);
	        bodyLines = new ArrayList<String>();
	        parameters = new ArrayList<Parameter>();
	        exceptions = new ArrayList<FullyQualifiedJavaType>();
	        this.bodyLines.addAll(original.bodyLines);
	        this.constructor = original.constructor;
	        this.exceptions.addAll(original.exceptions);
	        this.name = original.name;
	        this.parameters.addAll(original.parameters);
	        this.returnType = original.returnType;
	        this.isNative = original.isNative;
	        this.isSynchronized = original.isSynchronized;
	    }

	    /**
	     * @return Returns the bodyLines.
	     */
	    public List<String> getBodyLines() {
	        return bodyLines;
	    }

	    public void addTypeParameter(String typeParameter){
	    	this.typeParameters.add(typeParameter);
	    }
	    
	    public void addBodyLine(String line) {
	        bodyLines.add(line);
	    }

	    public void addBodyLine(int index, String line) {
	        bodyLines.add(index, line);
	    }

	    public void addBodyLines(Collection<String> lines) {
	        bodyLines.addAll(lines);
	    }

	    public void addBodyLines(int index, Collection<String> lines) {
	        bodyLines.addAll(index, lines);
	    }

	    public String getFormattedContent(int indentLevel, boolean interfaceMethod) {
	        StringBuilder sb = new StringBuilder();

	        addFormattedJavadoc(sb, indentLevel);
	        addFormattedAnnotations(sb, indentLevel);

	        OutputUtilities.javaIndent(sb, indentLevel);

	        if (!interfaceMethod) {
	            sb.append(getVisibility().getValue());

	            if (isStatic()) {
	                sb.append("static "); //$NON-NLS-1$
	            }

	            if (isFinal()) {
	                sb.append("final "); //$NON-NLS-1$
	            }
	            
	            if (isSynchronized()) {
	                sb.append("synchronized "); //$NON-NLS-1$
	            }
	            
	            if (isNative()) {
	                sb.append("native "); //$NON-NLS-1$
	            } else if (bodyLines.size() == 0) {
	                sb.append("abstract "); //$NON-NLS-1$
	            }
	        }

	        if(this.typeParameters.size() >0){
	        	sb.append("<");
	        	for(String type:typeParameters){
	        		sb.append(type);
	        		sb.append(",");
	        	}
	        	sb.setLength(sb.length()-1);
	        	sb.append("> ");
	        }
	        
	        if (!constructor) {
	            if (getReturnType() == null) {
	                sb.append("void"); //$NON-NLS-1$
	            } else {
	                sb.append(getReturnType().getShortName());
	            }
	            sb.append(' ');
	        }

	        sb.append(getName());
	        sb.append('(');

	        boolean comma = false;
	        for (Parameter parameter : getParameters()) {
	            if (comma) {
	                sb.append(", "); //$NON-NLS-1$
	            } else {
	                comma = true;
	            }

	            sb.append(parameter.getFormattedContent());
	        }

	        sb.append(')');

	        if (getExceptions().size() > 0) {
	            sb.append(" throws "); //$NON-NLS-1$
	            comma = false;
	            for (FullyQualifiedJavaType fqjt : getExceptions()) {
	                if (comma) {
	                    sb.append(", "); //$NON-NLS-1$
	                } else {
	                    comma = true;
	                }

	                sb.append(fqjt.getShortName());
	            }
	        }

	        // if no body lines, then this is an abstract method
	        if (bodyLines.size() == 0 || isNative()) {
	            sb.append(';');
	        } else {
	            sb.append(" {"); //$NON-NLS-1$
	            indentLevel++;

	            ListIterator<String> listIter = bodyLines.listIterator();
	            while (listIter.hasNext()) {
	                String line = listIter.next();
	                if (line.startsWith("}")) { //$NON-NLS-1$
	                    indentLevel--;
	                }

	                OutputUtilities.newLine(sb);
	                OutputUtilities.javaIndent(sb, indentLevel);
	                sb.append(line);

	                if ((line.endsWith("{") && !line.startsWith("switch")) //$NON-NLS-1$ //$NON-NLS-2$
	                        || line.endsWith(":")) { //$NON-NLS-1$
	                    indentLevel++;
	                }

	                if (line.startsWith("break")) { //$NON-NLS-1$
	                    // if the next line is '}', then don't outdent
	                    if (listIter.hasNext()) {
	                        String nextLine = listIter.next();
	                        if (nextLine.startsWith("}")) { //$NON-NLS-1$
	                            indentLevel++;
	                        }

	                        // set back to the previous element
	                        listIter.previous();
	                    }
	                    indentLevel--;
	                }
	            }

	            indentLevel--;
	            OutputUtilities.newLine(sb);
	            OutputUtilities.javaIndent(sb, indentLevel);
	            sb.append('}');
	        }

	        return sb.toString();
	    }

	    /**
	     * @return Returns the constructor.
	     */
	    public boolean isConstructor() {
	        return constructor;
	    }

	    /**
	     * @param constructor
	     *            The constructor to set.
	     */
	    public void setConstructor(boolean constructor) {
	        this.constructor = constructor;
	    }

	    /**
	     * @return Returns the name.
	     */
	    public String getName() {
	        return name;
	    }

	    /**
	     * @param name
	     *            The name to set.
	     */
	    public void setName(String name) {
	        this.name = name;
	    }

	    public List<Parameter> getParameters() {
	        return parameters;
	    }

	    public void addParameter(Parameter parameter) {
	        parameters.add(parameter);
	    }

	    public void addParameter(int index, Parameter parameter) {
	        parameters.add(index, parameter);
	    }

	    /**
	     * @return Returns the returnType.
	     */
	    public FullyQualifiedJavaType getReturnType() {
	        return returnType;
	    }

	    /**
	     * @param returnType
	     *            The returnType to set.
	     */
	    public void setReturnType(FullyQualifiedJavaType returnType) {
	        this.returnType = returnType;
	    }

	    /**
	     * @return Returns the exceptions.
	     */
	    public List<FullyQualifiedJavaType> getExceptions() {
	        return exceptions;
	    }

	    public void addException(FullyQualifiedJavaType exception) {
	        exceptions.add(exception);
	    }

	    public boolean isSynchronized() {
	        return isSynchronized;
	    }

	    public void setSynchronized(boolean isSynchronized) {
	        this.isSynchronized = isSynchronized;
	    }

	    public boolean isNative() {
	        return isNative;
	    }

	    public void setNative(boolean isNative) {
	        this.isNative = isNative;
	    }
}
