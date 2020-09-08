package pw.cdmi.wechat.open3rd.extapp.rs.v1;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.wechat.open3rd.extapp.model.AuditPages;
import pw.cdmi.wechat.open3rd.extapp.model.GrayReleasePlan;
import pw.cdmi.wechat.open3rd.extapp.model.QRCodeRole;
import pw.cdmi.wechat.open3rd.extapp.model.QRCodeRoleResponse;
import pw.cdmi.wechat.open3rd.extapp.model.entities.ExtApp;

@RestController
@RequestMapping("/wechat/open3rd/extapp/v1")
public class ExtAppResource {
	
	//为授权的小程序帐号上传小程序代码
	//https://api.weixin.qq.com/wxa/commit?access_token=TOKEN
	@PostMapping(value = "/program")
	public void uploadProgram(@RequestBody ExtApp extApp) {
		
	}
	
	//获取体验小程序的体验二维码
	//https://api.weixin.qq.com/wxa/get_qrcode?access_token=TOKEN&path=page%2Findex%3Faction%3D1
	@GetMapping(value = "/qrcode")
	public void getQRCode(@RequestParam("path") String path) {
		
	}
	
	//获取授权小程序帐号的可选类目
	//https://api.weixin.qq.com/wxa/get_category?access_token=TOKEN
	@GetMapping(value = "/categories")
	public void getCategory() {
		
	}
	
	//获取小程序的第三方提交代码的页面配置（仅供第三方开发者代小程序调用）
	//https://api.weixin.qq.com/wxa/get_page?access_token=TOKEN
	@GetMapping(value = "/pages")
	public void getPages() {
		
	}
	
	//将第三方提交的代码包提交审核（仅供第三方开发者代小程序调用）
	//https://api.weixin.qq.com/wxa/submit_audit?access_token=TOKEN
	@PostMapping(value = "/audit")
	public String submitAudit(@RequestBody AuditPages page_list) {
		String auditid = "1234567";
		return auditid;
	}
	
	//查询某个指定版本的审核状态（仅供第三方代小程序调用）
	//https://api.weixin.qq.com/wxa/get_auditstatus?access_token=TOKEN
	@PostMapping(value = "/audit/status")
	public void getAuditStatus(@RequestParam("auditid") String auditid) {
		
	}
	
	//查询最新一次提交的审核状态（仅供第三方代小程序调用）
	//https://api.weixin.qq.com/wxa/get_latest_auditstatus?access_token=TOKEN
	@GetMapping(value = "/audit/latest")
	public void getLatestAuditStatus() {
		
	}
	
	
	//发布已通过审核的小程序（仅供第三方代小程序调用）
	//https://api.weixin.qq.com/wxa/release?access_token=TOKEN
	@PostMapping(value = "/program/release")
	public void release() {
		
	}
	
	//修改小程序线上代码的可见状态（仅供第三方代小程序调用）
	//https://api.weixin.qq.com/wxa/change_visitstatus?access_token=TOKEN
	@PostMapping(value = "/visitstatus")
	public void changeVisitStatus(@RequestParam("action") String action) {
		
	}
	
	//小程序版本回退（仅供第三方代小程序调用）
	//https://api.weixin.qq.com/wxa/revertcoderelease?access_token=TOKEN
	@GetMapping(value = "/program")
	public void revertCodeRelease() {
		
	}
	
	//查询当前设置的最低基础库版本及各版本用户占比 （仅供第三方代小程序调用)
	//https://api.weixin.qq.com/cgi-bin/wxopen/getweappsupportversion?access_token=TOKEN
	@GetMapping(value = "/supportversion")
	public void getWeappSupportVersion() {
		
	}
	
	//设置最低基础库版本（仅供第三方代小程序调用）
	//https://api.weixin.qq.com/cgi-bin/wxopen/setweappsupportversion?access_token=TOKEN
	@PostMapping(value = "/supportversion")
	public void setWeappSupportVersion(@RequestParam("version") String version) {
		
	}
	
	//设置小程序“扫普通链接二维码打开小程序”能力
	//1.增加或修改二维码规则
	//https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpadd?access_token=TOKEN
	@PostMapping(value = "/qrcode/role")
	public void addQRCodeRole(@RequestBody QRCodeRole role) {
		
	}
	
	//设置小程序“扫普通链接二维码打开小程序”能力
	//2.获取已设置的二维码规则
	//https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpget?access_token=TOKEN
	@GetMapping(value = "/qrcode/role")
	public QRCodeRoleResponse getQRCodeRole() {
		return null;
	}
	
	//设置小程序“扫普通链接二维码打开小程序”能力
	//3.获取校验文件名称及内容
	//https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpdownload?access_token=TOKEN
	@GetMapping(value = "/verifyfile")
	public void downloadVerifyFile() {

	}
	
	//设置小程序“扫普通链接二维码打开小程序”能力
	//4.删除已设置的二维码规则
	//https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumpdelete?access_token=TOKEN
	@DeleteMapping(value = "/qrcode/role")
	public void deleteQRCodeRole(@RequestParam("prefix") String prefix) {

	}
	
	//设置小程序“扫普通链接二维码打开小程序”能力
	//5.发布已设置的二维码规则
	//https://api.weixin.qq.com/cgi-bin/wxopen/qrcodejumppublish?access_token=TOKEN
	@PostMapping(value = "/qrcode/role/published")
	public void publishQRCodeRole(@RequestParam("prefix") String prefix) {

	}
	
	//小程序审核撤回
	//https://api.weixin.qq.com/wxa/undocodeaudit?access_token=TOKEN
	@GetMapping(value = "/audit/undo")
	public void undoCodeAudit() {

	}
	
	//小程序分阶段发布
	//1.分阶段发布接口
	//https://api.weixin.qq.com/wxa/grayrelease?access_token=TOKEN
	@PostMapping(value = "/program/gray")
	public void grayRelease(@RequestParam("gray_percentage") String gray_percentage) {

	}
	
	//小程序分阶段发布
	//2.取消分阶段发布
	//https://api.weixin.qq.com/wxa/revertgrayrelease?access_token=TOKEN
	@GetMapping(value = "/program/gray")
	public void revertGrayRelease() {

	}
	
	//小程序分阶段发布
	//3.查询当前分阶段发布详情
	//https://api.weixin.qq.com/wxa/getgrayreleaseplan?access_token=TOKEN
	@GetMapping(value = "/program/gray/plan")
	public GrayReleasePlan getGrayrReleasePlan() {
		return null;
	}
}
