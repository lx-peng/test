package cn.itcast.demo;

import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class JfreeChartDemo {

	public static void main(String[] args) throws Exception{
		System.out.println("-------------------------------------------");
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("家电", 10086);
		dataset.setValue("百货", 234);
		dataset.setValue("食品", 8903);
		//参数 1 title 标题
		//参数2 dataset 数据集
		//参数3  是否开启图例
		//参数4 是否开启工具栏
		//参数5 是否开启url跳转
		//创建一张饼图
		JFreeChart chart = ChartFactory.createPieChart("标题", dataset, true, false, false);
		//参数1 生成图片文件到本地
		//参数2 chart对象
		//参数3 图片宽度
		//参数4图片高度
		ChartUtilities.saveChartAsJPEG(new File("d:\\chartDemo.jpeg"), chart, 400, 300);
	}
}
