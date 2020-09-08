package pw.cdmi.open.security;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.web.method.HandlerMethod;

public class MethodSecurityInterceptorImpl extends AbstractSecurityInterceptor implements MethodInterceptor{

	private MethodSecurityMetadataSourceImpl securityMetadataSource;
	
	@Override
	public Class<?> getSecureObjectClass() {
		return MethodInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(
		MethodSecurityMetadataSourceImpl methodSecurityMetadataSourceImpl) {
	    this.securityMetadataSource = methodSecurityMetadataSourceImpl;
	  }

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//在执行原方法之前，进行权限的检查，而具体的实现已经交给securityAccessDecisionManager
		
		HandlerMethod handler = new HandlerMethod(invocation.getThis(), invocation.getMethod());
		Object[] arguments = invocation.getArguments();

		securityMetadataSource.setHandlerMethod(handler);
		securityMetadataSource.setArguments(arguments);
		
		logger.info("[方法验证]: 对执行的方法进行验证...");
		InterceptorStatusToken token = super.beforeInvocation(invocation);
		try {
			// 调用原方法，即调用CustomerService中的方法
	        Object result = invocation.proceed();
			return result;
		} finally {
			super.afterInvocation(token, null);
		}
	}
}
