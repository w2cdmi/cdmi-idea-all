package pw.cdmi.paas.cmdb.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/************************************************************
 * 实体表 ,以Key、Value的形式记录应用的部署配置信息.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_system_configuration")
public class SystemConfiguration {
	@Id
	private String propkey;
	private String propvalue;
	private boolean readonly;
}
