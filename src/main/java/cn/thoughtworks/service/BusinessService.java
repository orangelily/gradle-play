package cn.thoughtworks.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.thoughtworks.exception.AddGoodsInCarInfoException;
import cn.thoughtworks.exception.DiscountInfoException;
import cn.thoughtworks.exception.GoodsInfoException;
import cn.thoughtworks.exception.ShopCarInfoException;
import cn.thoughtworks.model.Discount;
import cn.thoughtworks.model.Goods;
import cn.thoughtworks.model.ShoppingCar;
import cn.thoughtworks.model.ShoppingCar.GoodsInCar;

public class BusinessService {
	// 商店内所有商品列表
	private Map<String, Goods> allGoods = new HashMap<String, Goods>();
	// 购物车商品列表
	private ShoppingCar shoppingCar = new ShoppingCar();
	//买二送一的商品列表
	private List<String> dis2_1 = new ArrayList<String>(); 
	//95的商品列表
	private List<String> dis95 = new ArrayList<String>();
	
	// 初始化商店内部现有商品信息
	public void initGoods(String infoGoods) throws Exception {
		Gson gson = new Gson();
		//
		List<Goods> listGd = new ArrayList<Goods>();
		try {
			// 根据json字符串，解析得到商店内所有现存的商品信息
			listGd = gson.fromJson(infoGoods, new TypeToken<List<Goods>>() {
			}.getType());

		} catch (Exception e) {
			throw new GoodsInfoException();
		}
		if (listGd != null) {
			for (int i = 0; i < listGd.size(); i++) {
				allGoods.put(listGd.get(i).getBarcode(), listGd.get(i));
			}
		}
	}
	// 初始化优惠信息
		public void initDiscount(String infoDiscount) throws Exception {
			Gson gson = new Gson();
			List<Discount> discount = null;
			try {
				// 根据json字符串，解析得到商店内所有促销信息
				discount = gson.fromJson(infoDiscount, new TypeToken<List<Discount>>(){}.getType());

			} catch (Exception e) {
				throw new DiscountInfoException();
			}
			if (discount!=null&&discount.size()>=0) {
				for (int i = 0; i < discount.size(); i++) {
					if(discount.get(i).getType().equals("BUY_TWO_GET_ONE_FREE")){
						//促销信息为BUY_TWO_GET_ONE_FREE：买二送一
						dis2_1 =Arrays.asList(discount.get(i).getBarcodes());
					}else if(discount.get(i).getType().equals("FIVE_PERCENT_OFF")){
						//促销信息为FIVE_PERCENT_OFF：95折
						dis95 = Arrays.asList(discount.get(i).getBarcodes());
					}else{
					}
				}
			}
		}
		
		// 初始化购物车信息
		public void initShopCar(String infoShopCar) throws Exception {
			ShoppingCar sc = new ShoppingCar();
			try {
				/*
				 * 输入格式（样例）： // javascript [ 'ITEM000001', 'ITEM000001',
				 * 'ITEM000001', 'ITEM000001', 'ITEM000001', 'ITEM000003-2',
				 * 'ITEM000005', 'ITEM000005', 'ITEM000005' ]
				 */
				// 根据输入字符串，拆分去掉首尾[] , 得到购物车的商品信息
				String newShopCar = infoShopCar.trim();
				Integer index = newShopCar.indexOf('[');
				if (index<0) {
					throw new ShopCarInfoException();
				}
				index = newShopCar.indexOf(']');
				if (index<0) {
					throw new ShopCarInfoException();
				}
				newShopCar = newShopCar.substring(1, newShopCar.length() - 1).trim();
				String[] barcodeArr = newShopCar.split(",");
				for (int i = 0; i < barcodeArr.length; i++) {
					// 去掉字符串中的首尾单引号
					String barcode = barcodeArr[i].trim();
					barcode=barcode.substring(1, barcode.length() - 1);
					int count = 1;
					//拆分barcode类型为'ITEM000003-2'的情况
					String[] countSpli = barcode.split("-");
					if (countSpli.length<=1) {
						count=1;
					}else {
						count=Integer.parseInt(countSpli[1]);
					}
					//判断购买商品的条形码是否属于商店商品条形码数据
					if (allGoods.get(countSpli[0])==null) {
						throw new AddGoodsInCarInfoException(countSpli[0]);
					}else {
						String discountOption = "";
						//如果有满二减一
						if(dis2_1.contains(countSpli[0])){
							discountOption="BUY_TWO_GET_ONE_FREE";
						}else if(dis95.contains(countSpli[0])){
							//如果满足95折
							discountOption="FIVE_PERCENT_OFF";
						}else{
						}
						// 将购买商品添加到购物车
						shoppingCar.addGoods(allGoods.get(countSpli[0]),count,discountOption);
					}
					
				}
			} catch (Exception e) {
				if(e instanceof AddGoodsInCarInfoException){
					throw e;
				}else {
					throw new ShopCarInfoException();
					}
			}
		}
		// 结算购物车
		public String billCheck() throws Exception {
			//价格保留两位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			//商品的总价格（不使用优惠）
			Double totalPrice = 0.0;
			//商品的总价格（优惠后的）
			Double discountTotalPrice = 0.0;
					
			//拼接商品结算列表的barcode字符串
			StringBuffer sb = new StringBuffer();
			//获取购物车中的商品信息
			Map<String, GoodsInCar> mapInCar = shoppingCar.getShopCar();
				//定义购物车中满二送一优惠商品信息
				Map<String, GoodsInCar> discount2_1 = new HashMap<String,GoodsInCar>();
				//定义购物车中95折优惠商品信息
				Map<String, GoodsInCar> discount95 = new HashMap<String,GoodsInCar>();		
				Set setInCar = mapInCar.entrySet();
				Iterator it = setInCar.iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Entry) it.next();
					String key = (String) entry.getKey();
					//获取商品的数量、价格等信息
					GoodsInCar value = (GoodsInCar) entry.getValue();
					sb.append("名称：");
					sb.append(value.goods.getName());
					sb.append(",数量：");
					sb.append(value.count);
					sb.append(value.goods.getUnit());
					sb.append("，单价：");
					sb.append(value.goods.getPrice());
					sb.append("(元)，小计：");
					
					//根据折扣优惠信息，判断是否重新计算优惠商品的subprice
					if (value.discountOption.equals("BUY_TWO_GET_ONE_FREE")||value.discountOption.equals("FIVE_PERCENT_OFF")) {
						
						//折扣后的价格=原价格-优惠价
						//如果有满二送一的商品
						if(value.discountOption.equals("BUY_TWO_GET_ONE_FREE")){
							//计算实际需要买的件数
							/*
							int i=1;
							for(i=1;i<=value.count;i++){
								int j=i;
								if(i%2==0){
									j--;
								}
								if(j/2+i==value.count){
									break;
								}
							}*/
							int i=1;
							for(i=1;i<=value.count;i++){
								if(i/2+i<value.count){
									continue;
								}else{
									break;
								}
							}
							value.discountPrice =i*value.goods.getPrice();
							//标记满二送一的商品信息
							discount2_1.put(key, value);
						}else if(value.discountOption.equals("FIVE_PERCENT_OFF")){
							value.discountPrice =value.subPrice - 0.05*value.subPrice;
							//标记满95折商品信息
							discount95.put(key, value);
						}else{
							
						}
						totalPrice+=value.subPrice;
						discountTotalPrice +=value.discountPrice;
						/*名称：篮球，数量：2个，单价：98.00(元)，
						 * 小计：176.00(元)，优惠：10.00（元) 
						 * 名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：9.00(元) 
						 * 名称：羽毛球，数量：5个，单价：1.00(元)，小计：5.00(元) 
						 * 名称：苹果，数量：2斤，单价：5.50(元)，小计：11.00(元)")
						 */
						
						sb.append(df.format(value.discountPrice));
						sb.append("(元)，优惠：");
						sb.append(value.subPrice-value.discountPrice);
						
					}else {
						totalPrice+=value.subPrice;
						/*	名称：可口可乐，数量：3瓶，单价：3.00(元)，小计：9.00(元) 
						 * 	名称：羽毛球，数量：5个，单价：1.00(元)，小计：5.00(元) 
						 * 	名称：苹果，数量：2斤，单价：5.50(元)，小计：11.00(元)
						 */
						//表示未优惠，折扣后价格==原总价
						discountTotalPrice +=value.subPrice;
						sb.append(df.format(value.subPrice));
						sb.append("(元)");
					}
					sb.append("\n");
				}
//				sb.append("<div style='border-top:1px;border-color:black;'></div>");
				/*
				 * 总计：25.00(元)
				 */
				if ((discount2_1==null||discount2_1.size()<=0)&&(discount95==null||discount95.size()<=0)) {
					sb.append("总计：");
					sb.append(df.format(totalPrice));
					sb.append("元");
				}else {

//					sb.append("<div style='border-top:1px;border-color:black;'></div>");
					if(discount2_1.size()>0){
					sb.append("买二减一商品： ");
						//遍历有优惠的商品信息
						Set setInDicount = discount2_1.entrySet();
						Iterator itDis = setInDicount.iterator();
						while (itDis.hasNext()) {
							Map.Entry entry = (Entry) itDis.next();
							String key = (String) entry.getKey();
							//获取优惠商品的原总价、折扣价等信息
							GoodsInCar goodsInCar = (GoodsInCar) entry.getValue();
							/*
							 * 单品满100减10块商品： 篮球，原价：186.00(元)，优惠： 10.00(元)
							 * 总计：201.00(元) 节省：10.00(元)
							 */
							sb.append("\n");
							sb.append(goodsInCar.goods.getName());
							sb.append(",原价");
							sb.append(df.format(goodsInCar.subPrice));
							sb.append("(元),优惠");
							sb.append(goodsInCar.subPrice-goodsInCar.discountPrice);
							sb.append("元");
						}
					}
					if(discount95.size()>0){
						sb.append("\n");
						sb.append("95折商品： ");
						//遍历有优惠的商品信息
						Set setInDicount = discount95.entrySet();
						Iterator itDis = setInDicount.iterator();
						while (itDis.hasNext()) {
							Map.Entry entry = (Entry) itDis.next();
							String key = (String) entry.getKey();
							//获取优惠商品的原总价、折扣价等信息
							GoodsInCar goodsInCar = (GoodsInCar) entry.getValue();
							/*
							 * 单品满100减10块商品： 篮球，原价：186.00(元)，优惠： 10.00(元)
							 * 总计：201.00(元) 节省：10.00(元)
							 */
							sb.append("\n");
							sb.append(goodsInCar.goods.getName());
							sb.append(",原价");
							sb.append(df.format(goodsInCar.subPrice));
							sb.append("(元),优惠");
							sb.append(goodsInCar.subPrice-goodsInCar.discountPrice);
							sb.append("元");
						}
					}
					sb.append("\n");
					sb.append("总计：");
					sb.append(df.format(discountTotalPrice));
					sb.append("元 节省：");
					sb.append(totalPrice-discountTotalPrice);
					sb.append("元");
				}
			return sb.toString();
		}
}
