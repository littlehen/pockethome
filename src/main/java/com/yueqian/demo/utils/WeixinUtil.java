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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.pdd.pop.ext.apache.http.client.ClientProtocolException;
import com.yueqian.demo.dao.weichattemplate.AccessTokenRepository;
import com.yueqian.demo.dao.weichattemplate.WeichatAccessTokenRepository;
import com.yueqian.demo.model.menu.Button;
import com.yueqian.demo.model.menu.ClickButton;
import com.yueqian.demo.model.menu.Menu;
import com.yueqian.demo.model.menu.ViewButton;
import com.yueqian.demo.model.weichattemplate.AccessToken;
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

    private static String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
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
	
	//获取accessToken
//	public AccessToken getAccessToken()  {
//		System.out.println("123");
//	       Optional<AccessToken> accessToken = accessTokenRepository.findById(1);
//	       System.out.println("1233");
//	       AccessToken myaccessToken = null;
//	       if(accessToken.isPresent()) {
//	    	   myaccessToken = accessToken.get();
//	       }
//	       System.out.println("--------------"+myaccessToken);
//	       //System.out.println("CreateTime: "+accessToken.get().getCreatedate().getTime()+"NOW: "+new Date().getTime() +"\n"+ Long.parseLong(accessToken.get().getExpiresin()));
//	       if(myaccessToken == null){
//	    	   //System.out.println("CreateTime: "+accessToken.get().getCreatedate().getTime()+"NOW: "+new Date().getTime() +"\n"+ Long.parseLong(accessToken.get().getExpiresin()));
//	           String url = accesstoken.replace("APPID", appid).replace("APPSECRET", appsecret);
//	           JSONObject jsonObject = httpRequest(url, "GET", null);
//	           System.out.println(jsonObject.toString());
//	           if(jsonObject.getString("errcode") == null){
//	        	   myaccessToken = new AccessToken();
//	        	   myaccessToken.setId(1);
//	        	   myaccessToken.setAccessToken(jsonObject.getString("access_token"));
//	        	   myaccessToken.setExpiresin(jsonObject.getString("expires_in"));
//	        	   myaccessToken.setCreatedate(new Date());
//	           }
//	           accessTokenRepository.save(myaccessToken);
//	       }
//	       else if(myaccessToken.getCreatedate().getTime() + (Long.parseLong(myaccessToken.getExpiresin())-200L) *1000 < new Date().getTime()) {
//	    	   String url = accesstoken.replace("APPID", appid).replace("APPSECRET", appsecret);
//	           JSONObject jsonObject = httpRequest(url, "GET", null);
//	           System.out.println(jsonObject.toString());
//	           if(jsonObject.getString("errorcode") == null){
//	        	   myaccessToken = new AccessToken();
//	        	   myaccessToken.setAccessToken(jsonObject.getString("access_token"));
//	        	   myaccessToken.setExpiresin(jsonObject.getString("expires_in"));
//	        	   myaccessToken.setCreatedate(new Date());
//	           }
//	           accessTokenRepository.save(myaccessToken);
//	       }
//	       return myaccessToken;
//	 }

	/**
	 * 检测微信头像是否可用
	 * @param url
	 * @return
	 */
	public static boolean checkWeChatHeadImg(String url) throws Exception{
	    HttpURLConnection http = null;
	    try {
	        URL urlObj = new URL(url);
	        if(url.startsWith("https://")){
	            http = (HttpsURLConnection) urlObj.openConnection();
	        }else{
	            http = (HttpURLConnection) urlObj.openConnection();
	        }
	        http.setRequestMethod("GET");
	        http.setConnectTimeout(30 * 1000);
	        http.setReadTimeout(30 * 1000);
	        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
	        http.setDefaultUseCaches(false);

	        http.connect();

	        Map<String, List<String>> resHeaders = http.getHeaderFields();
	        for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
	            String name = entry.getKey();
	            if ("X-ErrNo".equalsIgnoreCase(name)) {
	                return false;
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (http != null) {
	            http.disconnect();
	        }
	    }
	    return true;
	}
//   @Scheduled(initialDelay = 500, fixedDelay = 100*60*1000)
//    public void get_access_token() {
//        try {
//            WeichatAccessTokenEntity weichatAccessToken = new WeichatAccessTokenEntity();
//            String requestUrl = accesstoken;
//            requestUrl = requestUrl.replace("APPID",appid).replace("APPSECRET",appsecret);
//            JSONObject jsonObject = httpRequest(requestUrl,"GET",null);
//            if(jsonObject.getString("access_token")!=null){
//                System.out.println(jsonObject.getString("access_token"));
//                System.out.println(jsonObject.getInt("expires_in"));
//                weichatAccessToken = weichatAccessTokenRepository.findByAppid(appid);
//                if(weichatAccessToken == null){
//                    weichatAccessToken.setAccessToken(jsonObject.getString("access_token"));
//                    weichatAccessToken.setExpiresIn(jsonObject.getInt("expires_in"));
//                    weichatAccessToken.setAppid(appid);
//                }else{
//                    weichatAccessToken.setAccessToken(jsonObject.getString("access_token"));
//                }
//                weichatAccessTokenRepository.save(weichatAccessToken);
//            }
//            else{
//                log.error("定时获取token失败 errcode:{} errmsg:{}",jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//            }
//        }
//        catch (Exception e){
//            log.info("更新access_token的过程当中发生了异常，异常的信息是"+e.getMessage());
//        }
//    }
	
	public static Menu initMenu(){
		Menu menu = new Menu();
		ViewButton button11 = new ViewButton();
		button11.setName("进入商城");
		button11.setType("view");
		button11.setUrl("http://www.pockethome.com.cn/index.html");

		ClickButton button21 = new ClickButton();
		button21.setName("我的二维码");
		button21.setType("click");
		button21.setKey("21");

		ViewButton button31 = new ViewButton();
		button31.setName("我的");
		button31.setType("view");
		button31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2ec2c331d6af829a&redirect_uri=http://www.pockethome.com.cn/pockethome/spring/WeiChat/bind.do&response_type=code&scope=snsapi_base&state=123#wechat_redirect");

		ClickButton button32 = new ClickButton();
		button32.setName("在线客服");
		button32.setType("click");
		button32.setKey("32");
		
		ClickButton button33 = new ClickButton();
		button33.setName("优惠券");
		button33.setType("click");
		button33.setKey("33");
		
		ClickButton button34 = new ClickButton();
		button34.setName("公司简介");
		button34.setType("click");
		button34.setKey("34");
		
		ViewButton button35 = new ViewButton();
		button35.setName("订单查询");
		button35.setType("view");
		button35.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2ec2c331d6af829a&redirect_uri=http://www.pockethome.com.cn/pockethome/spring/WeiChat/getorderlist&response_type=code&scope=snsapi_base&state=123#wechat_redirect");

		Button button = new Button();
		button.setName("服务中心");
		button.setSub_button(new Button[]{button31,button32,button33,button34,button35});

		menu.setButton(new Button[]{button11,button21,button});
		return menu;
		}
	
	public static int createMenu(String token,String menu) throws ClientProtocolException, IOException {
	
		int result = 0;
	
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
	
		JSONObject jsonObject = httpRequest(url,"POST",menu);
	
		if(jsonObject != null){
	
		result = jsonObject.getInt("errcode");
	
		}
	
		return result;
	
		}
	
	
}

