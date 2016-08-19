package com.bjsasc.ddm.distribute.service.commonsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bjsasc.ddm.common.ConstUtil;
import com.bjsasc.plm.core.util.ExcelUtil;
import com.bjsasc.plm.core.util.StringUtil;
import com.bjsasc.plm.grid.GridColumn;
import com.bjsasc.plm.grid.GridUtil;
import com.bjsasc.plm.grid.spot.Column;
import com.bjsasc.plm.grid.spot.Spot;
import com.bjsasc.plm.grid.spot.SpotUtil;

/**
 * �ۺϲ�ѯ����ʵ���ࡣ
 * 
 * @author gengancong 2013-2-22
 */
public class CommonSearchServiceImpl implements CommonSearchService {

	/* ���� Javadoc��
	 * @see com.bjsasc.ddm.distribute.service.commonsearch.CommonSearchService#getExcelObject(java.util.List)
	 */
	public HSSFWorkbook getExcelObject(List<Map<String, Object>> result) {

		String spotId = ConstUtil.SPOT_LISTDISTRIBUTECOMMONSEARCH;
		String spotInstance = ConstUtil.SPOT_LISTDISTRIBUTECOMMONSEARCH;
		Spot spot = SpotUtil.getSpot(spotId);
		List<GridColumn> columns = GridUtil.getColumns(spotId, spotInstance);

		List<String> headers = new ArrayList<String>();

		try {
			ExcelUtil ew = new ExcelUtil();

			String sheetName = "�ۺϲ�ѯ";

			ew.init(sheetName);

			// ������Ԫ����ʽ
			HSSFCellStyle headStyle = ew.getCellStyle((short) 200, true, false, true);
			HSSFCellStyle detailStyle = ew.getCellStyle((short) 200, true, false, false);

			int columnSize = columns.size();
			int rowSize = result.size();
			ew.createRow(rowSize + 1, columnSize + 1);
			ew.setRowHeight(1, (short) 300);

			ew.setCell(1, 1, "No.", headStyle);
			ew.setColumnWidth(1, 1500);
			ew.setColumnWidth(2, 3700);
			for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
				GridColumn column = columns.get(columnIndex);
				String columnId = column.getAttrId();

				// int width = column.getWidth();

				String name = column.getAttrName();

				// �����Զ�����Ϣ���������ơ���ȡ��༭
				Column customColumn = spot.getCustomColumns().get(columnId);
				if (customColumn != null && customColumn.getColumnName() != null) {
					name = customColumn.getColumnName();
				}
				headers.add(columnId);

				ew.setCell(1, columnIndex + 2, name, headStyle);
				ew.setColumnWidth(columnIndex + 2, 3700);
			}

			for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
				ew.setCell(rowIndex + 2, 1, rowIndex + 1, detailStyle);
				Map<String, Object> map = result.get(rowIndex);
				// ���������ȡObject��������ֵ
				Object[] arrStr = new Object[columnSize];
				for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
					String key = headers.get(columnIndex);
					Object object = map.get(key);
					String cellValue = "";
					if (!StringUtil.isNull(object)) {
						cellValue = object.toString();
						if (StringUtil.isHtmlTag(cellValue)) {
							cellValue = StringUtil.delHtmlTag(cellValue);
						}
					}

					//��ȡ��Ԫ��ֵ
					arrStr[columnIndex] = StringUtil.getString(cellValue);

					ew.setCell(rowIndex + 2, columnIndex + 2, cellValue, detailStyle);
				}

				// ����,��ȡ��������ֵ
				String maxLength = StringUtil.maxValue(arrStr);

				// ���ݳ�������ַ������õ�Ԫ������߶�
				ew.setCellHeightStyle(rowIndex + 2, 3, maxLength, 7);
			}

			// ���ô�ӡ��ʽ
			ew.setPrintStyle(true);
			ew.close();

			return ew.getCurrentWorkbook();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
