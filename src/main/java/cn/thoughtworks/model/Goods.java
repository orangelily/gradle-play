package cn.thoughtworks.model;
/**
 * 单件商品信息
 * @author 
 * @date 2016年7月17日 下午5:56:49
 */
public class Goods {
	
	public Goods(){
		
	}
	public Goods(String barcode,String name, String unit,String category,String subCategory,Double price){
		this.barcode = barcode;
		this.name=name;
		this.unit=unit;
		this.category=category;
		this.subCategory=subCategory;
		this.price = price;
	}
	
	private String barcode;//条形码
	private String name;//名称
	private String unit;//单位
	private String category;//类别
	private String subCategory;//子类别
	private Double price;//单价
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
