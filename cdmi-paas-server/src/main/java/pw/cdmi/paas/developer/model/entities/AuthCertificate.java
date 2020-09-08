package pw.cdmi.paas.developer.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
@Entity
@Table(name="p_authcertificate")
public class AuthCertificate {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;	
	
	//对应的开发者的访问授权码
	@Column(name="accesskey",nullable=false)
	private String accessKey;
	
	//对应的开发者的访问密钥
	@Column(name="secretkey",nullable=false)
	private String secretKey;
	
	//对应的开发者
	@Column(name="developer_id",nullable=false)
	private String developerId;
	
//	//对应的开发者账号的管理用户，对应的是accountId
//	@Column(name="developer_manager_id",nullable=false)
//	private String managerId;
	
	@CreatedDate
	private Date createTime;
	@CreatedDate
	private Date upDateTime;
}
