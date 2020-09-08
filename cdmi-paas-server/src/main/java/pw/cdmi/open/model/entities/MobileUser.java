package pw.cdmi.open.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 该类保存了系统采集到的原始手机号码信息
 * @author 伍伟
 *
 */
@Data
@Entity
@Table(name = "p_mobile_user")
public class MobileUser {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;				// 信息编号
	
	private String mobile;			//手机号码
	
	private String name;			//对应人的称呼
	
	private String peopleId;		//对应人的信息编号
		
	private String userId;			//对应系统内帐号信息编号
}
