package pw.cdmi.wechat.open3rd.model;

import lombok.Data;
import pw.cdmi.wechat.open3rd.model.ComponentAuthorizationResponse.FuncscopeCategory;

@Data
public class MiniProAuthorizerInfoResponse {
	private AuthorizerInfo authorizer_info;
	private AuthorizationInfo authorization_info;

	class AuthorizerInfo {
		private String nick_name;
		private String head_img;
		private ServiceTypeInfo service_type_info;
		private VerifyTypeInfo verify_type_info;
		private String user_name;
		private String principal_name;
		private BusinessInfo business_info;
		private String alias;
		private String qrcode_url;
		private String signature;
		private MiniProgramInfo MiniProgramInfo;
		
		public String getHead_img() {
			return head_img;
		}
		public void setHead_img(String head_img) {
			this.head_img = head_img;
		}
		public String getNick_name() {
			return nick_name;
		}
		public void setNick_name(String nick_name) {
			this.nick_name = nick_name;
		}
		public ServiceTypeInfo getService_type_info() {
			return service_type_info;
		}
		public void setService_type_info(ServiceTypeInfo service_type_info) {
			this.service_type_info = service_type_info;
		}
		public VerifyTypeInfo getVerify_type_info() {
			return verify_type_info;
		}
		public void setVerify_type_info(VerifyTypeInfo verify_type_info) {
			this.verify_type_info = verify_type_info;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getPrincipal_name() {
			return principal_name;
		}
		public void setPrincipal_name(String principal_name) {
			this.principal_name = principal_name;
		}
		public BusinessInfo getBusiness_info() {
			return business_info;
		}
		public void setBusiness_info(BusinessInfo business_info) {
			this.business_info = business_info;
		}
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		public String getQrcode_url() {
			return qrcode_url;
		}
		public void setQrcode_url(String qrcode_url) {
			this.qrcode_url = qrcode_url;
		}
		public String getSignature() {
			return signature;
		}
		public void setSignature(String signature) {
			this.signature = signature;
		}
		public MiniProgramInfo getMiniProgramInfo() {
			return MiniProgramInfo;
		}
		public void setMiniProgramInfo(MiniProgramInfo miniProgramInfo) {
			MiniProgramInfo = miniProgramInfo;
		}
	}
	
	class AuthorizationInfo {
		private String authorization_appid;
		private FuncInfo[] func_info;
		
		public String getAuthorization_appid() {
			return authorization_appid;
		}
		public void setAuthorization_appid(String authorization_appid) {
			this.authorization_appid = authorization_appid;
		}
		public FuncInfo[] getFunc_info() {
			return func_info;
		}
		public void setFunc_info(FuncInfo[] func_info) {
			this.func_info = func_info;
		}
	}
	
	class MiniProgramInfo {
		private Network network;
		private MiniProgramCategory[] categories;
		private int visit_status;
		
		public Network getNetwork() {
			return network;
		}
		public void setNetwork(Network network) {
			this.network = network;
		}
		public MiniProgramCategory[] getCategories() {
			return categories;
		}
		public void setCategories(MiniProgramCategory[] categories) {
			this.categories = categories;
		}
		public int getVisit_status() {
			return visit_status;
		}
		public void setVisit_status(int visit_status) {
			this.visit_status = visit_status;
		}
	}
	
	class Network {
		private String RequestDomain;
		private String WsRequestDomain;
		private String UploadDomain;
		private String DownloadDomain;
		
		public String getRequestDomain() {
			return RequestDomain;
		}
		public void setRequestDomain(String requestDomain) {
			RequestDomain = requestDomain;
		}
		public String getWsRequestDomain() {
			return WsRequestDomain;
		}
		public void setWsRequestDomain(String wsRequestDomain) {
			WsRequestDomain = wsRequestDomain;
		}
		public String getUploadDomain() {
			return UploadDomain;
		}
		public void setUploadDomain(String uploadDomain) {
			UploadDomain = uploadDomain;
		}
		public String getDownloadDomain() {
			return DownloadDomain;
		}
		public void setDownloadDomain(String downloadDomain) {
			DownloadDomain = downloadDomain;
		}
	}
	
	class MiniProgramCategory{
		private String first;
		private String second;
		
		public String getFirst() {
			return first;
		}
		public void setFirst(String first) {
			this.first = first;
		}
		public String getSecond() {
			return second;
		}
		public void setSecond(String second) {
			this.second = second;
		}
	}
	
	class ServiceTypeInfo {
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
	
	class VerifyTypeInfo {
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
	
	class BusinessInfo {
		private int open_store;
		private int open_scan;
		private int open_pay;
		private int open_card;
		private int open_shake;
		
		public int getOpen_store() {
			return open_store;
		}
		public void setOpen_store(int open_store) {
			this.open_store = open_store;
		}
		public int getOpen_scan() {
			return open_scan;
		}
		public void setOpen_scan(int open_scan) {
			this.open_scan = open_scan;
		}
		public int getOpen_card() {
			return open_card;
		}
		public void setOpen_card(int open_card) {
			this.open_card = open_card;
		}
		public int getOpen_shake() {
			return open_shake;
		}
		public void setOpen_shake(int open_shake) {
			this.open_shake = open_shake;
		}
		public int getOpen_pay() {
			return open_pay;
		}
		public void setOpen_pay(int open_pay) {
			this.open_pay = open_pay;
		}
	}
	
	class FuncInfo{
		private FuncscopeCategory funcscope_category;

		public FuncscopeCategory getFuncscope_category() {
			return funcscope_category;
		}

		public void setFuncscope_category(FuncscopeCategory funcscope_category) {
			this.funcscope_category = funcscope_category;
		}
	}
}
