package pw.cdmi.paas.manager.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "p_manager")
public class Manager {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;							//系统管理员的Id
	
	private String userId;						//系统对应的平台用户账号Id
	
    private String wxUserId;					//对应的微信用户表的Id
    
    private String wxUnionId;					//对应的微信用户的UnionId
    
    private String wxOpenId;					//对应的微信用户的OpenId
    
    private String[] thirdUserTypes;			//关联的用户类型，只有wx的话，这里就只有WeChat
    
}
