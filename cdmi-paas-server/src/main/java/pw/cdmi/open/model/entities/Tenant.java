package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import pw.cdmi.open.model.TenantClass;

/****************************************************
 * 
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
@Data
@Entity
@Table(name = "p_tenant")
public class Tenant {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 租户的信息编号
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TenantClass type;				// 租户的类型
	
	private Long refCustomerId;			    //对应的可能是User对象的Id, 或Company的id
	
	@Column(length = 28)
	private String linkMan;					//对应的租户的联系人
	
	@Column(length = 19)
	private String linkPhone;				//对应的租户的联系电话
	
	@Column(length = 24)
	private String linkMail;				//对应的租户的联系邮件
	
	private Long mangerId;					//租户的管理员Id，对应UserAccount表
	
	private Date createTime;				//加入成为租户的时间
	
	private String siteId;					//对应的应用子站的编号
	
	private String appId;					//对应的接入应用的编号
}
