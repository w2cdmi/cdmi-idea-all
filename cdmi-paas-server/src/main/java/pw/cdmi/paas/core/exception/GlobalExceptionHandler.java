package pw.cdmi.paas.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pw.cdmi.core.http.Headers;
import pw.cdmi.core.http.exception.AWSException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AWSException.class)
	public ResponseEntity<String> beanValidation(HttpServletRequest req, AWSException exception) {
		System.out.println("GlobalExceptionHandler:" + exception);
		System.out.println("path:" + req.getServletPath());
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String requestId = (String)attributes.getAttribute(Headers.REQUEST_ID, 0);
		System.out.println("requestId:" + requestId);
		return ResponseEntity.status(exception.getHttpStatus())
				.header(Headers.SERVER, "order-service") //微服务名称
				.header(Headers.REQUEST_ID, requestId) //请求编号
				.body(exception.getJsonText());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> beanValidation(HttpServletRequest req, Exception exception) {
		System.out.println("GlobalExceptionHandler:" + exception);
		System.out.println("path:" + req.getServletPath());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.header(Headers.SERVER, "order-service") //微服务名称
				.body("未捕获的系统缺陷,请联系开发商");
	}
}
