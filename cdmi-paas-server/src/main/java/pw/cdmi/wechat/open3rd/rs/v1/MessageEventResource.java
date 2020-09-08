package pw.cdmi.wechat.open3rd.rs.v1;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/open3rd/v1")
public class MessageEventResource {

	/**
	 * 授权事件接收URL
	 * 用于接收取消授权通知、授权成功通知、授权更新通知，也用于接收ticket，
	 * ticket是验证平台方的重要凭据，服务方在获取component_access_token时
	 * 需要提供最新推送的ticket以供验证身份合法性。此ticket作为验证服务方
	 * 的重要凭据，请妥善保存
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/authevent")
	public void receiveAuthEvent(@RequestBody String request) {
		System.out.println("微信开放平台第三方平台接收到的消息：" + request);
	}
	
	/**
	 * 公众号消息与事件接收URL
	 * 该URL用于接收已授权公众号的消息和事件，消息内容、消息格式、签名方式、
	 * 加密方式与普通公众号接收的一致，唯一区别在于签名token和加密symmetric_key
	 * 使用的是服务方申请时所填写的信息。由于消息具体内容不会变更，故根据消息
	 * 内容里的ToUserName，服务方是可以区分出具体消息所属的公众号
	 */
	@PostMapping(value = "/{APPID}/msgevent")
	public void receiveMessageEvent(@PathVariable("APPID") String  appId, @RequestBody String request) {
		System.out.println("微信开放平台第三方平台接收到的消息：" + request);
	}
}
