package pw.cdmi.open.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 岗位信息实体
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@NamedQueries({ @NamedQuery(name = "Position.findAll", query = "from Position p"),
        @NamedQuery(name = "Position.findAllByCompany", query = "from Position p where p.companyId=?"),
        @NamedQuery(name = "Position.findByName", query = "from Position p where p.name like ? and p.companyId=?"), })
@Data
@Entity
@Table(name = "ept_position")
public class Position {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;							// 岗位编号

    @Column(length = 20, nullable = false)
    private String name;					// 岗位名称

    private String description;				// 岗位描述

    private String companyId; // 总公司ID

}
