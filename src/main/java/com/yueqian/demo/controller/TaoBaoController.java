package com.yueqian.demo.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkItemGetRequest;
import com.taobao.api.response.TbkItemGetResponse;
import com.yueqian.demo.utils.MD5_Sign;
import com.yueqian.demo.utils.WeixinUtil;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月17日    
 * @Description 淘宝接口
 */
@RestController
public class TaoBaoController {
	
	@Value("${taobao.app_key}")
    String appkey;

    @Value("${taobao.app_secret}")
    String appsecret;
	
    private static final String SIGN_METHOD_MD5 = "md5";
    
    public static final String serverUrl = "http://gw.api.taobao.com/router/rest";
    
	private Logger logger = LoggerFactory.getLogger(TaoBaoController.class);
	
	//获取传入标题查询到的商品，淘宝客商品查询
	@RequestMapping("/getTaobaoSearchDetail")
	public Map<String, String> getTaobaoSearchDetail(String page,String title,String sort_type){
		String sign = "";
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> map1 = new HashMap<String, String>();
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time);
        map.put("timestamp", nowTimeStamp);
        map.put("v", "2.0");
        map.put("app_key", appkey);
        //map.put("target_app_key", appsecret);
        map.put("method", "taobao.tbk.item.get");
        map.put("format", "json");
        map.put("sort",sort_type);
        map.put("fields", "num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick");
        map.put("page_no", page);
        map.put("page_size", "20");
        try {
			sign = MD5_Sign.signTopRequest(map, appsecret, SIGN_METHOD_MD5);
		} catch (IOException e1) {
			logger.info("淘宝api-商品查询接口：sign签名出错");
			e1.printStackTrace();
		}

		
		TaobaoClient client = new DefaultTaobaoClient(serverUrl,appkey,appsecret);
		TbkItemGetRequest req = new TbkItemGetRequest();
		req.setFields("num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick");
		req.setTimestamp(time);
		req.setQ(title);
		req.setTopApiVersion("2.0");
		req.setTargetAppKey(appkey);
		req.setTopHttpMethod("taobao.tbk.item.get");
		req.setTopApiFormat("json");
		req.setPageNo(Long.valueOf(page));
		req.setPageSize(20L);
		req.setSort(sort_type);
		TbkItemGetResponse rsp = null;
		try {
			rsp = client.execute(req, sign);
		} catch (ApiException e) {
			logger.info("淘宝客商品查询出错！");
			e.printStackTrace();
		}
		System.out.println(rsp.getBody());
		map1.put("content", rsp.getBody());
        map1.put("whatplatform", "taobao");
		return map1;
	}
	
	//设置pid
	@RequestMapping("/setTaobaoPid")
	public String setPid(String pname){
		String sign = "";
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> map1 = new HashMap<String, String>();
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time);
        map.put("timestamp", nowTimeStamp);
        map.put("v", "2.0");
        map.put("app_key", appkey);
        //map.put("target_app_key", appsecret);
        map.put("method", "taobao.tbk.adzone.create");
        map.put("format", "json");
        map.put("site_id", "288750146");
        map.put("adzone_name", pname);
        map.put("sign_method", SIGN_METHOD_MD5);
        try {
			sign = MD5_Sign.signTopRequest(map, appsecret, SIGN_METHOD_MD5);
		} catch (IOException e1) {
			logger.info("淘宝api-商品查询接口：sign签名出错");
			e1.printStackTrace();
		}
        String url="http://gw.api.taobao.com/router/rest?";

        String getgood_Detail_url = "method=taobao.tbk.adzone.create&format=json&timestamp="+nowTimeStamp+"&app_key="+appkey+"&sign="+sign+"&site_id=288750146&adzone_name="+pname+"&v=2.0&sign_method="+SIGN_METHOD_MD5;
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
        return jsonObject;
	}
}
