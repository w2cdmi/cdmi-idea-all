package pw.cdmi.open.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

/************************************************************
 * 职称信息实体
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@NamedQueries({ @NamedQuery(name = "PositionalTitle.findAll", query = "from PositionalTitle pt"),
        /*@NamedQuery(name = "PositionalTitle.findAllByCompany", query = "select pt from PositionalTitle pt,Position p where pt.positionId=p.id and p.companyId=?"),*/
        @NamedQuery(name = "PositionalTitle.findAllByCompany", query = "select pt from PositionalTitle pt where pt.companyId=?")
        // @NamedQuery(name = "PositionalTitle.findByKeyword", query =
        // "from PositionalTitle pt where pt.name like ?1 and pt.positionId = ?2"),
})
@Data
@Entity
@Table(name = "ept_positional_title")
public class PositionalTitle {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;					// 职称信息编号

    @Column(length = 20, nullable = false)
    private String name;			// 职称名称

    private String description;		// 职称描述

    private String positionId;		// 对应的岗位编号

    private String companyId;				// 对应当前企业id

    public String createSearchQuery() {
        StringBuffer queryString = new StringBuffer("select pt from PositionalTitle pt");

        String nameCondition = "";
        String positionIdCondition = "";

        if (positionId != null) {
            queryString.append(" ,Position p where ");
            positionIdCondition = "pt.positionId =" + positionId;
        }

        if (StringUtils.isNotBlank(name)) {
            nameCondition = "pt.name like '%" + name.trim() + "%'";
        }

        if (StringUtils.isNotBlank(positionIdCondition)) {
            queryString.append(positionIdCondition);
            if (StringUtils.isNotBlank(nameCondition)) {
                queryString.append(" and ").append(nameCondition);
            }
        } else if (StringUtils.isNotBlank(nameCondition)) {
            queryString.append(" where ").append(nameCondition);
        }

        return queryString.toString();
    }
}
