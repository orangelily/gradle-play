<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收银机打印小票</title>
</head>
<body>
	<h1 style="text-align: center;">收银机 -买二送一&95折优惠</h1>
	<form action="${pageContext.request.contextPath }/GoodsInputAction" method="post">
		<table style="margin: 0 auto;">
			<tr>
				<td>商店所有商品信息：</td>
				<td><textarea rows="5" cols="100" name="jsonGoods"><c:if test="${empty param.jsonGoods}">[{ barcode: 'ITEM000001', name: '可口可乐1', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 13.00 },
{ barcode: 'ITEM000002', name: '可口可乐2', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 12.00 },
{ barcode: 'ITEM000003', name: '可口可乐3', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 7.30 },
{ barcode: 'ITEM000004', name: '可口可乐4', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 5.10 },
{ barcode: 'ITEM000005', name: '可口可乐5', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 5.20 }]</c:if><c:if test="${not empty param.jsonGoods}">${param.jsonGoods}
				</c:if></textarea>
		</td>
			</tr>
			<tr>

				<td> 购物车商品条形码：</td>
				<td><textarea rows="5" cols="100" name="infoShopCar"><c:if test="${empty param.infoShopCar}">[ 'ITEM000001-100', 'ITEM000001', 'ITEM000001', 'ITEM000001', 'ITEM000001', 'ITEM000003-2', 'ITEM000005', 'ITEM000005', 'ITEM000005' ]</c:if><c:if test="${not empty param.infoShopCar}">${param.infoShopCar}
				</c:if></textarea>
		</td>
			</tr>

			<tr>
				<td>优惠促销信息：</td>
				<td><textarea  rows="5" cols="100" name="infoDiscount"><c:if test="${empty param.infoDiscount}">[{ type: 'FIVE_PERCENT_OFF', barcodes: [ 'ITEM000005', 'ITEM000001' ] } ,{ type: 'BUY_TWO_GET_ONE_FREE', barcodes: [  'ITEM000004', 'ITEM000003' ,'ITEM000001' ] }  ]</c:if><c:if test="${not empty param.infoDiscount}">${param.infoDiscount}
				</c:if></textarea>
		</td>
			</tr>
			<tr><td colspan="2" style="text-align: center;"><div><label style="color:red;">${error}</label></div><input type="submit" value="商品结算"></td></tr>
		</table>
		
		<pre style="margin:0 auto;width:500px;"><%
			Object goodsBill = request.getAttribute("goodsBill");
			if (goodsBill != null) {
				out.write("<h2 style='text-align: center;'>购物清单</h2>*<没钱赚商店>购物清单*\n"+request.getAttribute("goodsBill").toString());
			}
		%></pre>
	</form>
</body>
</html>