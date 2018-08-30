package cn.itcast.erp.biz.impl;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;
/**
 * 供应商业务逻辑类
 * @author Administrator
 *
 */
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

	private ISupplierDao supplierDao;
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
		super.setBaseDao(this.supplierDao);
	}
	/**
	 * 导出到excel文件
	 */
	@Override
	public void export(OutputStream os, Supplier t1) {
		//根据查询条件获取供应商/客户列表
		List<Supplier> supplierList = super.getList(t1, null, null);
		//创建excel工作簿
		HSSFWorkbook wk = new HSSFWorkbook();
		HSSFSheet sheet = null;
		//根据查询条件中的类型来创建响应名称的工作表
		if("1".equals(t1.getType())){
			sheet=wk.createSheet("供应商");
		}if("2".equals(t1.getType())){
			sheet=wk.createSheet("客户");
		}
		//写入表头
		HSSFRow row= sheet.createRow(0);
		//定义好每一列的标题
		String[] headerNames = {"名称","地址","联系人","电话","Email"};
		//指定每一列的宽度
		int[] columnWidths = {4000,8000,2000,3000,8000};
		HSSFCell cell=null;
		for (int i = 0; i < headerNames.length; i++) {
			cell=row.createCell(i);
			cell.setCellValue(headerNames[i]);
			sheet.setColumnWidth(i, columnWidths[i]);
		}
		//写入内容
		int i=1;
		for (Supplier supplier : supplierList) {
			row=sheet.createRow(i);
			//必须按照headerNames的顺序来
			row.createCell(0).setCellValue(supplier.getName());//名称
			row.createCell(1).setCellValue(supplier.getAddress());//地址
			row.createCell(2).setCellValue(supplier.getContact());//联系人
			row.createCell(3).setCellValue(supplier.getTele());//联系电话
			row.createCell(4).setCellValue(supplier.getEmail());//邮件地址
			i++;
		}
		//写入到输出流中
		try {
			wk.write(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				wk.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void doImport(InputStream is)throws IOException{
		HSSFWorkbook wb=null;
		try {
			wb=new HSSFWorkbook(is);
			HSSFSheet sheet = wb.getSheetAt(0);
			String type="";
			if("供应商".equals(sheet.getSheetName())){
				type=Supplier.TYPE_SUPPLIER;
			}else if("客户".equals(sheet.getSheetName())){
				type=Supplier.TYPE_CUSTOMER;
			}else{
				throw new ErpException("工作表名称不正确");
			}
			//读取数据
			//最后一行的行号
			int lastRow = sheet.getLastRowNum();
			Supplier supplier=null;
			for (int i = 0; i < lastRow; i++) {
				for(int j=0;j<5;j++){
					sheet.getRow(i).getCell(j).setCellType(CellType.STRING);
				}
				//设置单元格类型
				supplier = new Supplier();
				supplier.setName(sheet.getRow(i).getCell(0).getStringCellValue());//供应商名称
				//判断是否存在，通过名称来判断
				List<Supplier> list = supplierDao.getList(null, supplier, null);
				if(list.size()>0){
					supplier=list.get(0);
				}
				supplier.setAddress(sheet.getRow(i).getCell(1).getStringCellValue());//地址
				supplier.setContact(sheet.getRow(i).getCell(2).getStringCellValue());//联系人
				supplier.setTele(sheet.getRow(i).getCell(3).getStringCellValue());//电话
				supplier.setEmail(sheet.getRow(i).getCell(4).getStringCellValue());//Email
				if(list.size() == 0){
					//新增
					supplier.setType(type);
					supplierDao.add(supplier);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null!=wb){
				try {
					wb.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
