package groovy.template;

/** 公共使用状态 */
public enum AbstractActiveStatus{
	enabled("0", "启用"),
	disable("1", "停用");
	String code;
	String name;
	AbstractActiveStatus(String code, String name){
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}