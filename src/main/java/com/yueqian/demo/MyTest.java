package com.yueqian.demo;

import java.util.Date;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yueqian.demo.utils.MD5_Sign;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class MyTest {
	@Test
	public void test() throws Exception{
		String sign="";
		long time = System.currentTimeMillis();
	    String nowTimeStamp = String.valueOf(time / 1000);
		TreeMap<String,String> map=new TreeMap<String, String>();
        //商品
        map.put("sort_type","0");
        map.put("type","pdd.ddk.goods.search");
        map.put("client_id","4b45df16ebb643bf9594e93e55b289da");
        map.put("keyword", "【加绒加厚套装】阿尔皮纳袋鼠 男士保暖内衣男套装圆领秋衣秋裤");
        
        map.put("with_coupon","false");
        map.put("data_type","JSON");
        map.put("timestamp",nowTimeStamp);
        System.out.println(nowTimeStamp);
        sign = MD5_Sign.sign(map);
        System.out.println(sign);
//		String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(1541851886 * 1000));
//      Long timestamp = Long.parseLong(timestampString)*1000;  
//      String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));  
	}
}
