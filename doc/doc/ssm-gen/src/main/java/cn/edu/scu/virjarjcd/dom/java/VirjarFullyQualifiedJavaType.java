package cn.edu.scu.virjarjcd.dom.java;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;

/**
 *  对原生类的一个包装，允许shortName里面带有package
 *  <ul>
 *  <li>java代码里面，可能引用同名不同包的类，这时需要支持在代码块里面写类的全路径</li>
 *  <li>访问内部类的时候，习惯引用外部类，然后通过外部类限定内部类Type</li>
 *  </ul>
 *  不会解析包路径
 *  @author weijia.deng
 */
public class VirjarFullyQualifiedJavaType extends FullyQualifiedJavaType {


	/**
	 * The short name without any generic arguments
	 */
	private String baseShortName;

	/**
	 * The fully qualified name without any generic arguments
	 */
	private String baseQualifiedName;

	private boolean explicitlyImported;
	private String packageName;
	private boolean primitive;
	private PrimitiveTypeWrapper primitiveTypeWrapper;
	private List<FullyQualifiedJavaType> typeArguments;

	// the following three values are used for dealing with wildcard types
	private boolean wildcardType;
	private boolean boundedWildcard;
	private boolean extendsBoundedWildcard;

	/**
	 * Use this constructor to construct a generic type with the specified type
	 * parameters
	 * 
	 * @param fullTypeSpecification
	 */
	public VirjarFullyQualifiedJavaType(String fullTypeSpecification) {
		super(fullTypeSpecification);
		typeArguments = new ArrayList<FullyQualifiedJavaType>();
		parse(fullTypeSpecification);
	}

	/**
	 * @return Returns the explicitlyImported.
	 */
	public boolean isExplicitlyImported() {
		return explicitlyImported;
	}

	/**
	 * This method returns the fully qualified name - including any generic type
	 * parameters
	 * 
	 * @return Returns the fullyQualifiedName.
	 */
	public String getFullyQualifiedName() {
		StringBuilder sb = new StringBuilder();
		if (wildcardType) {
			sb.append('?');
			if (boundedWildcard) {
				if (extendsBoundedWildcard) {
					sb.append(" extends "); //$NON-NLS-1$
				} else {
					sb.append(" super "); //$NON-NLS-1$
				}

				sb.append(baseQualifiedName);
			}
		} else {
			sb.append(baseQualifiedName);
		}

		if (typeArguments.size() > 0) {
			boolean first = true;
			sb.append('<');
			for (FullyQualifiedJavaType fqjt : typeArguments) {
				if (first) {
					first = false;
				} else {
					sb.append(", "); //$NON-NLS-1$
				}
				sb.append(fqjt.getFullyQualifiedName());

			}
			sb.append('>');
		}

		return sb.toString();
	}

	/**
	 * Returns a list of Strings that are the fully qualified names of this
	 * type, and any generic type argument associated with this type.
	 */
	public List<String> getImportList() {
		List<String> answer = new ArrayList<String>();
		if (isExplicitlyImported()) {
			int index = baseShortName.indexOf('.');
			if (index == -1) {
				answer.add(baseQualifiedName);
			} else {
				// an inner class is specified, only import the top
				// level class
				StringBuilder sb = new StringBuilder();
				sb.append(packageName);
				sb.append('.');
				sb.append(baseShortName.substring(0, index));
				answer.add(sb.toString());
			}
		}

		for (FullyQualifiedJavaType fqjt : typeArguments) {
			answer.addAll(fqjt.getImportList());
		}

		return answer;
	}

	/**
	 * @return Returns the packageName.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return Returns the shortName - including any type arguments.
	 */
	public String getShortName() {
		StringBuilder sb = new StringBuilder();
		if (wildcardType) {
			sb.append('?');
			if (boundedWildcard) {
				if (extendsBoundedWildcard) {
					sb.append(" extends "); //$NON-NLS-1$
				} else {
					sb.append(" super "); //$NON-NLS-1$
				}

				sb.append(baseShortName);
			}
		} else {
			sb.append(baseShortName);
		}

		if (typeArguments.size() > 0) {
			boolean first = true;
			sb.append('<');
			for (FullyQualifiedJavaType fqjt : typeArguments) {
				if (first) {
					first = false;
				} else {
					sb.append(", "); //$NON-NLS-1$
				}
				sb.append(fqjt.getShortName());

			}
			sb.append('>');
		}

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FullyQualifiedJavaType)) {
			return false;
		}

		FullyQualifiedJavaType other = (FullyQualifiedJavaType) obj;

		return getFullyQualifiedName().equals(other.getFullyQualifiedName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getFullyQualifiedName().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFullyQualifiedName();
	}

	/**
	 * @return Returns the primitive.
	 */
	public boolean isPrimitive() {
		return primitive;
	}

	/**
	 * @return Returns the wrapperClass.
	 */
	public PrimitiveTypeWrapper getPrimitiveTypeWrapper() {
		return primitiveTypeWrapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(FullyQualifiedJavaType other) {
		return getFullyQualifiedName().compareTo(other.getFullyQualifiedName());
	}

	public void addTypeArgument(FullyQualifiedJavaType type) {
		typeArguments.add(type);
	}

	private void parse(String fullTypeSpecification) {
		String spec = fullTypeSpecification.trim();

		if (spec.startsWith("?")) { //$NON-NLS-1$
			wildcardType = true;
			spec = spec.substring(1).trim();
			if (spec.startsWith("extends ")) { //$NON-NLS-1$
				boundedWildcard = true;
				extendsBoundedWildcard = true;
				spec = spec.substring(8);
			} else if (spec.startsWith("super ")) { //$NON-NLS-1$
				boundedWildcard = true;
				extendsBoundedWildcard = false;
				spec = spec.substring(6);
			} else {
				boundedWildcard = false;
			}
			parse(spec);
		} else {
			int index = fullTypeSpecification.indexOf('<');
			if (index == -1) {
				simpleParse(fullTypeSpecification);
			} else {
				simpleParse(fullTypeSpecification.substring(0, index));
				genericParse(fullTypeSpecification.substring(index));
			}
		}
	}

	private void simpleParse(String typeSpecification) {
		baseQualifiedName = typeSpecification.trim();

		baseShortName = baseQualifiedName;
		explicitlyImported = false;
		packageName = ""; //$NON-NLS-1$

		if ("byte".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper.getByteInstance();
		} else if ("short".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper.getShortInstance();
		} else if ("int".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper
					.getIntegerInstance();
		} else if ("long".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper.getLongInstance();
		} else if ("char".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper
					.getCharacterInstance();
		} else if ("float".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper.getFloatInstance();
		} else if ("double".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper.getDoubleInstance();
		} else if ("boolean".equals(baseQualifiedName)) { //$NON-NLS-1$
			primitive = true;
			primitiveTypeWrapper = PrimitiveTypeWrapper
					.getBooleanInstance();
		} else {
			primitive = false;
			primitiveTypeWrapper = null;
		}

	}

	private void genericParse(String genericSpecification) {
		int lastIndex = genericSpecification.lastIndexOf('>');
		if (lastIndex == -1) {
			throw new RuntimeException(getString(
					"RuntimeError.22", genericSpecification)); //$NON-NLS-1$
		}
		String argumentString = genericSpecification.substring(1, lastIndex);
		// need to find "," outside of a <> bounds
		StringTokenizer st = new StringTokenizer(argumentString, ",<>", true); //$NON-NLS-1$
		int openCount = 0;
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if ("<".equals(token)) { //$NON-NLS-1$
				sb.append(token);
				openCount++;
			} else if (">".equals(token)) { //$NON-NLS-1$
				sb.append(token);
				openCount--;
			} else if (",".equals(token)) { //$NON-NLS-1$
				if (openCount == 0) {
					typeArguments
					.add(new FullyQualifiedJavaType(sb.toString()));
					sb.setLength(0);
				} else {
					sb.append(token);
				}
			} else {
				sb.append(token);
			}
		}

		if (openCount != 0) {
			throw new RuntimeException(getString(
					"RuntimeError.22", genericSpecification)); //$NON-NLS-1$
		}

		String finalType = sb.toString();
		if (stringHasValue(finalType)) {
			typeArguments.add(new FullyQualifiedJavaType(finalType));
		}
	}
}
