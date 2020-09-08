package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体表，用于记录用户对系统中的关键资源的访问，用于审计.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_audit_resource")
public class AuditResource {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						//审计记录的Id
	private Long userId;					//当前操作用户的Id
	private String resourceClass;			//用户访问的资源的分类
	private String resourceUrl;				//用户访问的资源路径
	private String eventContent;			//用户什么时间做了什么操作
	private Date eventTime;					//操作时间
	private String iPAddress;				//用户访问时的IP地址
	private String siteId;					//对应的应用站点的编号
}

