package pw.cdmi.open.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体表 ,记录应用的邮箱配置信息.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_mail_configuration")
public class MailConfiguration {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;

    private Boolean stmpAuth;

    private String host;

    private String password;

    private String protocol;

    private Long timeout;

    private String username;

    private Integer port;

    private String appId;
}
