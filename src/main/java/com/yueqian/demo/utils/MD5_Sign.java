package com.yueqian.demo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MD5_Sign {
	
		@Value("${pinduoduo.client_id}")
	    public  static   String client_id = "4b45df16ebb643bf9594e93e55b289da";
	 
		@Value("${pinduoduo.client_secret}")
	    public  static  String client_secret = "9c16f347d216669b85061c1ee6d407ac6b66b9eb";
	 
	 
	    public static String md5(String string) {
	        byte[] hash;
	        try {
	            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("UTF-8 is unsupported", e);
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("MessageDigest不支持MD5Util", e);
	        }
	        StringBuilder hex = new StringBuilder(hash.length * 2);
	        for (byte b : hash) {
	            if ((b & 0xFF) < 0x10) hex.append("0");
	            hex.append(Integer.toHexString(b & 0xFF));
	        }
	        return hex.toString();
	    }
	 
	    /**
	     * md5签名
	     *
	     * 按参数名称升序，将参数值进行连接 签名
	     * @param params
	     * @return
	     */
	    public static String sign( TreeMap<String, String> params) {
	        StringBuilder paramValues = new StringBuilder();
	        params.put("client_id", client_id);
	        for (Map.Entry<String, String> entry : params.entrySet()) {
	            paramValues.append(entry.getKey()+entry.getValue());
	        }
	 
	        /*  //拼多多 签名认证
	         *签名算法
	            POP目前支持的签名算法为：MD5(sign_method=md5)，签名大体过程如下：
	            1-所有参数进行按照首字母先后顺序排列
	            2-把排序后的结果按照参数名+参数值的方式拼接
	            3-拼装好的字符串首尾拼接client_secret进行md5加密后转大写，secret的值是拼多多开放平台后台分配的client_secret
	          *
	          * */
//	        System.out.println(client_secret+paramValues.toString()+client_secret);
	        String Md5Sign=md5(client_secret+paramValues.toString()+client_secret).toUpperCase();
//	        System.out.println(Md5Sign);
	        return Md5Sign;
	    }
	 
	 
	    /**
	     * 请求参数签名验证
	     *
	     * @param request
	     * @return true 验证通过 false 验证失败
	     * @throws Exception
	     */
	    public static boolean verifySign(HttpServletRequest request) throws Exception {
	        TreeMap<String, String> params = new TreeMap<String, String>();
	 
	        String signStr = request.getParameter("sign");
	        if(StringUtils.isBlank(signStr)){
	            throw new RuntimeException("There is no signature field in the request parameter!");
	        }
	 
	        Enumeration<String> enu = request.getParameterNames();
	        while (enu.hasMoreElements()) {
	            String paramName = enu.nextElement().trim();
	            if (!paramName.equals("sign")) {
	                params.put(paramName, URLDecoder.decode(request.getParameter(paramName), "UTF-8"));
	            }
	        }
	 
	        if (!sign(params).equals(signStr)) {
	            return false;
	        }
	        return true;
	    }
}
