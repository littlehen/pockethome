package com.yueqian.demo.controller;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pdd.pop.ext.apache.http.client.ClientProtocolException;
import com.yueqian.demo.dao.user.UserInfoRepository;
import com.yueqian.demo.dao.weichattemplate.AccessTokenRepository;
import com.yueqian.demo.model.user.UserInfo;
import com.yueqian.demo.model.wechatmessage.TextMessage;
import com.yueqian.demo.model.weichattemplate.AccessToken;
import com.yueqian.demo.utils.ImgUtil;
import com.yueqian.demo.utils.SecurityUtil;
import com.yueqian.demo.utils.WeChatMessageUtil;
import com.yueqian.demo.utils.WeixinUtil;

import net.sf.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "${CommonValues.base_url}/WeiChat")
public class WeiChatController {

    @Value("${weichat.appid}")
    String appid;

    @Value("${weichat.appsecret}")
    String appsecret;

    @Value("${weichat.authaccesstoken}")
    String authaccesstoken;
    
    @Value("${weichat.getUserInfo}")
    String getUserInfo;
    

    @Value("${weichat.accesstoken}")
    String accesstoken;
    
    //上传临时素材url
    @Value("${weichat.UPLOAD_URL}")
    String UPLOAD_URL;
    
    //获取永久素材url
    @Value("${weichat.Get_ROREVER_MATERIAL_URL}")
    String Get_ROREVER_MATERIAL_URL;
    
    //获取公众号二维码media_id
    @Value("${weichat.qrcode_Media_Id}")
    String qrcode_Media_Id;
    
    
    
    private String TOKEN = "vippockethome";


    private Logger logger = LoggerFactory.getLogger(WeiChatController.class);

    
    @Autowired 
    UserInfoRepository userInfoRepository;
    
    @Autowired
    AccessTokenRepository accessTokenRepository;
    
    @RequestMapping(value="/bind.do")
    public void getweixininfo(@RequestParam(name="code",required=false)String code,@RequestParam(name="state")String
            state,HttpServletResponse res) throws IOException {
        logger.info("-----------------------------点击我的，收到请求，请求数据为："+code+"-----------------------"+state);
        String requestUrl = authaccesstoken;
        String get_access_token_url = requestUrl.replace("APPID",appid).replace("SECRET",appsecret);
        get_access_token_url = get_access_token_url.replace("CODE", code);
        JSONObject jsonObject = WeixinUtil.httpRequest(get_access_token_url, "GET", null);
        String openId = null;
        openId = jsonObject.getString("openid");
        UserInfo userIfo = new UserInfo();
        userIfo = userInfoRepository.findByOpenid(openId);
        if(userIfo != null) {
        	String headimgurl = userIfo.getHeadimgurl();
        	if(userIfo.getStatus().equals("0")) {
        		res.setCharacterEncoding("utf-8");
//    			String message = URLEncoder.encode("nojoin","utf-8");
        		try {
					if(!WeixinUtil.checkWeChatHeadImg(userIfo.getHeadimgurl())) {
						AccessToken access_token = getAccessToken();
					    String get_UserInfo_url = getUserInfo.replace("ACCESS_TOKEN", access_token.getAccessToken()).replace("OPENID", openId);
					    JSONObject jsonObject1 = WeixinUtil.httpRequest(get_UserInfo_url, "GET", null);
					    if(jsonObject1 != null) {
					    	headimgurl = jsonObject1.getString("headimgurl");
					    	userIfo.setHeadimgurl(headimgurl);
					    	userInfoRepository.save(userIfo);
					    }
					}
				} catch (Exception e) {
					logger.info("更新头像出错！！！！");
					e.printStackTrace();
				}
				res.sendRedirect("/pockethome/nojoin.html?openId="+openId+"&headimgurl="+headimgurl);	
				System.out.println("=========111=====");
			}		
        	else {
        		res.setCharacterEncoding("utf-8");
//    			String message = URLEncoder.encode("login","utf-8");
				res.sendRedirect("/pockethome/login.html?openId="+openId);
				System.out.println("=========222=====");
        	}
        }

    }
    
    
    
    //更新微信头像
//    public String updateweixinheadimgurl(String nopenId) {
//    	UserInfo userIfo = userInfoRepository.findByOpenid(nopenId);
//		AccessToken access_token = getAccessToken();
//        String get_UserInfo_url = getUserInfo.replace("ACCESS_TOKEN", access_token.getAccessToken()).replace("OPENID", openId);
//        JSONObject jsonObject1 = WeixinUtil.httpRequest(get_UserInfo_url, "GET", null);
//        if(jsonObject1 != null) {
//        	userIfo.setHeadimgurl(jsonObject1.getString("headimgurl"));
//        	userInfoRepository.save(userIfo);
//        	return jsonObject1.getString("headimgurl");
//        }
//    	
//    	return null;
//    }
  	
    //获取微信用户信息：昵称和头像
    @RequestMapping(value="/getweixininfo")
    public Map<String,Object> getweixininfo(String nopenId) {
    	UserInfo userIfo = userInfoRepository.findByOpenid(nopenId);
    	Map<String,Object> map = new HashMap<String,Object>();
    	try {
			if(!WeixinUtil.checkWeChatHeadImg(userIfo.getHeadimgurl())) {
				AccessToken access_token = getAccessToken();
	            String get_UserInfo_url = getUserInfo.replace("ACCESS_TOKEN", access_token.getAccessToken()).replace("OPENID", nopenId);
	            JSONObject jsonObject1 = WeixinUtil.httpRequest(get_UserInfo_url, "GET", null);
	            if(jsonObject1 != null) {
	            	userIfo.setHeadimgurl(jsonObject1.getString("headimgurl"));
	            }
	            map.put("nickname", userIfo.getNickname());
	            map.put("status", userIfo.getStatus());
				map.put("headimgurl", jsonObject1.getString("headimgurl"));
				userInfoRepository.save(userIfo);
			}else {
				map.put("nickname", userIfo.getNickname());
				map.put("status", userIfo.getStatus());
				map.put("headimgurl", userIfo.getHeadimgurl());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("判断检测微信头像是否可用出错！");
			e.printStackTrace();
		}
    	
    	return map;
    }

    
    /**
     * 
     * @author 吴佶津  
     * @date 2019年1月26日    
     * @Description 获取openId。公众号点击菜单商城入口 
     * @param code
     * @param state
     * @param res
     * @throws IOException
     */
    @RequestMapping(value="/getopenId.do")
    public void getopenId(@RequestParam(name="code",required=false)String code,@RequestParam(name="state")String
            state,HttpServletResponse res) throws IOException {
        logger.info("-----------------------------收到请求，请求数据为："+code+"-----------------------"+state);
        String requestUrl = authaccesstoken;
        String get_access_token_url = requestUrl.replace("APPID",appid).replace("SECRET",appsecret);
        get_access_token_url = get_access_token_url.replace("CODE", code);
        JSONObject jsonObject = WeixinUtil.httpRequest(get_access_token_url, "GET", null);
        String openId = null;
        openId = jsonObject.getString("openid");
        res.setCharacterEncoding("utf-8");
		res.sendRedirect("/pockethome/index.html?openId="+openId);	
    }

    /**
     * 
     * @author 吴佶津  
     * @date 2019年1月29日    
     * @Description  微信URL接入验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping(value="/weChat")
    public String validate(String signature,String timestamp,String nonce,String echostr){
    	logger.info("get:weChat 微信URL接入验证");
        //1. 将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = {timestamp,nonce,TOKEN};
        Arrays.sort(arr);
        //2. 将三个参数字符串拼接成一个字符串
        StringBuilder sb = new StringBuilder();
        for (String temp : arr) {
           sb.append(temp);
        }
        //3.进行sha1加密,开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if(SecurityUtil.sha1(sb.toString()).equals(signature)){
        	logger.info("签名校验成功,接入成功");
            //接入成功
            return echostr;
        }
        //接入失败
        logger.info("签名校验失败,接入失败");
        return null;
    }
    
    /**
     * 
     * @author 吴佶津  
     * @date 2019年1月30日    
     * @Description 微信消息接收，实现与用户交互
     * @param request
     * @param response
     * @return
     * @throws DocumentException 
     * @throws IOException 
     * @throws NoSuchProviderException 
     * @throws NoSuchAlgorithmException 
     * @throws KeyManagementException 
     */
    @PostMapping(value="/weChat")
    public void wechatMessageHandel(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException{
    	logger.info("post:weChat 微信消息接收");
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();;
		//将微信请求xml转为map格式，获取所需的参数
		Map<String,String> map = WeChatMessageUtil.parseXml(request);
		// 公众号帐号
		String toUserName = map.get("ToUserName");
		// 发送方帐号（open_id）
		String fromUserName = map.get("FromUserName");
		// 消息类型
		String msgType = map.get("MsgType");
		String content = map.get("Content");
		
		// 默认返回的文本消息内容
        String respContent = "";
        
        String respMessage = null; 
		
     // 文本消息  
        if (msgType.equals(WeChatMessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
            respContent = "欢迎加入口袋专享！！";  
            respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
        }  
        // 图片消息  
        else if (msgType.equals(WeChatMessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
            respContent = "欢迎加入口袋专享！";  
            respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
        }  
        // 地理位置消息  
        else if (msgType.equals(WeChatMessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
            respContent = "欢迎加入口袋专享！";  
            respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
        }  
        // 链接消息  
        else if (msgType.equals(WeChatMessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
            respContent = "欢迎加入口袋专享！";  
            respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
        }  
        // 音频消息  
        else if (msgType.equals(WeChatMessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
            respContent = "欢迎加入口袋专享！";  
            respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
        }  
        // 事件推送  
        else if (msgType.equals(WeChatMessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
            // 事件类型  
            String eventType = map.get("Event");  
            // 订阅  
            if (eventType.equals(WeChatMessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
                respContent = "欢迎加入口袋专享！";  
            	AccessToken access_token = getAccessToken();
                String get_UserInfo_url = getUserInfo.replace("ACCESS_TOKEN", access_token.getAccessToken()).replace("OPENID", fromUserName);
                JSONObject jsonObject1 = WeixinUtil.httpRequest(get_UserInfo_url, "GET", null);  
                logger.info("-----------------------------/weChat 事件类型-订阅: jsonObject1："+jsonObject1+"-----------------------");
                if(jsonObject1 != null) {
                	if(userInfoRepository.findByOpenid(fromUserName) == null) {
                		UserInfo userIfo1 = new UserInfo();
                    	userIfo1.setOpenid(fromUserName);
                    	userIfo1.setNickname(jsonObject1.getString("nickname"));
                    	userIfo1.setSex(jsonObject1.getString("sex"));
                    	userIfo1.setCity(jsonObject1.getString("city"));
                    	userIfo1.setProvince(jsonObject1.getString("province"));
                    	userIfo1.setHeadimgurl(jsonObject1.getString("headimgurl"));
                    	userIfo1.setStatus("0");
                    	userIfo1.setLastmouthmoney(0L);
                    	userIfo1.setLeijimoney(0L);
                    	userIfo1.setPersonalmoney(0);
                    	userIfo1.setTeammoney(0);
                    	userIfo1.setZhanghumoney(0L);
                    	userInfoRepository.save(userIfo1);
                	}
                	
                }
                respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
            }  
            // 取消订阅  
            else if (eventType.equals(WeChatMessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
                // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
            }  
            // 自定义菜单点击事件  
            else if (eventType.equals(WeChatMessageUtil.EVENT_TYPE_CLICK)) {  
                // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
                String eventKey = map.get("EventKey");  

                if (eventKey.equals("21")) {  
                	UserInfo userIfo = new UserInfo();
                	userIfo = userInfoRepository.findByOpenid(fromUserName);
                	if(userIfo.getYaoqingma() == null || userIfo.getYaoqingma().equals("")) {
                		 respContent = "您还没有成为店长，无法生成个性化海报，请先成为店长！";  
                		 respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
                	}else {
                		String mediaId = upload(getAccessToken().getAccessToken(),WeChatMessageUtil.REQ_MESSAGE_TYPE_IMAGE,fromUserName);
                		logger.info("mediaId:"+mediaId);
                		respMessage = WeChatMessageUtil.initImageMessage(mediaId, toUserName, fromUserName);
//                		String bigImg = "images/myshare.jpg";
//                        String content1 = "邀请码:666666";
//                        String outPath = "C:\\Users\\WuJiJin\\Desktop\\" + System.currentTimeMillis() + ".jpg";
//                        ImgUtil.bigImgAddSmallImgAndText(bigImg,content1, 145, 508, outPath);
                	}
                } else if (eventKey.equals("32")) {  
//获取永久素材                      	Get_ROREVER_MATERIAL_URL = Get_ROREVER_MATERIAL_URL.replace("ACCESS_TOKEN", getAccessToken().getAccessToken()).replace("MEDIA_ID", qrcode_Media_Id);
//                	JSONObject JSONResult = WeixinUtil.httpRequest(Get_ROREVER_MATERIAL_URL, "GET", null);
                	respMessage = WeChatMessageUtil.initImageMessage(qrcode_Media_Id, toUserName, fromUserName);
                }else {
                	respContent = "敬请期待！";  
                	respMessage = WeChatMessageUtil.initTextMessage(respContent, toUserName, fromUserName);
                }
            }  
        }  

       
        out.write(respMessage);
		out.close();
    }
    
    /**
     * 
     * @author 吴佶津  
     * @date 2019年1月30日    
     * @Description 微信公众号,点击 订单查询
     */
    @RequestMapping(value="/getorderlist")
    public void getorderlist(@RequestParam(name="code",required=false)String code,@RequestParam(name="state")String
            state,HttpServletResponse res) throws IOException {
    	logger.info("-----------------------------点击 我的，收到请求，请求数据为："+code+"-----------------------"+state);
        String requestUrl = authaccesstoken;
        String get_access_token_url = requestUrl.replace("APPID",appid).replace("SECRET",appsecret);
        get_access_token_url = get_access_token_url.replace("CODE", code);
        JSONObject jsonObject = WeixinUtil.httpRequest(get_access_token_url, "GET", null);
        String openId = null;
        openId = jsonObject.getString("openid");
        UserInfo userIfo = new UserInfo();
        userIfo = userInfoRepository.findByOpenid(openId);
        res.setCharacterEncoding("utf-8");
        if(userIfo != null && userIfo.getStatus().equals("2")) {
        	res.sendRedirect("/pockethome/orderlist.html");	
        }else if(userIfo != null && userIfo.getStatus().equals("1")) {
        	res.sendRedirect("/pockethome/yaoqingma.html");
        }else if(userIfo != null && userIfo.getStatus().equals("0")){
        	String headimgurl = userIfo.getHeadimgurl();
        	try {
				if(!WeixinUtil.checkWeChatHeadImg(userIfo.getHeadimgurl())) {
					AccessToken access_token = getAccessToken();
				    String get_UserInfo_url = getUserInfo.replace("ACCESS_TOKEN", access_token.getAccessToken()).replace("OPENID", openId);
				    JSONObject jsonObject1 = WeixinUtil.httpRequest(get_UserInfo_url, "GET", null);
				    if(jsonObject1 != null) {
				    	headimgurl = jsonObject1.getString("headimgurl");
				    	userIfo.setHeadimgurl(headimgurl);
				    	userInfoRepository.save(userIfo);
				    }
				}
			} catch (Exception e) {
				logger.info("更新头像出错！！！！");
				e.printStackTrace();
			}
			res.sendRedirect("/pockethome/nojoin.html?openId="+openId+"&headimgurl="+headimgurl);	
			System.out.println("=========微信公众号,点击 订单查询  getorderlist=====");
        }
    }
    
    /**
	 * 上传本地文件到微信获取mediaId
	 */
	
	public String upload(String accessToken,String type,String openId) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		logger.info("----临时素材开始上传临时素材到微信获取mediaId----");
		UserInfo userIfo = new UserInfo();
        userIfo = userInfoRepository.findByOpenid(openId);
		String bigImg = "classes/static/images/myshare.png";
        String content1 = "邀请码:"+userIfo.getYaoqingma();
        //String outPath = "C:\\Users\\WuJiJin\\Desktop\\" + System.currentTimeMillis() + ".jpg";
        BufferedImage bufferedImage = ImgUtil.bigImgAddSmallImgAndText(bigImg,content1, 145, 508);
 
		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
 
		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); 
 
		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
 
		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
 
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + bigImg + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
 
		byte[] head = sb.toString().getBytes("utf-8");
 
		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
 
		//文件正文部分
		//把文件已流文件的方式 推入到url中
		logger.info("----文件正文部分----");
		ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();;
		ImageIO.write(bufferedImage,"jpg",bufferOut);
		out.write(bufferOut.toByteArray());
 
		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线
 
		out.write(foot);
 
		out.flush();
		out.close();
 
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
 
		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}
	
	 @RequestMapping(value="/createMenu")
    public void createMenu() throws ClientProtocolException, IOException {
	 String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
	 int result = WeixinUtil.createMenu(getAccessToken().getAccessToken(),menu);
	 if(result==0){
		 logger.info("菜单创建成功！");
	}else{
		logger.info("菜单创建失败！"+result);
	}
	 }
	 
	//获取accessToken
	  	public AccessToken getAccessToken()  {
	  	       Optional<AccessToken> accessToken = accessTokenRepository.findById(1);
	  	       AccessToken myaccessToken = null;
	  	       if(accessToken.isPresent()) {
	  	    	   myaccessToken = accessToken.get();
	  	       }
	  	       System.out.println("--------------"+myaccessToken);
	  	       //System.out.println("CreateTime: "+accessToken.get().getCreatedate().getTime()+"NOW: "+new Date().getTime() +"\n"+ Long.parseLong(accessToken.get().getExpiresin()));
	  	       if(myaccessToken == null){
	  	    	   //System.out.println("CreateTime: "+accessToken.get().getCreatedate().getTime()+"NOW: "+new Date().getTime() +"\n"+ Long.parseLong(accessToken.get().getExpiresin()));
	  	           String url = accesstoken.replace("APPID", appid).replace("APPSECRET", appsecret);
	  	           JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
	  	           System.out.println(jsonObject.toString());
	  	           if(jsonObject != null){
	  	        	   myaccessToken = new AccessToken();
	  	        	   myaccessToken.setId(1);
	  	        	   myaccessToken.setAccessToken(jsonObject.getString("access_token"));
	  	        	   myaccessToken.setExpiresin(jsonObject.getString("expires_in"));
	  	        	   myaccessToken.setCreatedate(new Date());
	  	           }
	  	           accessTokenRepository.save(myaccessToken);
	  	       }
	  	       else if(myaccessToken.getCreatedate().getTime() + (Long.parseLong(myaccessToken.getExpiresin())-200L) *1000 < new Date().getTime()) {
	  	    	   String url = accesstoken.replace("APPID", appid).replace("APPSECRET", appsecret);
	  	           JSONObject jsonObject = WeixinUtil.httpRequest(url, "GET", null);
	  	           System.out.println(jsonObject.toString());
	  	            if(jsonObject != null){
	  	        	   myaccessToken = new AccessToken();
	  	        	   myaccessToken.setId(1);
	  	        	   myaccessToken.setAccessToken(jsonObject.getString("access_token"));
	  	        	   myaccessToken.setExpiresin(jsonObject.getString("expires_in"));
	  	        	   myaccessToken.setCreatedate(new Date());
	  	           }
	  	           accessTokenRepository.save(myaccessToken);
	  	       }
	  	       return myaccessToken;
	  	 }
}
