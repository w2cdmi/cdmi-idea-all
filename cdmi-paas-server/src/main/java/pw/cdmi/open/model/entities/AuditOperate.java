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
 * 实体表，记录用户在系统中所做的关键性操作，用于审计.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_audit_operate")
public class AuditOperate {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private Long id;						//审计记录的Id
	private Long userId;					//当前操作用户的Id
	private String eventContent;			//用户什么时间做了什么操作
	private Date eventTime;					//操作时间
	private String siteId;					//对应的应用站点的编号
}

