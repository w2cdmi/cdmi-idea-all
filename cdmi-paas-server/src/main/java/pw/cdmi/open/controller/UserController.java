package pw.cdmi.open.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;
import pw.cdmi.paas.account.model.ProtectAQ;
import pw.cdmi.paas.account.model.entities.UserAccount;
import pw.cdmi.paas.account.service.UserService;
import pw.cdmi.utils.VerificationCodeUtils;

/************************************************************
 * 控制类，处理用户或账号信息的请求操作
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-19
 ************************************************************/
//@Controller
//@RequestMapping(value = "/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

//    @Autowired
//    private BusinessOrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/account/userName/{userName}/password/{password}/employeeId/{employeeId}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createUserAccount(@PathVariable(value = "userName") String userName,
        @PathVariable(value = "password") String password, @PathVariable(value = "employeeId") String employeeId) {
        Map<String, String> map = new HashMap<String, String>();
        try {
//            userService.createUserAccountForEmployee(userName, password, employeeId);
            map.put("message", "success");
            map.put("userName", userName);
        } catch (Exception e) {
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "/account/id/{id}/password/{password}", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, String> resetPassword(@PathVariable(value = "id") String id,
        @PathVariable(value = "password") String password) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            UserAccount ua = userService.getUserAccountById(id);
            if (ua != null) {
                // 对用户的密码进行加密处理
                BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
                String hashPassword = bcryptEncoder.encode(password);
                ua.setPassword(hashPassword);
                userService.updateUserAccount(ua);
                map.put("message", "success");
            } else {
                map.put("message", "没有找到对应的账号！");
            }
        } catch (Exception e) {
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "/account/updatePassword", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, String> updatePassword(UserAccount userAccount, String oldPassword) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            UserAccount ua = userService.getUserAccountById(userAccount.getId());
            // 对用户的密码进行加密处理
            BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
            String pwd = bcryptEncoder.encode(oldPassword);
            if (ua != null && pwd.equals(ua.getPassword())) {
                ua.setPassword(bcryptEncoder.encode(userAccount.getPassword()));
                userService.updateUserAccount(ua);
                map.put("message", "success");
            }
            map.put("message", "旧密码不正确！");
        } catch (Exception e) {
            map.put("message", "failure");
        }
        return map;
    }

    @RequestMapping(value = "/account/employeeId/{employeeId}", method = RequestMethod.GET)
    @ResponseBody
    public UserAccount getUserAccountByUserId(@PathVariable(value = "employeeId") String employeeId) {
        Employee employee = employeeService.getSingleEmployeeById(employeeId);
        String accountId = employee.getAccountId();
        if (accountId != null) {
            UserAccount userAccount = userService.getUserAccountById(employee.getAccountId());
            return userAccount;
        } else {
            return new UserAccount();
        }
    }

    @RequestMapping(value = "/account/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> login(String userName, String password, String checkCode, HttpSession session) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            UserAccount ua;
            String oldCode = (String) session.getAttribute("checkCode");
            if (oldCode.equalsIgnoreCase(checkCode)) {
                BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
                String pwd = bcryptEncoder.encode(password);
                ua = userService.getUserAccountByUserNameAndPassword(userName, pwd);
                if (ua != null) {
//                    String companyId = organizationService.getCurrentCompanyId();
                	String companyId = null;
                    Employee employee = employeeService.getEmployeeByPeopleId(companyId, ua.getPeopleId());
                    if (employee != null) {
                        session.setAttribute("user", employee);
                        map.put("message", "success");
                    } else {
                        map.put("message", "not user found!");
                    }
                } else {
                    map.put("message", "no account found!");
                }
            } else {
                map.put("message", "验证码错误！");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            map.put("message", "exception");
        }
        return map;
    }

    @RequestMapping(value = "/account/exit")
    @ResponseBody
    public Map<String, String> exit(HttpSession session) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            session.removeAttribute("user");
            map.put("message", "退出成功");
        } catch (Exception e) {
            map.put("message", "退出失败");
        }
        return map;
    }

    @RequestMapping(value = "/verification")
    public void setVerification(HttpSession session, HttpServletResponse response) {
        try {
            List<Object> list = VerificationCodeUtils.produceCode();
            session.setAttribute("checkCode", list.get(0));
            byte[] bytes = (byte[]) list.get(1);
            response.setContentType("image/jpeg");
            OutputStream stream = response.getOutputStream();
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("serial")
    @RequestMapping(value = "/protectAQ/userAccountId/{userAccountId}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createProtectAQ(@PathVariable(value = "userAccountId") long userAccountId,
        @RequestParam(value = "qOne")
        final String qOne, @RequestParam(value = "aOne")
        final String aOne, @RequestParam(value = "qTwo")
        final String qTwo, @RequestParam(value = "aTwo")
        final String aTwo, @RequestParam(value = "qThr")
        final String qThr, @RequestParam(value = "aThr")
        final String aThr) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            List<Map<String, String>> protectAQList = new ArrayList<Map<String, String>>() {
                {
                    add(new HashMap<String, String>() {
                        {
                            put("question", qOne);
                            put("answer", aOne);
                        }
                    });
                    add(new HashMap<String, String>() {
                        {
                            put("question", qTwo);
                            put("answer", aTwo);
                        }
                    });
                    add(new HashMap<String, String>() {
                        {
                            put("question", qThr);
                            put("answer", aThr);
                        }
                    });
                }
            };
            userService.createProtectAQs(userAccountId, protectAQList);
            map.put("message", "success");
        } catch (Exception e) {
            log.error("保存密保信息时发生异常！");
            map.put("message", "failure");
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "/protectAQ/userAccountId/{userAccountId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> findProtectAQ(@PathVariable("userAccountId") Long userAccountId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            ProtectAQ protectAQ = userService.findSingleProtectAQByUserAccountIdAndRandom(userAccountId);
            if (protectAQ != null) {
                map.put("id", protectAQ.getId());
                map.put("question", protectAQ.getQuestion());
                map.put("message", "success");
            } else {
                map.put("message", "未创建密保信息！请先设置密保！");
            }
        } catch (Exception e) {
            log.error("查找密保问题时发生异常！");
            map.put("message", "exception");
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "/protectAQ/id/{id}/answer/{answer}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> checkProtectAQ(@PathVariable(value = "id") Long id,
        @PathVariable(value = "answer") String answer) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            ProtectAQ protectAQ = userService.findProtectAQById(id);
            if (answer.equals(protectAQ.getAnswer())) {
                map.put("message", "success");
            } else {
                map.put("message", "failure");
            }
        } catch (Exception e) {
            log.error("密保信息校验异常！");
            map.put("message", "exception");
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "/selfInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getLoginUserInfo(HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
            UserAccount ua = userService.getUserAccountByUsername(userDetails.getUsername());
            // Employee employee = (Employee) session.getAttribute("user");
            // map = employeeService.getSingleEmployeeByPeopleId(companyId, peopleId)

        } catch (Exception e) {
            log.error("获取个人信息异常！");
            map.put("message", "failure");
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping(value = "/accountExists", method = RequestMethod.POST)
    @ResponseBody
    public boolean accountExists(String accountName) {
        UserAccount account = userService.getUserAccountByUsername(accountName);
        if (null != account) {
            return false;
        }

        return true;
    }

    @RequestMapping(value = "/emailExists", method = RequestMethod.POST)
    @ResponseBody
    public boolean emailExists(String email) {
        UserAccount userAccount = userService.getUserAccountByEmail(email);
        if (null != userAccount) {
            return false;
        }
        return true;

    }

    @RequestMapping(value = "/accountHaveIt", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> accountHaveIt(String employeeId) {
        Map<String, String> map = new HashMap<String, String>();
        Employee emp = employeeService.getSingleEmployeeById(employeeId);
        if (null != emp) {
            UserAccount account = userService.getUserAccountByIdAndStatus(emp.getAccountId());
            if (null != account) {
                map.put("message", "need");
            } else {
                map.put("message", "needNot");
            }
        }
        return map;
    }

}
