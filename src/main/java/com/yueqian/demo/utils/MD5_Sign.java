package com.yueqian.demo.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
	    
	    
	    private static final String SIGN_METHOD_MD5 = "md5";
	    private static final String SIGN_METHOD_HMAC = "hmac";
	    private static final String CHARSET_UTF8 = "utf-8";

	    
	    /**
	     * 对TOP请求进行签名。
	     */
	    public static String signTopRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
	        // 第一步：检查参数是否已经排序
	        String[] keys = params.keySet().toArray(new String[0]);
	        Arrays.sort(keys);
	 
	        // 第二步：把所有参数名和参数值串在一起
	        StringBuilder query = new StringBuilder();
	        if (SIGN_METHOD_MD5.equals(signMethod)) {
	            query.append(secret);
	        }
	        for (String key : keys) {
	            String value = params.get(key);
	            if (isNotEmpty(key) && isNotEmpty(value)) {
	                query.append(key).append(value);
	            }
	        }
	 
	        // 第三步：使用MD5/HMAC加密
	        byte[] bytes;
	        if (SIGN_METHOD_HMAC.equals(signMethod)) {
	            bytes = encryptHMAC(query.toString(), secret);
	        } else {
	            query.append(secret);
	            bytes = encryptMD5(query.toString());
	        }
	 
	        // 第四步：把二进制转化为大写的十六进制
	        return byte2hex(bytes);
	    }
	 
	    /**
	     * 对字节流进行HMAC_MD5摘要。
	     */
	    private static byte[] encryptHMAC(String data, String secret) throws IOException {
	        byte[] bytes = null;
	        try {
	            SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8), "HmacMD5");
	            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
	            mac.init(secretKey);
	            bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
	        } catch (GeneralSecurityException gse) {
	            throw new IOException(gse.toString());
	        }
	        return bytes;
	    }
	 
	    /**
	     * 对字符串采用UTF-8编码后，用MD5进行摘要。
	     */
	    private static byte[] encryptMD5(String data) throws IOException {
	        return encryptMD5(data.getBytes(CHARSET_UTF8));
	    }
	 
	    /**
	     * 对字节流进行MD5摘要。
	     */
	    private static byte[] encryptMD5(byte[] data) throws IOException {
	        byte[] bytes = null;
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            bytes = md.digest(data);
	        } catch (GeneralSecurityException gse) {
	            throw new IOException(gse.toString());
	        }
	        return bytes;
	    }
	 
	    /**
	     * 把字节流转换为十六进制表示方式。
	     */
	    private static String byte2hex(byte[] bytes) {
	        StringBuilder sign = new StringBuilder();
	        for (int i = 0; i < bytes.length; i++) {
	            String hex = Integer.toHexString(bytes[i] & 0xFF);
	            if (hex.length() == 1) {
	                sign.append("0");
	            }
	            sign.append(hex.toUpperCase());
	        }
	        return sign.toString();
	    }
	    
	    private static boolean isNotEmpty(String value) {
	        int strLen;
	        if (value == null || (strLen = value.length()) == 0) {
	            return false;
	        }
	        for (int i = 0; i < strLen; i++) {
	            if ((Character.isWhitespace(value.charAt(i)) == false)) {
	                return true;
	            }
	        }
	        return false;
	    }


}
