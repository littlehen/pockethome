package com.yueqian.demo.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsPidGenerateRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsPidQueryRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsRecommendGetRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPidGenerateResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPidQueryResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsRecommendGetResponse;
import com.yueqian.demo.dao.amdin.BabyRepository;
import com.yueqian.demo.dao.amdin.CarouselUrlRepository;
import com.yueqian.demo.dao.system.PidRepository;
import com.yueqian.demo.dao.system.YaoqingmaRepository;
import com.yueqian.demo.dao.user.UserInfoRepository;
import com.yueqian.demo.model.admin.Baby;
import com.yueqian.demo.model.admin.CarouselUrl;
import com.yueqian.demo.model.system.Pid;
import com.yueqian.demo.model.system.Yaoqingma;
import com.yueqian.demo.model.user.UserInfo;
import com.yueqian.demo.service.admin.BabyService;
import com.yueqian.demo.service.admin.CarouselUrlService;
import com.yueqian.demo.utils.MD5_Sign;
import com.yueqian.demo.utils.MatrixToImageWriter;
import com.yueqian.demo.utils.WeixinUtil;

import net.sf.json.JSONArray;

/**
 * 
 * @author 吴佶津  
 * @date 2018年11月19日    
 * @Description 拼多多接口
 */
@RestController
public class PinDuoDuoController {
	
	@Autowired
	CarouselUrlService carouselUrlService;
	
	@Autowired
	CarouselUrlRepository carouselUrlRepository;
	
	@Autowired
	BabyService babyService;
	
	@Autowired
	BabyRepository babyRepository;
	
	@Autowired
	PidRepository pidRepository;
	
	@Autowired
	YaoqingmaRepository yaoqingmaRepository;
	
	@Autowired 
	UserInfoRepository userInfoRepository;
	
	@Value("${pinduoduo.client_id}")
    public  static   String client_id = "4b45df16ebb643bf9594e93e55b289da";
 
	@Value("${pinduoduo.client_secret}")
    public  static  String client_secret = "9c16f347d216669b85061c1ee6d407ac6b66b9eb";
	
	//获取传入标题查询到的商品
	@RequestMapping("/getSearchDetail")
	public Map<String,String> getSearchDetail(String page,String title,String sort_type){
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
		Map<String,String> map1 = new HashMap<String,String>();
        //商品
        map.put("sort_type",sort_type);
        map.put("type","pdd.ddk.goods.search");
        map.put("client_id",client_id);
        map.put("keyword", title);
        map.put("page", page);
        map.put("page_size", "20");
        map.put("with_coupon","true");
        map.put("data_type","JSON");
        map.put("timestamp",nowTimeStamp);
//        String strmap = JSONObject.fromObject(map).toString();
//        System.out.println(nowTimeStamp);
        sign = MD5_Sign.sign(map);
//        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.ddk.goods.search&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sort_type="+sort_type+"&with_coupon=true&sign="+sign+"&keyword="+title+"&page="+page+"&page_size=20";
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
        map1.put("content", jsonObject);
        map1.put("whatplatform", "pinduoduo"); //判断是什么平台
		return map1;
	}
	
	//获取商品详情
	@RequestMapping("/getGoodDetail")
	public String getGoodDetail(String goods_id_list){
		System.out.println(goods_id_list);
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("type","pdd.ddk.goods.detail");
        map.put("client_id",client_id);
        map.put("goods_id_list",goods_id_list);
        map.put("data_type","JSON");
        map.put("timestamp",nowTimeStamp);
//        String strmap = JSONObject.fromObject(map).toString();
//        System.out.println(nowTimeStamp);
        sign = MD5_Sign.sign(map);
//        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.ddk.goods.detail&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&goods_id_list="+goods_id_list;
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
		return jsonObject;
	}
	
	//获取商品详情
	@RequestMapping("/getGoods")
	public String getGoods(String cat_id,String page,String sort_type){
		System.out.println("---------="+cat_id);
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("sort_type",sort_type);
        map.put("type","pdd.ddk.goods.search");
        map.put("client_id",client_id);
        if(!cat_id.equals("0")) {
            map.put("cat_id", cat_id);
		}
        map.put("page", page);
        map.put("page_size", "20");
        map.put("with_coupon","true");
        map.put("data_type","JSON");
        map.put("timestamp",nowTimeStamp);
//        String strmap = JSONObject.fromObject(map).toString();
//        System.out.println(nowTimeStamp);
        sign = MD5_Sign.sign(map);
//        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";
        String getgood_Detail_url = "";
        if(!cat_id.equals("0")) {
        	getgood_Detail_url = "type=pdd.ddk.goods.search&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sort_type="+sort_type+"&with_coupon=true&sign="+sign+"&page="+page+"&page_size=20&cat_id="+cat_id;
		}else {
			getgood_Detail_url = "type=pdd.ddk.goods.search&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sort_type="+sort_type+"&with_coupon=true&sign="+sign+"&page="+page+"&page_size=20";
		}
        
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
		return jsonObject;
	}
	
	//获取pid
	@RequestMapping("/getPId")
	public String getPId(int page){
//		String sign="";
//		long time = System.currentTimeMillis();
//	    String nowTimeStamp = String.valueOf(time / 1000);
//		TreeMap<String,String> map=new TreeMap<String, String>();
//        //商品
//        map.put("type","pdd.ddk.goods.pid.query");
//        map.put("client_id",client_id);
//        map.put("page","1");
//        map.put("page_size","100");
//        map.put("timestamp",nowTimeStamp);
//        map.put("data_type","JSON");
//        sign = MD5_Sign.sign(map);
////        System.out.println(sign);
//        String url="http://gw-api.pinduoduo.com/api/router?";
//
//        String getgood_Detail_url = "type=pdd.ddk.goods.pid.query&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&page=1&page_size=100";
//        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
//        System.out.println(jsonObject);
        
        PopClient client = new PopHttpClient(client_id, client_secret);

        PddDdkGoodsPidQueryRequest request = new PddDdkGoodsPidQueryRequest();
        request.setPage(page);
        request.setPageSize((int) 100L);

        PddDdkGoodsPidQueryResponse response = null;
		try {
			response = client.syncInvoke(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String mpid = JsonUtil.transferToJson(response); //调用拼多多设置pid接口
		JSONObject jsonObject1 = JSONObject.parseObject(mpid);
		
		for(int i = 0;i < jsonObject1.getJSONObject("pidQueryResponse").getJSONArray("pidList").size(); i++) {
			Pid pid = new Pid();
			pid.setPddpid(jsonObject1.getJSONObject("pidQueryResponse").getJSONArray("pidList").getJSONObject(i).getString("pid"));
			pidRepository.save(pid);
		}
//		System.out.println(jsonObject1.getJSONObject("pidQueryResponse").getJSONArray("pidList").size());
		return JsonUtil.transferToJson(response);
	}
	
	//循环生成10w个pid
	@RequestMapping("/inputPId")
	public void inputPId(){
		for(int i = 1;i <= 1000;i++) {
			getPId(i);
		}
	}

	//循环生成000000到999999的数字邀请码
	@RequestMapping("/createYaqingma")
	public void createYaqingma(){
		List<String> list = new ArrayList<>();
		for(int i = 0;i < 1000000;i++) {
			list.add(String.format("%06d", i));
		}
		//直接调用shuffle，就是随机排序
		Collections.shuffle(list);
		for(int i = 0;i < list.size();i++) {
			if(!isSameCharacter(list.get(i))) {
				Yaoqingma yaoqingma = new Yaoqingma();
				yaoqingma.setYqm(list.get(i));
				yaoqingmaRepository.save(yaoqingma);
			}
			
		}
	}
	
	//判断是否是相同数字的邀请码，如000000，111111
	public boolean isSameCharacter(String s){
		//如果不区分大小写，可以添加下面这句
		String character = s.substring(0,1);
		String replace = "";
		String test = s.replace(character, replace);
		if("".equals(test))
		return true;
		else
		return false;
		
	}
	
	//设置pid
	@RequestMapping("/setPid")
	public String setPid(){
//		String sign="";
//		long time = System.currentTimeMillis();
//	    String nowTimeStamp = String.valueOf(time / 1000);
//		TreeMap<String,String> map=new TreeMap<String, String>();
//        //商品
//        map.put("type","pdd.ddk.goods.pid.generate");
//        map.put("client_id","4b45df16ebb643bf9594e93e55b289da");
//        map.put("number","1");
//        map.put("p_id_name_list",pname);
//        map.put("timestamp",nowTimeStamp);
//        map.put("data_type","JSON");
//        sign = MD5_Sign.sign(map);
////        System.out.println(sign);
//        String url="http://gw-api.pinduoduo.com/api/router?";
//
//        String getgood_Detail_url = "type=pdd.ddk.goods.pid.generate&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&number=1&p_id_name_list="+pname;
//        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
//        System.out.println(jsonObject);
        
        
        
        PopClient client = new PopHttpClient(client_id, client_secret);

        PddDdkGoodsPidGenerateRequest request = new PddDdkGoodsPidGenerateRequest();
        request.setNumber(8L);
        //request.setPIdNameList(Arrays.asList(pname));

        PddDdkGoodsPidGenerateResponse response = null;
		try {
			response = client.syncInvoke(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(JsonUtil.transferToJson(response));
		return JsonUtil.transferToJson(response);
	}
	
	//获取推广链接
	@RequestMapping("/generatePUrl")
	public String generatePUrl(String goods_id_list,String openId){
		UserInfo userIfo = new UserInfo();
        userIfo = userInfoRepository.findByOpenid(openId);
        String p_id = "3444993_33040639"; //口袋专享官方拼多多pid，没有成为店长的，佣金全归此pid
        if(userIfo != null && (userIfo.getPid() != null || userIfo.getPid().equals(""))) {
        	p_id = userIfo.getPid();
        }
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("type","pdd.ddk.goods.promotion.url.generate");
        map.put("client_id",client_id);
        map.put("p_id",p_id);
        map.put("goods_id_list",goods_id_list);
        map.put("generate_weapp_webview","true");
        map.put("generate_short_url","true");
        map.put("generate_we_app","false");
        map.put("timestamp",nowTimeStamp);
        map.put("data_type","JSON");
        sign = MD5_Sign.sign(map);
//        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.ddk.goods.promotion.url.generate&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&p_id=3444993_33040639"+"&goods_id_list="+goods_id_list+"&generate_weapp_webview=true&generate_we_app=false&generate_short_url=true";
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        
		return jsonObject;
	}
	
	//生成二维码
	@RequestMapping("/qr-code")
	public void placeQrOrder(HttpServletResponse resp,String url) {
		System.out.println(url);
	    resp.setHeader("Cache-Control", "no-store");
	    resp.setHeader("Pragma", "no-cache");
	    resp.setDateHeader("Expires", 0);
	    resp.setContentType("image/png");

	    Map<EncodeHintType, Object> hints = new HashMap<>();
	    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	    hints.put(EncodeHintType.MARGIN, 0);
	    
	    BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter()
			        .encode(url, BarcodeFormat.QR_CODE, 100, 100, hints);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", resp.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取商品标准类目
	@RequestMapping("/getcats")
	public String getcats(String parent_cat_id){
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("type","pdd.goods.cats.get");
        map.put("client_id",client_id);
        map.put("parent_cat_id",parent_cat_id);
        map.put("timestamp",nowTimeStamp);
        map.put("data_type","JSON");
        sign = MD5_Sign.sign(map);
//        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.goods.cats.get&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&parent_cat_id="+parent_cat_id;
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
		return jsonObject;
	}
	
	//查询商品标签列表
	@RequestMapping("/getopt")
	public String getopt(String parent_opt_id){
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("type","pdd.goods.opt.get");
        map.put("client_id",client_id);
        map.put("parent_opt_id",parent_opt_id);
        map.put("timestamp",nowTimeStamp);
        map.put("data_type","JSON");
        sign = MD5_Sign.sign(map);
//	        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.goods.opt.get&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&parent_opt_id="+parent_opt_id;
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
		return jsonObject;
	}
	
	//主题列表查询
	@RequestMapping("/getthemelist")
	public String getthemelist(){
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("type","pdd.ddk.theme.list.get");
        map.put("client_id",client_id);
        map.put("timestamp",nowTimeStamp);
        map.put("data_type","JSON");
        sign = MD5_Sign.sign(map);
//		        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.ddk.theme.list.get&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign;
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
		return jsonObject;
	}
	
	//主题商品查询
	@RequestMapping("/searchthemegoods")
	public String searchthemegoods(String theme_id){
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("type","pdd.ddk.theme.goods.search");
        map.put("client_id",client_id);
        map.put("timestamp",nowTimeStamp);
        map.put("data_type","JSON");
        map.put("theme_id",theme_id);
        sign = MD5_Sign.sign(map);
//			        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.ddk.theme.goods.search&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&theme_id="+theme_id;
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
		return jsonObject;
	}
	
	/**
	 * 
	 * @author 吴佶津  
	 * @date 2018年11月26日    
	 * @Description 运营频道商品查询
	 * @param channel_type 频道类型；0, "1.9包邮", 1, "今日爆款", 2, "品牌清仓", 3, "默认商城"
	 * @param offset 从多少位置开始请求；默认值 ： 0
	 * @return
	 */
	@RequestMapping("/getrecommendgoods")
	public String getrecommendgoods(String channel_type,String offset){
//		String sign="";
//		long time = System.currentTimeMillis();
//	    String nowTimeStamp = String.valueOf(time / 1000);
//		TreeMap<String,String> map=new TreeMap<String, String>();
//        //商品
//        map.put("type","pdd.ddk.goods.recommend.get");
//        map.put("client_id","4b45df16ebb643bf9594e93e55b289da");
//        map.put("timestamp",nowTimeStamp);
//        map.put("data_type","JSON");
//        map.put("channel_type",channel_type);
//        map.put("offset",offset);
//        map.put("limit","20");
//        sign = MD5_Sign.sign(map);
////				        System.out.println(sign);
//        String url="http://gw-api.pinduoduo.com/api/router?";
//
//        String getgood_Detail_url = "type=pdd.ddk.goods.recommend.get&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&channel_type="+channel_type+"&offset="+offset+"&limit=20";
//        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
//        System.out.println(jsonObject);
        
        
        
        
        
        
        
        PopClient client = new PopHttpClient(client_id, client_secret);

        PddDdkGoodsRecommendGetRequest request = new PddDdkGoodsRecommendGetRequest();
        request.setOffset(Integer.valueOf(offset));
        request.setLimit((int) 20L);
        request.setChannelType((int) 1L);
        request.setPid("3444993_33040639");
//        request.setCustomParameters("testStr");

        PddDdkGoodsRecommendGetResponse response = null;
		try {
			response = client.syncInvoke(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("11111"+JsonUtil.transferToJson(response));
		return JsonUtil.transferToJson(response);
	}
	
	@RequestMapping("/getselectgoods")
	public String[] getselectgoods(){
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
		
        //商品
        map.put("sort_type","0");
        map.put("type","pdd.ddk.goods.search");
        map.put("client_id",client_id);
        
        map.put("with_coupon","true");
        map.put("data_type","JSON");
        map.put("timestamp",nowTimeStamp);
        List <Baby>  babyList = babyRepository.findAllList();
        String url="http://gw-api.pinduoduo.com/api/router?";
        String getgood_Detail_url = null;
        if(!babyList.isEmpty()) {
        	String jsonObject[] = new String[babyList.size()];
        	for(int i = 0;i < babyList.size(); i++) {
        		map.put("keyword", babyList.get(i).getTitle());
        		sign = MD5_Sign.sign(map);
                getgood_Detail_url = "type=pdd.ddk.goods.search&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sort_type=0"+"&with_coupon=true&sign="+sign+"&keyword="+babyList.get(i).getTitle();
                jsonObject[i] = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
                System.out.println("------------------------"+jsonObject[i]);
        	}
        	return jsonObject;
        }
        
        
		return null;
	}
	
	//获取首页轮播图和链接
	@RequestMapping("/getCarouselUrls")
	public Map<String,Object> getCarouselUrls(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<CarouselUrl> carouselUrls = carouselUrlRepository.findAllList();
		if(!carouselUrls.isEmpty()) {
			map.put("list",carouselUrls);
			map.put("lsize", carouselUrls.size());
			map.put("state",true);
		}else {
			map.put("state", false);
		}
		return map;
	}
	
	

}
