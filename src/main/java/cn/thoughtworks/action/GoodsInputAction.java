package cn.thoughtworks.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.thoughtworks.exception.AddGoodsInCarInfoException;
import cn.thoughtworks.exception.DiscountInfoException;
import cn.thoughtworks.exception.GoodsInfoException;
import cn.thoughtworks.exception.ShopCarInfoException;
import cn.thoughtworks.service.BusinessService;

/**
 * 输入购物车商品信息，开始结算
 */
public class GoodsInputAction extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	//同步操作synchronized：控制并发，多台电脑进行商品入库时的同步操作，避免重复添加
	protected synchronized void  doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		BusinessService goodsService = new BusinessService();

		// 获取购物车商品数据
		String jsonGoods = request.getParameter("jsonGoods");

		String infoShopCar = request.getParameter("infoShopCar");
		String infoDiscount = request.getParameter("infoDiscount");
		try {
			goodsService.initGoods(jsonGoods);
			goodsService.initDiscount(infoDiscount);
			goodsService.initShopCar(infoShopCar);
			
			// System.out.println();
			// 设置打印购物清单
			request.setAttribute("goodsBill", goodsService.billCheck());

		} catch (Exception e) {
			// e.printStackTrace();
			if (e instanceof GoodsInfoException) {
				request.setAttribute("error", "商品入库信息输入格式有误");
			} else if (e instanceof ShopCarInfoException) {
				request.setAttribute("error", "购物车扫码信息输入格式有误");
			} else if (e instanceof DiscountInfoException) {
				request.setAttribute("error", "优惠信息输入格式有误");
			} else if (e instanceof AddGoodsInCarInfoException) {
				request.setAttribute("error", "购物车中包含非法条形码"+e.getMessage()+"的商品,不属于本商店商品或没有进行扫描入库操作！");
				
			}else{
				request.setAttribute("error", "系统错误");
			}
			request.getRequestDispatcher("/goodsInput.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/goodsInput.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
