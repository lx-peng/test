package cn.itcast.erp.biz.impl;


import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import cn.itcast.erp.ERPException;
import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
/**
 * 订单业务逻辑类
 * @author Administrator
 *
 */
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {
	private IEmpDao empDao;
	private ISupplierDao supplierDao;
	private IOrdersDao ordersDao;
	
	public void setOrdersDao(IOrdersDao ordersDao) {
		this.ordersDao = ordersDao;
		super.setBaseDao(this.ordersDao);
	}
	
	public IEmpDao getEmpDao() {
		return empDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public ISupplierDao getSupplierDao() {
		return supplierDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	//新增订单
	public void add(Orders orders){
		//新增的采购订单都是未审核
		orders.setState(Orders.STATE_CREATE);
		//设置订单类型未采购
		/*orders.setType(Orders.TYPE_IN);*/
		//设置订单创建时间未当前时间
		orders.setCreatetime(new Date());
		//计算总金额
		double total=0;
		for (Orderdetail detail : orders.getOrderDetails()) {
			total+=detail.getMoney();
			//设置明细的状态为未入库
			detail.setState(Orderdetail.STATE_NOT_IN);
			//设置明细对应的订单，原因，orders采用级联更新，且外键的维护交给明细来维护
			detail.setOrders(orders);
		}
		//设置总金额
		orders.setTotalmoney(total);
		//保存订单
		ordersDao.add(orders);
	}
	//重写listbypage方法
	public List<Orders> getListByPage(Orders t1,Orders t2,Object param,int firstResult,int maxResults){
		//获取分页结果
		List<Orders> orderList = super.getListByPage(t1, t2, param, firstResult, maxResults);
		//缓存员工名称，key为员工的编号，value为员工的姓名
		Map<Long,String> empNameMap=new HashMap<Long,String>();
		for (Orders o : orderList) {
			//设置下单员名称
			o.setCreaterName(getEmpName(o.getCreater(),empNameMap));
			o.setCheckName(getEmpName(o.getChecker(),empNameMap));
			o.setStarterName(getEmpName(o.getStarter(),empNameMap));
			o.setEnderName(getEmpName(o.getEnder(),empNameMap));
			o.setSupplierName(getEmpName(o.getSupplieruuid(),empNameMap));
		}
		return super.getListByPage(t1, t2, param, firstResult, maxResults);
	}
	//根据员工编号获取员工名称
	private String getEmpName(Long uuid,Map<Long,String> empNameMap){
		//如果员工编号为空，则返回空
		if(null==uuid){
			return null;
		}
		//通过员工id,尝试从缓存中获取员工名称
		String empName=empNameMap.get(uuid);
		if(null==empName){
			//如果在缓存中没有找到，则调用dao查询后，获取到员工名称
			if(null!=empDao && null!=empDao.get(uuid)){
				empName=empDao.get(uuid).getName();
			}
			//按员工编号保存员工名称到缓存中
			empNameMap.put(uuid, empName);
		}
		return empName;
	}
	//采购订单审核
	@Override
	public void doCheck(Long uuid, Long empuuid) {
		//获取订单信息
		Orders orders = ordersDao.get(uuid);
		//检查订单的状态是否为未审核
		if(!Orders.STATE_CREATE.equals(orders.getState())){
			throw new ERPException("亲！该订单已经审核过了");
		}
		//更新审核员
		orders.setChecker(empuuid);
		//更新审核时间
		orders.setChecktime(new Date());
		//更新订单状态为已审核
		orders.setState(Orders.STATE_CHECK);
	}
	//采购订单确认
	@Override
	public void doStart(Long uuid, Long empuuid) {
		//获取订单信息
		Orders orders = ordersDao.get(uuid);
		//检查订单的状态是否为未审核
		if(!Orders.STATE_CHECK.equals(orders.getState())){
			throw new ERPException("亲！该订单已经确认过了");
		}
		//更新确认员
		orders.setStarter(empuuid);
		//更新确认时间
		orders.setStarttime(new Date());
		//更新订单状态为已审核
		orders.setState(Orders.STATE_START);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void exportById(OutputStream os, Long uuid) throws Exception {
		//获取订单信息
				Orders orders = ordersDao.get(uuid);
		//工作簿
				HSSFWorkbook book = new HSSFWorkbook();
				//工作表
				HSSFSheet sheet = book.createSheet("采购订单");
				//内容样式
				HSSFCellStyle style_content=book.createCellStyle();
				style_content.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
				style_content.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
				style_content.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
				style_content.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
				//根据导出的订单样本创建10行4列
				//定义总共要创建的行数量，要包含明细列表的size
				int rowCnt = 10 + orders.getOrderDetails().size();
				for (int i = 2; i < rowCnt; i++) {
					HSSFRow row = sheet.createRow(i);//创建订单内容中的行
					for (int j = 0; j < 4; j++) {
						HSSFCell cell = row.createCell(j);//创建订单内容中单元格
						cell.setCellStyle(style_content);//设置单元格的样式
					}
				}
				//合并单元格
				sheet.addMergedRegion(new CellRangeAddress(0,0,0,3));//标题，第一行合并第一到第四列
				sheet.addMergedRegion(new CellRangeAddress(2,2,1,3));//供应商，第3行合并第2到第四列
				sheet.addMergedRegion(new CellRangeAddress(7,7,0,3));//订单明细，第8行合并第一到第四列
				//设置固定文本内容
				//设置标题内容，注意：单元格必须创建后才能设置
				sheet.createRow(0).createCell(0).setCellValue("采购单");//设置标题内容
				sheet.getRow(2).getCell(0).setCellValue("供应商");//设置供应商文本
				//已经创建过的row/cell，则通过sheet.getRow.getCell方式获取
				sheet.getRow(3).getCell(0).setCellValue("下单日期");
				sheet.getRow(3).getCell(2).setCellValue("经办人");
				sheet.getRow(4).getCell(0).setCellValue("审核日期");
				sheet.getRow(4).getCell(2).setCellValue("经办人");
				sheet.getRow(5).getCell(0).setCellValue("采购日期");
				sheet.getRow(5).getCell(2).setCellValue("经办人");
				sheet.getRow(6).getCell(0).setCellValue("入库日期");
				sheet.getRow(6).getCell(2).setCellValue("经办人");
				sheet.getRow(7).getCell(0).setCellValue("订单明细");
				sheet.getRow(8).getCell(0).setCellValue("商品名称");
				sheet.getRow(8).getCell(1).setCellValue("数量");
				sheet.getRow(8).getCell(2).setCellValue("价格");
				sheet.getRow(8).getCell(3).setCellValue("金额");
				//设置行高和列宽
				sheet.getRow(0).setHeight((short)1000);//设置标题行高
				//设置内容部分的行高
				for (int i = 2; i < 12; i++) {
					sheet.getRow(i).setHeight((short)500);
				}
				//设置列宽
				for(int i=0;i<4;i++){
					sheet.setColumnWidth(i, 5000);
				}
				//设置对齐方式和自提
				//内容部分的对齐设置
				style_content.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
				style_content.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
				//设置内容部分的字体
				HSSFFont font_content = book.createFont();//创建字体
				font_content.setFontName("宋体");//设置字体名称
				font_content.setFontHeightInPoints((short)11);//设置字体大小
				style_content.setFont(font_content);//设置样式的字体
				
				//标题样式
				HSSFCellStyle style_title=book.createCellStyle();
				style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style_title.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				HSSFFont font_title=book.createFont();//创建字体
				font_title.setFontName("黑体");
				font_title.setBold(true);
				font_title.setFontHeightInPoints((short)18);
				style_title.setFont(font_title);//设置样式的字体
				sheet.getRow(0).getCell(0).setCellStyle(style_title);//设置标题样式
				//日期格式设置
				HSSFCellStyle style_date=book.createCellStyle();
				style_date.cloneStyleFrom(style_content);//日期格式基本上和内容的格式一样
				HSSFDataFormat dataFormat = book.createDataFormat();
				style_date.setDataFormat(dataFormat.getFormat("yyyy-MM-dd hh:mm"));
				//设置日期的日期格式
				for(int i=3;i<7;i++){
					sheet.getRow(i).getCell(1).setCellStyle(style_date);
				}
				//设置供应商的值
				sheet.getRow(2).getCell(1).setCellValue(supplierDao.get(orders.getSupplieruuid()).getName());
				//设置日期内容
				if(null!=orders.getCreatetime()){
					sheet.getRow(3).getCell(1).setCellValue(orders.getCreatetime());
				}
				if(null!=orders.getChecktime()){
					sheet.getRow(4).getCell(1).setCellValue(orders.getChecktime());
				}
				if(null!=orders.getStarttime()){
					sheet.getRow(5).getCell(1).setCellValue(orders.getStarttime());
				}
				if(null!=orders.getEndtime()){
					sheet.getRow(6).getCell(1).setCellValue(orders.getEndtime());
				}
				//设置经办人的值
				if(null!=orders.getCreater()){
					sheet.getRow(3).getCell(3).setCellValue(empDao.get(orders.getCreater()).getName());
				}
				if(null!=orders.getChecker()){
					sheet.getRow(4).getCell(3).setCellValue(empDao.get(orders.getChecker()).getName());
				}
				if(null!=orders.getStarter()){
					sheet.getRow(5).getCell(3).setCellValue(empDao.get(orders.getStarter()).getName());
				}
				if(null!=orders.getEnder()){//库管员
					sheet.getRow(6).getCell(3).setCellValue(empDao.get(orders.getEnder()).getName());
				}
				//订单明细
				int rowIndex=9;
				HSSFRow row=null;
				for (Orderdetail od : orders.getOrderDetails()) {
					row=sheet.getRow(rowIndex);
					row.getCell(0).setCellValue(od.getGoodsname());
					row.getCell(1).setCellValue(od.getNum());
					row.getCell(2).setCellValue(od.getPrice());
					row.getCell(3).setCellValue(od.getMoney());
					rowIndex++;
				}
				//合计
				sheet.getRow(rowIndex).getCell(0).setCellValue("合计");
				sheet.getRow(rowIndex).getCell(3).setCellValue(orders.getTotalmoney());
				//保存工作簿到本地目录
				book.write(os);
				book.close();
	}
}
