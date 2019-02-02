package com.yueqian.demo.timedtasks;

import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yueqian.demo.controller.WeiChatController;
import com.yueqian.demo.dao.system.PddOrderUpdateTimeRepository;
import com.yueqian.demo.dao.user.OrderRepository;
import com.yueqian.demo.model.system.PddOrderUpdateTime;
import com.yueqian.demo.model.user.OrderDetail;
import com.yueqian.demo.model.weichattemplate.AccessToken;
import com.yueqian.demo.utils.MD5_Sign;
import com.yueqian.demo.utils.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author 吴佶津  
 * @date 2019年1月13日    
 * @Description 定时获取更新订单
 */
@Component
public class GetOrderTask {
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	PddOrderUpdateTimeRepository pddOrderUpdateTimeRepository;
	
	private Logger logger = LoggerFactory.getLogger(GetOrderTask.class);
	
	//按照时间段获取授权多多客下面所有多多客的推广订单信息
	@Scheduled(cron = "0 0/10 * * * ?")
	public void getOrderList(){
		logger.info("按照时间段获取授权多多客下面所有多多客的推广订单开始啦...............");
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
	    Long start_update_time1 = System.currentTimeMillis();
	    Optional<PddOrderUpdateTime> oPddOrderUpdateTime = pddOrderUpdateTimeRepository.findById(1L);
	    PddOrderUpdateTime pddOrderUpdateTime = new PddOrderUpdateTime();
	    if(!oPddOrderUpdateTime.isPresent() || oPddOrderUpdateTime.get() == null) {
	    	pddOrderUpdateTime.setPoutid(1L);
	    	pddOrderUpdateTime.setPddupdatetime(start_update_time1);
	    	pddOrderUpdateTime.setUpdatetime(start_update_time1);
	    }else {
	    	start_update_time1 = oPddOrderUpdateTime.get().getPddupdatetime();
	    	pddOrderUpdateTime.setPoutid(1L);
	    	pddOrderUpdateTime.setPddupdatetime(start_update_time1+300000);
	    	pddOrderUpdateTime.setUpdatetime(start_update_time1+240000);
	    }
	    pddOrderUpdateTimeRepository.save(pddOrderUpdateTime);
	    
	    String start_update_time = String.valueOf(start_update_time1 / 1000);
        Long end_update_time1 = start_update_time1+300000;
        String end_update_time = String.valueOf(end_update_time1 / 1000);
        
        String page_size = "50";
        String page = "10000";
       
		TreeMap<String,String> map= new TreeMap<String, String>();
        //商品
        map.put("type","pdd.ddk.order.list.increment.get");
        map.put("client_id","4b45df16ebb643bf9594e93e55b289da");
        map.put("timestamp",nowTimeStamp);
        map.put("data_type","JSON");
        map.put("start_update_time",start_update_time);
        map.put("end_update_time",end_update_time);
        map.put("page_size",page_size);
        map.put("page",page);
        map.put("return_count","true");
        sign = MD5_Sign.sign(map);
        
        		
//				        System.out.println(sign);
        String url="http://gw-api.pinduoduo.com/api/router?";

        String getgood_Detail_url = "type=pdd.ddk.order.list.increment.get&data_type=JSON&timestamp="+nowTimeStamp+"&client_id=4b45df16ebb643bf9594e93e55b289da&sign="+sign+"&start_update_time="+start_update_time+"&end_update_time="+end_update_time+"&page_size="+page_size+"&page="+page+"&return_count=true";
        String jsonObject = WeixinUtil.sendPostUrl(url,getgood_Detail_url);
        System.out.println(jsonObject);
        JSONObject json = JSONObject.fromObject(jsonObject);
        JSONObject json1 = JSONObject.fromObject(json.getString("order_list_get_response"));
        JSONArray jsonArray = JSONArray.fromObject(json1.getString("order_list"));
        System.out.println("----"+json1.getString("total_count"));
        System.out.println("----"+json1.get("order_list"));
        for(int i = jsonArray.size();i > 0;i--) {
        	 if(jsonArray.getJSONObject(i) != null) {
        		 OrderDetail existOrder = orderRepository.fingByOrdersn(jsonArray.getJSONObject(i).getString("order_sn"));
        		 if(jsonArray.getJSONObject(i).getInt("order_status") == -1
	        				 || (jsonArray.getJSONObject(i).getInt("order_status") == 0 && existOrder == null)) //订单状态为 -1 未支付;或者 0-已支付且数据库里没有该订单号，则写入数据库，增加一条订单信息；
	        		 {
        			 	 OrderDetail order = new OrderDetail();
	                     order.setGoodsname(jsonArray.getJSONObject(i).getString("goods_name"));
	                     order.setGoodsprice(jsonArray.getJSONObject(i).getLong("goods_price"));
	                     order.setGoodsquantity(jsonArray.getJSONObject(i).getLong("goods_quantity"));
	                     order.setOrderamount(jsonArray.getJSONObject(i).getLong("order_amount"));
	                     order.setGoods_thumbnail_url(jsonArray.getJSONObject(i).getString("goods_thumbnail_url"));
	                     order.setOrdercreatetime(jsonArray.getJSONObject(i).getLong("order_create_time"));
	                     order.setOrdergroupsuccesstime(jsonArray.getJSONObject(i).getLong("order_group_success_time"));
	                     order.setOrdermodifyat(jsonArray.getJSONObject(i).getLong("order_modify_at"));
	                     order.setOrderpaytime(jsonArray.getJSONObject(i).getLong("order_pay_time"));
	                     order.setOrdersn(jsonArray.getJSONObject(i).getString("order_sn"));
	                     order.setOrderstatus(jsonArray.getJSONObject(i).getInt("order_status"));
	                     order.setOrderstatusdesc(jsonArray.getJSONObject(i).getString("order_status_desc"));
	                     order.setOrderverifytime(jsonArray.getJSONObject(i).getLong("order_verify_time"));
	                     order.setPid(jsonArray.getJSONObject(i).getString("p_id"));
	                     order.setPromotionamount(jsonArray.getJSONObject(i).getLong("promotion_amount"));
	                     order.setPromotionrate(jsonArray.getJSONObject(i).getLong("promotion_rate"));
	                     orderRepository.save(order);
	        		 }else {
	        			 existOrder.setOrderstatus(jsonArray.getJSONObject(i).getInt("order_status"));
	        			 orderRepository.save(existOrder);
	        		 }
//            	 if(jsonArray.getJSONObject(i).getInt("order_status") == 4 && existOrder != null){ //订单状态为 4，即订单发生售后，订单状态置为审核失败;更新已有订单数据的状态；
//            		 existOrder.setOrderstatus(jsonArray.getJSONObject(i).getInt("order_status"));
//            		 orderRepository.save(existOrder);
//            	 }
//            	 if(jsonArray.getJSONObject(i).getInt("order_status") == 5 && existOrder != null){ //订单状态为 5，即订单已结算，订单状态置为已结算;更新已有订单数据的状态；
//            		 existOrder.setOrderstatus(jsonArray.getJSONObject(i).getInt("order_status"));
//            		 orderRepository.save(existOrder);
//            	 }
            	 
            }
        }
        
        logger.info("按照时间段获取授权多多客下面所有多多客的推广订单结束啦...............");
	}


}
