package com.yueqian.demo.model.wechatmessage;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月30日    
 * @Description 消息基类（普通用户 --> 公众号）
 */
public class BaseMessage {
	//开发者微信号
	private String ToUserName;
	//发送方账号（一个openID）
	private String FromUserName;
	// 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型（text/image/location/link）
    private String MsgType;
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
    
    
}
