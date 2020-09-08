package pw.cdmi.wechat.open3rd.extapp.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "p_wechat_open3rd_extapp")
public class ExtApp {
	@Id
    private String extAppId;						// 对应的授权小程序Id
	
	private String templateId;						// 对应第三方平台小程序代码库中的代码模版ID
	
	private String userVersion;						// 代码版本号，开发者可自定义
	
	private String userDesc;						// 代码描述，开发者可自定义
	
	private String extJson;							// 第三方自定义的配置
	
	private String createTime;						// 小程序上传时间
	
	private String ownerId;							// 小程序的拥有人，对应租户Id，非必须
	
	private String creatorId;						// 小程序的创建人，对应UserAccount，非必须
	
	private String appId;							// 对应的应用Id
}
