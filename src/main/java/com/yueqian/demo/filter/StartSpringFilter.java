package com.yueqian.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@Order(1)
@WebFilter(filterName = "startSpringFilter", urlPatterns = "/*")
public class StartSpringFilter implements Filter{
	
	private static final Logger log = LoggerFactory.getLogger(StartSpringFilter.class);
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse responce = (HttpServletResponse) arg1;
        String uri = request.getRequestURI();
        if(uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".map") || uri.endsWith(".json") || uri.endsWith(".woff") || uri.endsWith(".jpg") || uri.endsWith(".png")) {
        	arg2.doFilter(arg0, arg1);
        	return;
        }
        
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        arg2.doFilter(arg0, arg1);
        log.info("request ip is {},uri is {} ",ip,uri);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
