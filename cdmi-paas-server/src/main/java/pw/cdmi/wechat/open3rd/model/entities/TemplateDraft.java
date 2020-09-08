package pw.cdmi.wechat.open3rd.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 数据直接从微信平台获取，不保存到我们的数据库中
 * @author No.11
 *
 */
@Deprecated
@Data
@Entity
@Table(name = "p_wechat_open3rd_draft")
public class TemplateDraft {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;								//数据编号Id
	
	private String draftId;

	private String userVersion;						// 代码版本号，开发者可自定义
	
	private String userDesc;						// 代码描述，开发者可自定义
	
	private String createTime;						// 开发者上传草稿时间
	
	private String ownerId;							// 小程序模板的拥有人，对应租户Id，非必须
	
	private String creatorId;						// 小程序模板的创建人，对应UserAccount，非必须
	
	private String appId;							// 对应的应用Id
}
