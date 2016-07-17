package cn.thoughtworks.exception;

/**
 * 	收银机扫码商店所有商品中不包含该条形码的异常处理
 * @author kl
 *
 */
public class AddGoodsInCarInfoException extends Exception{
	
	public AddGoodsInCarInfoException(String goodsInCar) {
		super(goodsInCar);
	}
}
