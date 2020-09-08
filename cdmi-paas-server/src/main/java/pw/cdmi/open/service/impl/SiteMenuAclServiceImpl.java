package pw.cdmi.open.service.impl;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import pw.cdmi.open.service.SiteMenuAclService;
import pw.cdmi.paas.app.model.entities.SiteMenuAcl;
import pw.cdmi.paas.app.repositories.SiteMenuAclRepository;

/************************************************
 * 对基于URL的权限Menu的访问控制列表类的处理
 * @author WUWEI
 * @date 2015年4月30日
 * @version
 ************************************************/
public class SiteMenuAclServiceImpl implements SiteMenuAclService {
    @Autowired
    private SiteMenuAclRepository daoImpl;

    @Override
    public Iterable<SiteMenuAcl> findPrivilege() {
        Iterable<SiteMenuAcl> prilist = daoImpl.findAll();
        return prilist;
    }

    @Override
    public SiteMenuAcl getPrivilege(String id) {
        return daoImpl.findById(id).get();
    }

    @Override
    @Transactional
    public boolean savePrivilege(SiteMenuAcl privilege) {
        try {
            daoImpl.save(privilege);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean updatePrivilege(SiteMenuAcl privilege) {
        try {
            daoImpl.save(privilege);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deletePrivilege(String privilegeId) {
        try {
            long count = daoImpl.countByRoleId(privilegeId);
            //判断权限是否被使用
            if (count > 0) {

            } else {
                Iterable<SiteMenuAcl> plist = daoImpl.findByRoleId(privilegeId);
                for (SiteMenuAcl privilege : plist) {
                    daoImpl.delete(privilege);
                }
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
