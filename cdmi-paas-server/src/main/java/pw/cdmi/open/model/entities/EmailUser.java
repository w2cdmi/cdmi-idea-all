package pw.cdmi.open.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 该类保存了系统采集到的原始邮件地址信息
 * @author 伍伟
 *
 */
@Data
@Entity
@Table(name = "p_email_user")
public class EmailUser {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;								// 信息编号
	
	private String email;			//邮箱密码
	
	private String password;		//邮箱的密码
	
	private String name;			//对应人的称呼
	
	private String peopleId;		//对应人的信息编号
		
	private String userId;			//对应系统内帐号信息编号
}
