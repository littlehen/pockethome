package com.yueqian.demo;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class PockethomeApplication {
	
	@Value("${druid.loginUsername}")
	String loginUsername;
	@Value("${druid.loginPassword}")
	String loginPassword;

	@Bean  
    @ConfigurationProperties(prefix = "spring.datasource")  
    public DataSource druidDataSource() {  
        DruidDataSource druidDataSource = new DruidDataSource();  
        return druidDataSource;  
    } 
	
	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean srbean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
		Map<String,String> param = new HashMap<String,String>();
		param.put("loginUsername", loginUsername);
		param.put("loginPassword", loginPassword);
		param.put("allow", "");
		srbean.setInitParameters(param);
		return srbean;
	}
	
	@Bean
	public FilterRegistrationBean webStatFilter() {
		FilterRegistrationBean frbean = new FilterRegistrationBean();
		frbean.setFilter(new WebStatFilter());
		frbean.addUrlPatterns("/*");
		Map<String,String> param = new HashMap<String,String>();
		param.put("exclusions","*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,*.woff,/druid/*");
		frbean.setInitParameters(param);
		return frbean;
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PockethomeApplication.class, args);
	}
}
