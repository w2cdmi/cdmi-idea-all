package pw.cdmi.paas.developer.model;

import lombok.Data;
@Data
public class NewDeveloper {
	private String name;
	
	private String licence; 	//公司营业执照号码
	
	private String type;
	
	private String provinceId;	//公司或人员工商所在地-省份
	
	private String cityId;		//公司工商所在地-城市
	
	private String linkname;	//开发者账号联系人名称
	
	private String linkemail;	//开发者账号联系人邮件地址
	
	private String linkmobile;	//开发者账号联系人手机号码

}
