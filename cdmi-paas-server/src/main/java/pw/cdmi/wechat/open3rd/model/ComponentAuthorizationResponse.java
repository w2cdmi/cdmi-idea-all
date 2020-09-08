package pw.cdmi.wechat.open3rd.model;

import lombok.Data;

@Data
public class ComponentAuthorizationResponse {
	private String authorization_info;
	private String authorizer_appid;
	private String authorizer_access_token;
	private int expires_in;
	private String authorizer_refresh_token;
	private FuncInfo[] func_info;
	
	class FuncInfo{
		private FuncscopeCategory funcscope_category;

		public FuncscopeCategory getFuncscope_category() {
			return funcscope_category;
		}

		public void setFuncscope_category(FuncscopeCategory funcscope_category) {
			this.funcscope_category = funcscope_category;
		}
	}
	
	class FuncscopeCategory{
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}
