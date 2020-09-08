package pw.cdmi.paas.app.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;
import pw.cdmi.paas.app.model.AuthApplicationType;
import pw.cdmi.paas.app.model.SiteAccessStatus;
import pw.cdmi.paas.app.model.SiteAttribution;
import pw.cdmi.paas.app.model.SiteStatus;

/****************************************************
 * 平台接入应用信息。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
@Data
@Entity
@Table(name = "p_authapp")
public class AuthApplication {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String appId;						// 应用的ID，固定字符串长度

	@Column(name="name", nullable=false)
    private String appName;						// 应用的名称表示，比如：智能运维系统

    private String endpoint;					// 应用的访问域名
    
	@Column(name="url_icon",nullable=true)		// 设置应用的LOGO图标
	private String urlIcon;
    
    private String labels;                      // 为应用贴标签

    private String description;					// 应用的简要描述

	@CreatedDate
	@Column(name="create_time",nullable=false)
    private Date createTime;					// 应用创建的时间
    
	@CreatedDate
	@Column(name="update_time",nullable=true)
	private Date updateTime;   

    private SiteAttribution attribution;		// 应用的归属，是AWS附属服务组件，还是服务类组件，或者是客户接入应用
    
    private AuthApplicationType type;			// 应用的类型
    
    private SiteStatus status;                  // 应用站点的状态
    
    private SiteAccessStatus accessStatus;		// 应用的接入状态

	//对应的开发者
	@Column(name="developer_id")
    private String developerId;					// 应用对应的开发者Id
    
    private String tenantId;					// 应用的拥有人，对应租户表Id，如果是SiteAttribution为Self，则该属性为空。
  
}
