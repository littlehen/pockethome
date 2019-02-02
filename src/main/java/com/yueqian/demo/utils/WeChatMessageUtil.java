package com.yueqian.demo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.yueqian.demo.model.wechatmessage.Image;
import com.yueqian.demo.model.wechatmessage.ImageMessage;
import com.yueqian.demo.model.wechatmessage.TextMessage;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月30日    
 * @Description 微信消息工具类
 */
public class WeChatMessageUtil {
	/**
	 * 返回消息类型：文本
	 */
	
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	 /**
     * 返回消息类型：音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";

    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

    /**
     * 请求消息类型：链接
     */
    public static final String REQ_MESSAGE_TYPE_LINK = "link";

    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";
    
    /**
     * 事件类型：subscribe(订阅)and 未关注群体扫描二维码
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：已关注群体扫描二维码
     */
    public static final String EVENT_TYPE_SCAN="SCAN";
    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";
    /**
     * 事件类型：VIEW(点击自定义菜单跳转链接时的事件)
     */
    public static final String EVENT_TYPE_VIEW = "VIEW";

    /**
     * 事件类型：transfer_customer_service(把消息推送给客服)
     */
    public static final String EVENT_TYPE_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";
    
    /**
     * 解析微信发来的请求（xml）
     * 
     * @param request
     * @return
     * @throws IOException 
     * @throws DocumentException 
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    //屏蔽某些编译时的警告信息(在强制类型转换的时候编译器会给出警告)
    public static   Map<String , String> parseXml(HttpServletRequest request) throws IOException, DocumentException{
    	//将解析结果储存在HashMap上
    	Map<String, String> map = new HashMap<String, String>();
    	
    	// 从request中取得输入流
    	InputStream inputStream = request.getInputStream();
    	
    	//读取输入流
    	SAXReader reader = new SAXReader();
    	Document document = reader.read(inputStream);
    	
    	//得到xml根元素
    	Element root = document.getRootElement();
    	
    	//得到根元素的所有子节点
    	List<Element> elementList = root.elements();
    	//遍历所有子节点
	   	 for (Element e : elementList)
	            map.put(e.getName(), e.getText());
	   	 // 释放资源
	        inputStream.close();
	        inputStream = null;
	
	        return map;
    } 
    
    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     */
    
    public static String textMessageToXml(TextMessage textMessage) {
    	XStream xstream = new XStream();
    	xstream.alias("xml",textMessage.getClass());
    	return xstream.toXML(textMessage);
    }
    
    /**
     * 
     * @author 吴佶津  
     * @date 2019年1月31日    
     * @Description 组装文本xml
     * @param MediaId
     * @param toUserName
     * @param fromUserName
     * @return
     */
	public static String initTextMessage(String respContent,String toUserName,String fromUserName){	
		String respMessage = null; 
	    TextMessage textMessage = new TextMessage();  
	    textMessage.setToUserName(fromUserName);  
	    textMessage.setFromUserName(toUserName);  
	    textMessage.setCreateTime(new Date().getTime());  
	    textMessage.setMsgType(WeChatMessageUtil.RESP_MESSAGE_TYPE_TEXT);  
	    textMessage.setContent(respContent);  
        respMessage = textMessageToXml(textMessage);
        return respMessage;
	}
    
    /**
     * 
     * @author 吴佶津  
     * @date 2019年1月31日    
     * @Description 图片转成xml
     * @param imageMessage
     * @return
     */
    public static String imageMessageToXml(ImageMessage imageMessage){
		
		XStream xstream = new XStream();//xstream.jar,xmlpull.jar
		xstream.alias("xml", imageMessage.getClass());//置换根节点
		//System.out.println(xstream.toXML(imageMessage));
		return xstream.toXML(imageMessage);
		
	}
	
    /**
     * 
     * @author 吴佶津  
     * @date 2019年1月31日    
     * @Description 组装图片xml
     * @param MediaId
     * @param toUserName
     * @param fromUserName
     * @return
     */
	public static String initImageMessage(String MediaId,String toUserName,String fromUserName){		
		String message = null;
		Image image = new Image();
		ImageMessage imageMessage = new ImageMessage();
		image.setMediaId(MediaId);
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		imageMessage.setMsgType(REQ_MESSAGE_TYPE_IMAGE);
		message = imageMessageToXml(imageMessage);
		return message;
	}
}
