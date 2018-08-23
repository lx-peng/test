package cn.itcast.demo;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class POIDemo {

	public static void main(String[] args) throws Exception{
		//创建excel工作簿
		HSSFWorkbook wk = new HSSFWorkbook();
		//创建一张工作表
		HSSFSheet sheet = wk.createSheet("我的工作表");
		//创建第一行
		HSSFRow row = sheet.createRow(0);
		//创建第一行中的第一个单元格
		HSSFCell cell=row.createCell(0);
		//设置列宽度
		sheet.setColumnWidth(0, 5000);
		//向单元格写值
		cell.setCellValue("测试");
		//保存到本地目录
		wk.write(new FileOutputStream(new File("d:\\poitest.xls")));
		//关闭工作簿
		wk.close();
	}
}
