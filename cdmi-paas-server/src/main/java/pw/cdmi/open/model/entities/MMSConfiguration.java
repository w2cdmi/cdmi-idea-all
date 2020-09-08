package pw.cdmi.open.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体表 ,记录应用所采用的短信服务配置信息. 该表会被应用的配置表， 租户配置表所取代。
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_mms_configuration")
public class MMSConfiguration {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;

    private String url;

    private String password;

    private String signature;

    private String username;

	@Column(name="app_id", nullable = false)
	private String appId;						//数据归属应用ID
	@Column(name="site_id", nullable = true)
	private String siteId;						//对应的平台内子站点Id，这个子站点可能是租户，可以是频道
	@Column(name="tenant_id", nullable = true)
	private String tenantId;					//对应的租户ID
}
