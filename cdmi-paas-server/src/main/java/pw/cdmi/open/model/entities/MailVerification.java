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
 * 实体类，应用中记录需要进行邮件校验操作时的邮件校验信息.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_mail_verification")
public class MailVerification {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 邮件校验信息的编号

	private Long userId;				// 访问者的ID

	private String email; 				// 接受信息的邮件

	private String code;				// 生产的手机检验码

	private String content;				// 校验短信信息

	private String LinkedURL;			// 邮件校验码访问地址

	private String siteId;				// 对应的应用站点

	private Date createTime;			// 检验码生产的时间
}
