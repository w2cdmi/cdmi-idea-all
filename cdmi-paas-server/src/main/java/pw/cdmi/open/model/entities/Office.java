package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * **********************************************************
 * 办事处基础数据，记录办事处信息
 * 
 * @author liujh
 * @version iSoc Service Platform, 2015-6-30
 ***********************************************************
 */
@Data
@Entity
@Table(name = "ept_office")
public class Office {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;					// 办事处信息编号

    @Column(nullable = false, unique = true)
    private String openId;

    private String name;			// 办事处名称

    private String email;			// 办事处邮箱

    private String telephone;		// 办事处电话

    private String companyId;			// 对应公司

    private String areaId;			// 所属片区

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundDate;			// 成立时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;		// 信息录入时间

    private String supervisorId;	// 办事处主管，关联对应雇员信息编号

    private String description;		// 办事处简介
}
