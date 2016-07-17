package cn.thoughtworks.model;
/**
 * 促销信息
 * @author 
 * @date 2016年7月17日 下午6:29:07
 */
public class Discount {
	private String[] barcodes;//优惠商品条形码
	private String type;//优惠类型
	public String[] getBarcodes() {
		return barcodes;
	}
	public void setBarcodes(String[] barcodes) {
		this.barcodes = barcodes;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
