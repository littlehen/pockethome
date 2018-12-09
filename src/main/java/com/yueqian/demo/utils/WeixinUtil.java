package com.yueqian.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import javax.net.ssl.HttpsURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.yueqian.demo.dao.weichattemplate.WeichatAccessTokenRepository;
import com.yueqian.demo.model.weichattemplate.WeichatAccessTokenEntity;

/**
 * 
 * @author wujijin
 *
 */
@Component
public class WeixinUtil {
	
    private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

    @Autowired
    WeichatAccessTokenRepository weichatAccessTokenRepository;

    @Value("${weichat.appid}")
    String appid;

    @Value("${weichat.appsecret}")
    String appsecret;

    @Value("${weichat.accesstoken}")
    String accesstoken;
	
	/**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
	public static JSONObject httpRequest(String requestUrl,String requestMethod, String outputStr) {
		JSONObject jsonObject = null ;
		StringBuffer buffer = new StringBuffer();
		try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
 
            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();
 
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            
            String str = null ;
            while((str = bufferedReader.readLine()) != null) {
            	buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
		}
	public static String sendPostUrl(String url, String param){
        PrintWriter out = null;
        BufferedReader in = null;
        JSONObject jsonObject = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流（设置请求编码为UTF-8）
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 获取请求返回数据（设置返回数据编码为UTF-8）
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
//            jsonObject = JSONObject.fromObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        
        return result;
    }    
   @Scheduled(initialDelay = 500, fixedDelay = 100*60*1000)
    public void get_access_token() {
        try {
            WeichatAccessTokenEntity weichatAccessToken = new WeichatAccessTokenEntity();
            String requestUrl = accesstoken;
            requestUrl = requestUrl.replace("APPID",appid).replace("APPSECRET",appsecret);
            JSONObject jsonObject = httpRequest(requestUrl,"GET",null);
            if(jsonObject.getString("access_token")!=null){
                System.out.println(jsonObject.getString("access_token"));
                System.out.println(jsonObject.getInt("expires_in"));
                weichatAccessToken = weichatAccessTokenRepository.findByAppid(appid);
                if(weichatAccessToken == null){
                    weichatAccessToken.setAccessToken(jsonObject.getString("access_token"));
                    weichatAccessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                    weichatAccessToken.setAppid(appid);
                }else{
                    weichatAccessToken.setAccessToken(jsonObject.getString("access_token"));
                }
                weichatAccessTokenRepository.save(weichatAccessToken);
            }
            else{
                log.error("定时获取token失败 errcode:{} errmsg:{}",jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        catch (Exception e){
            log.info("更新access_token的过程当中发生了异常，异常的信息是"+e.getMessage());
        }
    }
}

