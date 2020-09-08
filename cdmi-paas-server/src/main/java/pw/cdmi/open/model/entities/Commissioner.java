package pw.cdmi.open.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 专员信息实体，同一个公司的同一个员工在同一个岗位上只有一条记录
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@Data
@Entity
@Table(name = "ept_commissioner")
public class Commissioner {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;					// 专员信息编号

    @Column(length = 50, nullable = false)
    private String name;				// 专员岗位名称，对应应用自定义的专员岗位名称

    private String description;			// 专员岗位描述

    private String companyId;			// 企业Id
     

}
