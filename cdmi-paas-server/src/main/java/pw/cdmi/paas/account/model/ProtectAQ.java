package pw.cdmi.paas.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

/************************************************************
 * 基础数据，用于存放用户密保问题和密保答案
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-6-16
 ************************************************************/
@Data
@Entity
@Table(name = "p_protect_aq")
@NamedQueries({
	@NamedQuery(name = "ProtectAQ.findByAccount", query = "from ProtectAQ where userAccountId = ?1 ")
})
public class ProtectAQ {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;					// 密保信息编号
	@Column(length = 200, nullable = false)
	private String question;			//密保问题
	@Column(length = 200, nullable = false)
	private String answer;				//密保答案
	
	private Long userAccountId;			//对应账号信息编号

}
