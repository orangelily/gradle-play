package cn.thoughtworks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import cn.thoughtworks.exception.AddGoodsInCarInfoException;
import cn.thoughtworks.exception.DiscountInfoException;
import cn.thoughtworks.exception.GoodsInfoException;
import cn.thoughtworks.exception.ShopCarInfoException;
import cn.thoughtworks.service.BusinessService;


public class Main {

	public static void main(String[] args) throws Exception {
		//所有的商品
		String allGoods = readFile("/cn/thoughtworks/Goods.json", "UTF-8");
		//折扣信息
		String discount = readFile("/cn/thoughtworks/Discount.json", "UTF-8");
		//System.out.println(discount);
		//控制台读入条码ctrl+z结束
		/**
		 * [ 'ITEM000001-100', 'ITEM000001', 
		 * 'ITEM000001', 'ITEM000001', 'ITEM000001', 'ITEM000003-2', 
		 * 'ITEM000005', 'ITEM000005', 'ITEM000005' ]
		 */
		StringBuffer shoppingCar = new StringBuffer();
		System.out.println("按照如下形式输入条形码："+"\n[ 'ITEM000001-100',"+"\n"
		                                    +"'ITEM000001'," +"\n"
		 +"'ITEM000001',"+"\n"
		 +"'ITEM000001'," +"\n"
		 +"'ITEM000001'," +"\n"
		 +"'ITEM000003-2'," +"\n"
		 +"'ITEM000005' ]");
		System.out.flush();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			shoppingCar.append(scanner.nextLine());
		}
		BusinessService goodsService = new BusinessService();
		try {
			goodsService.initGoods(allGoods);
			goodsService.initDiscount(discount);
			goodsService.initShopCar(shoppingCar.toString());
			// 设置打印购物清单
			System.out.println(goodsService.billCheck());

		} catch (Exception e) {
			// e.printStackTrace();
			if (e instanceof GoodsInfoException) {
				System.out.println("商品入库信息输入格式有误");
			} else if (e instanceof ShopCarInfoException) {
				System.out.println("购物车扫码信息输入格式有误");
			} else if (e instanceof DiscountInfoException) {
				System.out.println("优惠信息输入格式有误");
			} else if (e instanceof AddGoodsInCarInfoException) {
				System.out.println("购物车中包含非法条形码"+e.getMessage()+"的商品,不属于本商店商品或没有进行扫描入库操作！");
				
			}else{
				System.out.println("系统错误");
			}
			return;
		}
	}

	public static String readFile(String path,String charset){
		String str = "";
		InputStream stream = Main.class.getResourceAsStream(path);
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStreamReader = new InputStreamReader(stream, charset);
			bufferedReader = new BufferedReader(inputStreamReader);
			String lineTXT = null;
			while ((lineTXT = bufferedReader.readLine()) != null) {
				str += lineTXT.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					//关闭bufferreader
					bufferedReader.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				try {
					if (inputStreamReader != null) {
						//关闭inputStreamReader
						inputStreamReader.close();
					}
				} catch (Exception e3) {
					e3.printStackTrace();
				} finally {
					try {
						//关闭InputStream
						stream.close();
					} catch (Exception e4) {
						e4.printStackTrace();
					}
				}

			}

		}
		return str;
	}

}
