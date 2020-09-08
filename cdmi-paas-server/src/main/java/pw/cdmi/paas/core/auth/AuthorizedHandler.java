package pw.cdmi.paas.core.auth;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pw.cdmi.core.http.Headers;
import pw.cdmi.core.http.auth.Authorized;
import pw.cdmi.paas.auth.service.AuthTokenService;

@Aspect
@Component
public class AuthorizedHandler {
	
	@Autowired
    private AuthTokenService authTokenService;
	
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping() {
    }

    @Pointcut("execution(* pw.cdmi.paas.*.rs.*.*Resource.*(..))")
    public void methodPointCut() {
    }
    
    /**
     * 只有执行方法中带有Authorized注解的才会被执行 （先执行此方法）
     */
    @Before("requestMapping() && methodPointCut()&&@annotation(authorized)")
    public void doBefore(JoinPoint joinPoint, Authorized authorized) throws Exception {


        Class<?> type = joinPoint.getSignature().getDeclaringType();
        System.out.println("type:" + type);
        System.out.println("authorized:" + authorized);

        //获取当前http请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //TODO向请求中添加一个RequestId,这个要写入到日志中
        attributes.setAttribute(Headers.REQUEST_ID, "abdddceegee24332", 0);
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader(Headers.AUTHORIZATION);

        System.out.println("token1:" + token);
        
    }

    /**
     * 所有符合条件的请求都会被拦截 （再执行该方法）
     */
    @Before("requestMapping() && methodPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Exception {


        Class<?> type = joinPoint.getSignature().getDeclaringType();
        System.out.println("type2:" + type);

        //获取当前http请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //TODO向请求中添加一个RequestId,这个要写入到日志中
        attributes.setAttribute(Headers.REQUEST_ID, "abdddceegee24332", 0);
        HttpServletRequest request = attributes.getRequest();
        
    }
}
