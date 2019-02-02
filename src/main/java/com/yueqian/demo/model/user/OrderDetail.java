package com.yueqian.demo.model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderid;
	
	private String ordersn;
	
	private String goods_thumbnail_url;
	
	public String getGoods_thumbnail_url() {
		return goods_thumbnail_url;
	}

	public void setGoods_thumbnail_url(String goods_thumbnail_url) {
		this.goods_thumbnail_url = goods_thumbnail_url;
	}

	private Long goodsid;
	
	private String goodsname;
	
	private Long goodsquantity;//购买商品的数量
	
	private Long goodsprice;//订单中sku的单件价格，单位为分
	
	private Long orderamount;//实际支付金额，单位为分
	
	private String pid;//推广位ID
	
	private Long promotionrate;//佣金比例，千分比
	
	private Long promotionamount;//佣金金额，单位为分
	
	private Integer orderstatus;//订单状态： -1 未支付; 0-已支付；1-已成团；2-确认收货；3-审核成功；4-审核失败（不可提现）；5-已经结算；8-非多多进宝商品（无佣金订单）
	
	private String orderstatusdesc;//订单状态描述
	
	private Long ordercreatetime;//订单生成时间，UNIX时间戳
	
	private Long orderpaytime;//支付时间
	
	private Long ordergroupsuccesstime;//成团时间
	
	private Long orderverifytime;//审核时间
	
	private Long ordermodifyat;//最后更新时间

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}

	public Long getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(Long goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public Long getGoodsquantity() {
		return goodsquantity;
	}

	public void setGoodsquantity(Long goodsquantity) {
		this.goodsquantity = goodsquantity;
	}

	public Long getGoodsprice() {
		return goodsprice;
	}

	public void setGoodsprice(Long goodsprice) {
		this.goodsprice = goodsprice;
	}

	public Long getOrderamount() {
		return orderamount;
	}

	public void setOrderamount(Long orderamount) {
		this.orderamount = orderamount;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Long getPromotionrate() {
		return promotionrate;
	}

	public void setPromotionrate(Long promotionrate) {
		this.promotionrate = promotionrate;
	}

	public Long getPromotionamount() {
		return promotionamount;
	}

	public void setPromotionamount(Long promotionamount) {
		this.promotionamount = promotionamount;
	}

	public Integer getOrderstatus() {
		return orderstatus;
	}

	public void setOrderstatus(Integer orderstatus) {
		this.orderstatus = orderstatus;
	}

	public String getOrderstatusdesc() {
		return orderstatusdesc;
	}

	public void setOrderstatusdesc(String orderstatusdesc) {
		this.orderstatusdesc = orderstatusdesc;
	}

	public Long getOrdercreatetime() {
		return ordercreatetime;
	}

	public void setOrdercreatetime(Long ordercreatetime) {
		this.ordercreatetime = ordercreatetime;
	}

	public Long getOrderpaytime() {
		return orderpaytime;
	}

	public void setOrderpaytime(Long orderpaytime) {
		this.orderpaytime = orderpaytime;
	}

	public Long getOrdergroupsuccesstime() {
		return ordergroupsuccesstime;
	}

	public void setOrdergroupsuccesstime(Long ordergroupsuccesstime) {
		this.ordergroupsuccesstime = ordergroupsuccesstime;
	}

	public Long getOrderverifytime() {
		return orderverifytime;
	}

	public void setOrderverifytime(Long orderverifytime) {
		this.orderverifytime = orderverifytime;
	}

	public Long getOrdermodifyat() {
		return ordermodifyat;
	}

	public void setOrdermodifyat(Long ordermodifyat) {
		this.ordermodifyat = ordermodifyat;
	}
	
}
