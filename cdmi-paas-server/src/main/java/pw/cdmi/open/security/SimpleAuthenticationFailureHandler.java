package pw.cdmi.open.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 认证失败，则转到该类进行处理。
 * 
 * @author wuwei
 *
 */
public class SimpleAuthenticationFailureHandler implements
		AuthenticationFailureHandler {

	private static Logger logger = LoggerFactory
			.getLogger(SimpleAuthenticationFailureHandler.class);

	/**
	 * 对应配置文件中SimpleAuthenticationFailureHandler的targetUrl属性
	 */
	private String targetUrl;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		logger.info("认证：认证失败，输出失败信息");
		String ename = exception.getClass().getSimpleName();
		if ("BadCredentialsException".equals(ename)) {
			request.getSession().setAttribute("errorMsg", "用户名或密码错误，请重试");
		} else if ("DisabledException".equals(ename)) {
			request.getSession().setAttribute("errorMsg", "用户未激活，请联系管理员进行激活");
		}else if("CaptchaException".equals(ename)){
			request.getSession().setAttribute("errorMsg", "验证码输入错误");
		} else {
			request.getSession().setAttribute("errorMsg", "账户已过期");
		}
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	
}
