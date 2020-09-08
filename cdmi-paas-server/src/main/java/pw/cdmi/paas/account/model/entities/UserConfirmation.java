package pw.cdmi.paas.account.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/****************************************************
 * 基础数据，用户信息验证时候临时保存修改数据。
 * 暂时不使用该对象
 * 
 * @author 伍伟
 * @version CDMI Service Platform, Aug 17, 2014
 ***************************************************/
@Entity
@Data
@Table(name = "c_confirmation_user")
public class UserConfirmation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// @Column(name = "userOpenId")
	// private String userOpenId;//用户表中的OpenId
	private Integer event;	// 对应的验证事件

	private String content;	// 临时保存待修改的内容

	private Date applicationTime; // 验证申请时间

	private Long deadline; // 失效时间，单位为分钟

	private String keyword; // 检索关键字，当有userOpenId时，作用同userOpenId

	private String appkey; // 对应的接入应用AK

}
