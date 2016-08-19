package com.bjsasc.adm.active.service.activechangeservice;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleEdge;
/**
 * 现行信息统计图中生成图片的方法实现类。
 * 
 * @author tudong 2013-06-08
 */
public class ActiveJfreeChartBase {
	
	private ActiveJfreeChartBase() {
		
	}
	
	/**
	 * 设置柱状图的颜色，背景，连接
	 * 
	 */
	public static JFreeChart createChart(CategoryDataset paramCategoryDataset,
			String yTitle, String title, String url, boolean byGroup) {
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 20));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋体", Font.BOLD, 12));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋体", Font.BOLD, 12));
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart jfreechart = ChartFactory.createBarChart3D(title, "",
				yTitle, paramCategoryDataset, PlotOrientation.VERTICAL, true,
				true, true);
		// 把图例显示在右侧
		jfreechart.getLegend().setVisible(byGroup);
		jfreechart.getLegend().setPosition(RectangleEdge.RIGHT);

		// 设置示例图背景颜色
		jfreechart.getLegend().setBackgroundPaint(Color.WHITE);
		// 图表背景色
		jfreechart.setBackgroundPaint(Color.WHITE);
		// 图表区域对象
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();

		// 设置坐标不显示小数点1
		// NumberAxis na= (NumberAxis)categoryplot.getRangeAxis();
		// na.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// 数据区的背景透明度（0.0～1.0）,白色的就无关了
		categoryplot.setBackgroundAlpha(0.8f);
		categoryplot.setBackgroundPaint(Color.WHITE);
		// 横轴
		CategoryAxis domainAxis = categoryplot.getDomainAxis();
		// 水平底部列表
		domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 16));
		// 水平底部标题
		domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 14));
		domainAxis.setCategoryMargin(0.05);
		categoryplot.setDomainAxis(0, domainAxis);
		// 纵轴
		ValueAxis rangeAxis = categoryplot.getRangeAxis();
		// 设置坐标不显示小数点2
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// 设置最高的一个 Item 与图片顶端的距离
		rangeAxis.setUpperMargin(0.15);
		// 设置最低的一个 Item 与图片底端的距离
		rangeAxis.setLowerMargin(0.05);

		rangeAxis.setPositiveArrowVisible(true);
		categoryplot.setRangeAxis(rangeAxis);
		// 设置数据区的表示者
		BarRenderer renderer = new BarRenderer();

		// 设置连接
		if (!"".equals(url)) {
			renderer.setBaseItemURLGenerator(new StandardCategoryURLGenerator(
					url));
			// jfreechart.getLegend().setVisible(true);
		}
		// 设置倾斜
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();

		// ValueAxis v = categoryplot.getRangeAxis();
		// 设置纵坐标标题旋转角度
		categoryaxis.setLabelAngle(0.6);
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

		// 设置每个柱的颜色
		renderer.setSeriesPaint(0, new Color(0, 128, 255));
		// renderer.setSeriesPaint(1, Color.red);
		// renderer.setSeriesPaint(2, Color.blue);
		//
		// renderer.setBaseFillPaint(Color.white);
		// 设置平行柱的之间距离
		renderer.setItemMargin(-0.001D);
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setMaximumBarWidth(0.03);// 每个BAR的最大宽度
		renderer.setMinimumBarLength(0.03);
		// //设置每个柱border
		// renderer.setDrawBarOutline(true);
		// renderer.setOutlinePaint(Color.BLACK);
		// 显示每个柱的数值
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setItemLabelFont(new Font("宋体", Font.PLAIN, 16));
		renderer.setItemLabelsVisible(true);
		// 给数据区增加数据区的表示者
		categoryplot.setRenderer(renderer);
		// 设置Plot，不显示所有网格
		categoryplot.setOutlinePaint(null);
		categoryplot.setDomainGridlinesVisible(false);
		categoryplot.setRangeGridlinesVisible(false);
		return jfreechart;
	}
	/**
	 * 设置每个柱子的值
	 * 
	 */
	public static DefaultCategoryDataset getDatasetByGroup(
			List<Map<String, Object>> getStatResultData) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		List<String> listkey = new ArrayList<String>();
		Set keys = getStatResultData.get(0).keySet();
		if (keys != null) {
			Iterator iterator = keys.iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				if (!key.toString().equals("name")) {
					listkey.add(key.toString());
				}
			}
		}
		for (int i = 0; i < getStatResultData.size(); i++) {
			for (int j = 0; j < listkey.size(); j++) {
				int num = Integer.parseInt((getStatResultData.get(i)
						.get(listkey.get(j))).toString());
				dataset.addValue(num, getStatResultData.get(i).get("name")
						.toString(), listkey.get(j));
			}
		}
		return dataset;
	}

}
