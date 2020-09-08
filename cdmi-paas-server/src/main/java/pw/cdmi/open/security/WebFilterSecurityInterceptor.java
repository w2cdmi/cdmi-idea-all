package pw.cdmi.open.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * 针对用户请求进行合法用户身份校验的入口。
 * 用以检查用户是否已登录并获得可操作角色
 * 备注：当配置文件中存在<http pattern="**" security="none"/>时，
 * pattern对应页面将不会进入该Filter
 * 
 * @author wuwei
 *
 */
public class WebFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        String requestUrl = fi.getRequestUrl();

        // logger.info("校验：对访问用户进行身份校验，可在此判断用户是否拥有访问系统的角色");

        int indexOf = requestUrl.indexOf("?");
        if (indexOf > 0) {
            String inv = requestUrl.substring(indexOf + 1, requestUrl.length());
            if ("invalid".equals(inv)) {
                topWindow(request, response, "/login?disabled");
                return;
            }
        } else {
            Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if ("anonymousUser".equals(userDetails)) {// 无法获得用户信息，表示用户根本就没有登录
                topWindow(request, response, "/login");
                return;
            }
        }
        // 下面的这句话将会去执行FilterInvocationSecurityMetadataSource的getAttributes方法。
        invoke(fi);
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public Class<? extends Object> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        // 在执行doFilter之前，进行权限的检查，而具体的实现已经交给securityAccessDecisionManager
        InterceptorStatusToken token = super.beforeInvocation(fi);

        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }

    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {

    }

    private void topWindow(ServletRequest request, ServletResponse response, String targeturl) throws IOException {
        HttpServletRequest hreq = (HttpServletRequest) request;
        HttpServletResponse hres = (HttpServletResponse) response;
        String url = "<script  type='text/javascript'>window.top.location='" + hreq.getContextPath() + targeturl
                + "';</script>";
        hres.getWriter().print(url);

    }

}