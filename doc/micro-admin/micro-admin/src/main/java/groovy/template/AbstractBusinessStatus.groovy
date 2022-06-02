package groovy.template;

	public enum AbstractBusinessStatus{
		
		GIVEUP_RENTAL("120", "放弃租赁"),
		NOT_PASS_RENTAL("160", "拒租"),
		CONTRACT_AWARD("180", "合同签约"),
		AUDIT_AFTER_CONTRACT_SIGNING("190", "合同签约后审核"),
		RENEW_THE_CONTRACT("200", "重新签约"),
		PENDING_RENT("210", "待交租");
		
		String code;
		String name;
		AbstractBusinessStatus(String code, String name){
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