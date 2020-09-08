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
import pw.cdmi.paas.developer.model.DeveloperType;


@Data
@Entity
@Table(name = "p_developer")
public class Developer {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;							//开发者id
	
	private String userId;						//开发者的平台用户Id
	
	@Column(name="name", nullable=false)
	private String Name;						//开发者名称
	
	@Column(name="type", nullable=false)
	private DeveloperType Type;					//开发者类型，个人用户、企业用户
	
	@Column(name="director_id", nullable = false)
	private String director_id;					//开发者账号管理员的Id，对应账号表
		
	@Column(name="manager_name",nullable = false) //管理员名字，如果是个人账号就为开发者名称
	private String managerName;
	
	@Column(name="manager_email",nullable = false)
	private String managerEmail;
	
	@Column(name="manager_mobile",nullable = false)
	private String managerMobile;
	
	@CreatedDate
	private Date createTime;					//注册时间
}
