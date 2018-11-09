package com.yueqian.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yueqian.demo.utils.WeixinUtil;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "${CommonValues.base_url}/WeiChat")
public class WeiChatController {

    @Value("${weichat.appid}")
    String appid;

    @Value("${weichat.appsecret}")
    String appsecret;

    @Value("${weichat.authaccesstoken}")
    String authaccesstoken;

    private Logger logger = LoggerFactory.getLogger(WeiChatController.class);

    private String openId;

    private String openId1;


    @RequestMapping(value="/bind.do")
    public void getweixininfo(@RequestParam(name="code",required=false)String code,@RequestParam(name="state")String
            state,HttpServletResponse res) throws IOException {
        System.out.println("-----------------------------收到请求，请求数据为："+code+"-----------------------"+state);
        String requestUrl = authaccesstoken;
        String get_access_token_url = requestUrl.replace("APPID",appid).replace("SECRET",appsecret);
        get_access_token_url = get_access_token_url.replace("CODE", code);
        JSONObject jsonObject = WeixinUtil.httpRequest(get_access_token_url, "GET", null);
        openId = jsonObject.getString("openid");
//        List<CustomerInfoEntity> customerInfoEntities=customerInfoService.findByWeixin(openId);
        boolean flag=true;
//        for(CustomerInfoEntity customerInfoEntity:customerInfoEntities){
//            if(StringUtil.isNotBlank(customerInfoEntity.getWeixin())){
//                flag=false;
//                break;
//            }
//        }
        if(!flag){
            res.setCharacterEncoding("utf-8");
            res.sendRedirect("/weichat/alreadybind.html");
        }else{
            res.setCharacterEncoding("utf-8");
            res.sendRedirect("/weichat/bindweichat.html");
        }

    }


    @RequestMapping(value="/show.do")
    public void getweixinopenid(@RequestParam(name="code",required=false)String code,@RequestParam(name="state")String
            state,HttpServletResponse res) throws IOException {
        System.out.println("-----------------------------收到请求，请求数据为："+code+"-----------------------"+state);
        String requestUrl = authaccesstoken;
        String get_access_token_url = requestUrl.replace("APPID",appid).replace("SECRET",appsecret);
        get_access_token_url = get_access_token_url.replace("CODE", code);
        JSONObject jsonObject = WeixinUtil.httpRequest(get_access_token_url, "GET", null);
        openId1 = jsonObject.getString("openid");
        res.setCharacterEncoding("utf-8");
        res.sendRedirect("/weichat/showmyaddr.html");
    }


    @RequestMapping(value="/showmaterials.do")
    public void showmaterials(@RequestParam(name="code",required=false)String code,@RequestParam(name="state")String
            state,HttpServletResponse res) throws IOException {
        System.out.println("-----------------------------收到请求，请求数据为："+code+"-----------------------"+state);
        String requestUrl = authaccesstoken;
        String get_access_token_url = requestUrl.replace("APPID",appid).replace("SECRET",appsecret);
        get_access_token_url = get_access_token_url.replace("CODE", code);
        JSONObject jsonObject = WeixinUtil.httpRequest(get_access_token_url, "GET", null);
        openId1 = jsonObject.getString("openid");
        res.setCharacterEncoding("utf-8");
        res.sendRedirect("/weichat/showmaterials.html");
    }

   

   

    @RequestMapping(value="/showfinance.do")
    public void showfinance(@RequestParam(name="code",required=false)String code,@RequestParam(name="state")String
            state,HttpServletResponse res) throws IOException {
        String requestUrl = authaccesstoken;
        String get_access_token_url = requestUrl.replace("APPID",appid).replace("SECRET",appsecret);
        get_access_token_url = get_access_token_url.replace("CODE", code);
        JSONObject jsonObject = WeixinUtil.httpRequest(get_access_token_url, "GET", null);
        openId1 = jsonObject.getString("openid");
        res.setCharacterEncoding("utf-8");
        res.sendRedirect("/weichat/showfinance.html");
    }

   

}
