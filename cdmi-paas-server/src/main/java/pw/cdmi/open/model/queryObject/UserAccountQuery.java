package pw.cdmi.open.model.queryObject;

import lombok.Data;
import pw.cdmi.paas.account.model.UserStatus;

import org.apache.commons.lang.StringUtils;

@Data
public class UserAccountQuery {
	
	private String userName;
	
	private UserStatus status;

	public String createQuery() {
		StringBuilder hql = new StringBuilder("from UserAccount where 1=1");
		return addWhere(hql);
	}

	public String createPageCountQuery() {
		StringBuilder hql = new StringBuilder("select count(*) from UserAccount where 1=1");
		return addWhere(hql);
	}

	public String addWhere(StringBuilder hql) {
		if (StringUtils.isNotBlank(userName)) {
			hql.append(" and userName like '%" + userName + "%'");
		}
		if (status!=null) {
			hql.append(" and status = '" + status.toString() + "'");
		}
		return hql.toString();
	}
}
