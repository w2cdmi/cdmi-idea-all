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
 * 实体表，记录系统中的权限变化，用于审计.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_audit_privilege")
public class AuditPrivilege {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						//审计记录的Id
	private Long userId;					//当前操作用户的Id
	private String eventUserId;				//向谁取消或授权权限
	private String eventContent;			//取消或授权权限时间描述,比如：谁取消了谁的什么权限
	private Date eventTime;					//操作时间
	private String siteId;					//对应的应用站点的编号
	
}

