package com.yueqian.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月29日    
 * @Description  进行sha1加密
 */
public class SecurityUtil {
	public static String sha1(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();
			//字节数组转换为十六进制数
			for (int i = 0; i < messageDigest.length; i++) {
	            String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
	            if (shaHex.length() < 2) {
	               hexString.append(0);
	            }
	            hexString.append(shaHex);
	        }
			return hexString.toString();
		}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
