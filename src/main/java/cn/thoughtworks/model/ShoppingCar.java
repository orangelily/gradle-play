package cn.thoughtworks.model;

import java.util.HashMap;
import java.util.Map;
/**
 * 购物车
 * @author 
 * @date 2016年7月17日 下午6:20:10
 */
public class ShoppingCar {
	//内部类，每一种商品的合计信息
	public class GoodsInCar{
		public Goods goods;
		public Integer count=0;//数量
		public Double subPrice=0.0;//同种商品未进行优惠的累计价格
		public Double discountPrice=0.0;//优惠后的价格
		public String discountOption="";//优惠选项
		public GoodsInCar(Goods goods){
			this.goods = goods;
		}
	}
	//购物车中已有的商品
	private Map<String, GoodsInCar> shopCar = new HashMap<String, GoodsInCar>();
	//购物车中不打则情况下的总价格
	private Double totalPrice=0.0;
	//商品扫码
	public void addGoods(Goods goods,int count,String discountOption){
		if (goods==null||goods.getBarcode()==null||goods.getBarcode().equals("")) {
			return;
		}
		GoodsInCar gc=shopCar.get(goods.getBarcode());
		if (gc==null) {
			//对于购物车里没有的此商品要新建
			gc = new GoodsInCar(goods);
		}
		gc.count += count;
		gc.subPrice=gc.count*goods.getPrice();
		totalPrice+=goods.getPrice();
		gc.discountOption = discountOption;
		shopCar.put(goods.getBarcode(), gc);
	}
	//////////////////////////////////////////////////////
	public Map<String, GoodsInCar> getShopCar() {
		return shopCar;
	}

	public void setShopCar(Map<String, GoodsInCar> shopCar) {
		this.shopCar = shopCar;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
