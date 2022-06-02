package groovy.template;

/** 公共使用状态 */
public enum AbstractResultCode{
	success("000", "成功"),
	un_login("001", "未登录，或登录过期"),
	user_locked("002", "账户已锁定"),
	password_error("003", "用户名或密码错误"),
	duplication_user_name("004", "存在同登录名的用户"),
	admin_cant_not_stop("005", "系统管理员账户不能禁用"),
	role_id_already_exist ("006", "角色标识已存在"),
	cant_not_add_system_manager ("007", "不允许创建或修改系统管理员"),

	menu_code_exist ("010", "编码已存在"),
	menu_url_exist ("011", "链接已存在"),
	error("999", "错误"),
	;
	String code;
	String name;

	AbstractResultCode(String code, String name){
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