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
 * ������Ϣͳ��ͼ������ͼƬ�ķ���ʵ���ࡣ
 * 
 * @author tudong 2013-06-08
 */
public class ActiveJfreeChartBase {
	
	private ActiveJfreeChartBase() {
		
	}
	
	/**
	 * ������״ͼ����ɫ������������
	 * 
	 */
	public static JFreeChart createChart(CategoryDataset paramCategoryDataset,
			String yTitle, String title, String url, boolean byGroup) {
		// ����������ʽ
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// ���ñ�������
		standardChartTheme.setExtraLargeFont(new Font("����", Font.BOLD, 20));
		// ����ͼ��������
		standardChartTheme.setRegularFont(new Font("����", Font.BOLD, 12));
		// �������������
		standardChartTheme.setLargeFont(new Font("����", Font.BOLD, 12));
		// Ӧ��������ʽ
		ChartFactory.setChartTheme(standardChartTheme);
		JFreeChart jfreechart = ChartFactory.createBarChart3D(title, "",
				yTitle, paramCategoryDataset, PlotOrientation.VERTICAL, true,
				true, true);
		// ��ͼ����ʾ���Ҳ�
		jfreechart.getLegend().setVisible(byGroup);
		jfreechart.getLegend().setPosition(RectangleEdge.RIGHT);

		// ����ʾ��ͼ������ɫ
		jfreechart.getLegend().setBackgroundPaint(Color.WHITE);
		// ͼ����ɫ
		jfreechart.setBackgroundPaint(Color.WHITE);
		// ͼ���������
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();

		// �������겻��ʾС����1
		// NumberAxis na= (NumberAxis)categoryplot.getRangeAxis();
		// na.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// �������ı���͸���ȣ�0.0��1.0��,��ɫ�ľ��޹���
		categoryplot.setBackgroundAlpha(0.8f);
		categoryplot.setBackgroundPaint(Color.WHITE);
		// ����
		CategoryAxis domainAxis = categoryplot.getDomainAxis();
		// ˮƽ�ײ��б�
		domainAxis.setLabelFont(new Font("����", Font.PLAIN, 16));
		// ˮƽ�ײ�����
		domainAxis.setTickLabelFont(new Font("����", Font.BOLD, 14));
		domainAxis.setCategoryMargin(0.05);
		categoryplot.setDomainAxis(0, domainAxis);
		// ����
		ValueAxis rangeAxis = categoryplot.getRangeAxis();
		// �������겻��ʾС����2
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// ������ߵ�һ�� Item ��ͼƬ���˵ľ���
		rangeAxis.setUpperMargin(0.15);
		// ������͵�һ�� Item ��ͼƬ�׶˵ľ���
		rangeAxis.setLowerMargin(0.05);

		rangeAxis.setPositiveArrowVisible(true);
		categoryplot.setRangeAxis(rangeAxis);
		// �����������ı�ʾ��
		BarRenderer renderer = new BarRenderer();

		// ��������
		if (!"".equals(url)) {
			renderer.setBaseItemURLGenerator(new StandardCategoryURLGenerator(
					url));
			// jfreechart.getLegend().setVisible(true);
		}
		// ������б
		CategoryAxis categoryaxis = categoryplot.getDomainAxis();

		// ValueAxis v = categoryplot.getRangeAxis();
		// ���������������ת�Ƕ�
		categoryaxis.setLabelAngle(0.6);
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

		// ����ÿ��������ɫ
		renderer.setSeriesPaint(0, new Color(0, 128, 255));
		// renderer.setSeriesPaint(1, Color.red);
		// renderer.setSeriesPaint(2, Color.blue);
		//
		// renderer.setBaseFillPaint(Color.white);
		// ����ƽ������֮�����
		renderer.setItemMargin(-0.001D);
		// ��ʾÿ��������ֵ�����޸ĸ���ֵ����������
		renderer.setMaximumBarWidth(0.03);// ÿ��BAR�������
		renderer.setMinimumBarLength(0.03);
		// //����ÿ����border
		// renderer.setDrawBarOutline(true);
		// renderer.setOutlinePaint(Color.BLACK);
		// ��ʾÿ��������ֵ
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setItemLabelFont(new Font("����", Font.PLAIN, 16));
		renderer.setItemLabelsVisible(true);
		// �������������������ı�ʾ��
		categoryplot.setRenderer(renderer);
		// ����Plot������ʾ��������
		categoryplot.setOutlinePaint(null);
		categoryplot.setDomainGridlinesVisible(false);
		categoryplot.setRangeGridlinesVisible(false);
		return jfreechart;
	}
	/**
	 * ����ÿ�����ӵ�ֵ
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
