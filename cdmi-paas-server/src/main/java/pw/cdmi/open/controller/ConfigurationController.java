//package pw.cdmi.open.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import pw.cdmi.core.http.exception.AWSClientException;
//import pw.cdmi.error.ClientReason;
//import pw.cdmi.error.GlobalClientError;
//import pw.cdmi.open.model.entities.MMSConfiguration;
//import pw.cdmi.open.model.entities.MailConfiguration;
//import pw.cdmi.open.service.ConfigurationService;
//
//@Controller
//@RequestMapping("/configuration")
//public class ConfigurationController {
//
//    @Autowired
//    private ConfigurationService configurationService;
//
//    /**
//     * 
//     * 配置应用的邮件服务器信息
//     * 
//     * @param configuration
//     *            邮件服务器的配置信息
//     * @return
//     */
//    @RequestMapping(value = "/mail", method = RequestMethod.POST)
//    @ResponseBody
//    public void configMail(MailConfiguration configuration) {
//        if (StringUtils.isEmpty(configuration.getPassword()) || StringUtils.isEmpty(configuration.getHost())) {
//            throw new AWSClientException(GlobalClientError.IncompleteBody, ClientReason.IncompleteBody);
//        }
//        MailConfiguration mail = configurationService.getMailConfiguration();
//
//        if (mail == null) {
//            configurationService.createMailConfiguration(configuration);
//        } else {
//            if (mail.getId() != configuration.getId()) {
//                throw new AWSClientException(GlobalClientError.InvalidRequest, ClientReason.InvalidRequest);
//            }
//            configurationService.updateMailConfiguration(configuration);
//        }
//    }
//
//    /**
//     * 
//     * 配置应用的短信服务器信息
//     * 
//     * @param configuration
//     *            短信服务器的配置信息
//     * @return
//     */
//    @RequestMapping(value = "/mms", method = RequestMethod.POST)
//    @ResponseBody
//    public void configMMS(MMSConfiguration configuration) {
//        if (StringUtils.isEmpty(configuration.getPassword()) || StringUtils.isEmpty(configuration.getSignature())) {
//            throw new AWSClientException(GlobalClientError.IncompleteBody, ClientReason.IncompleteBody);
//        }
//        MMSConfiguration mms = configurationService.getMMSConfiguration();
//        if (mms == null) {
//            configurationService.createMMSConfiguration(configuration);
//        } else {
//            if (mms.getId() != configuration.getId()) {
//                throw new AWSClientException(GlobalClientError.InvalidRequest, ClientReason.InvalidRequest);
//            }
//            configurationService.updateMMSConfiguration(configuration);
//        }
//
//    }
//}
