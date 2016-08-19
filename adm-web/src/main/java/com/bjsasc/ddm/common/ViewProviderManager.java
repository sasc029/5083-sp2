package com.bjsasc.ddm.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.bjsasc.plm.common.search.SearchViewProvider;
import com.bjsasc.plm.common.search.SearchViewProviderImpl;
import com.bjsasc.plm.core.util.ConfigFileUtil;

/**
 * ��view Config�ж�ȡ���е���ͼ�����ļ�ViewProvider
 * 
 * @author Administrator
 * 
 */
public class ViewProviderManager {

	private static ViewProviderManager manager = null;
	/**
	 * Key:�������ƣ���Document<br>
	 * value:��Ӧ��SearchViewProvider��ʵ��
	 */
	private Map<String, SearchViewProvider> types = null;

	private ViewProviderManager() {
		try {
			loadConfigs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	public static synchronized ViewProviderManager getInstance() {
		if (manager == null) {
			manager = new ViewProviderManager();
		}
		return manager;
	}

	/**
	 * �������͵����Ʒ���searchViewProvicerImpl����
	 * 
	 * @author ������ 2012-5-21 ����11:30:46
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	public SearchViewProvider getViewProviderByName(String type) {
		return types.get(type);
	}

	/**
	 * 
	 * �������е������ļ�
	 * 
	 * @author ������ 2012-5-21 ����11:31:49
	 * @throws IOException
	 * @throws JDOMException
	 */
	public void loadConfigs() throws Exception {
		if (types == null) {
			types = new HashMap<String, SearchViewProvider>();
		}
		SAXBuilder builder = new SAXBuilder(false);
				
		//�õ������ļ�����	
		List<File> files = ConfigFileUtil.listFiles("ddmsearch", ".xml");		
		for (File file:files) {
			InputStream is = new FileInputStream(file);
			Document doc = builder.build(is);
			Element root = doc.getRootElement();
			List<Element> ls = root.getChildren();
			for (Element pe : ls) {
				// �����
				String typeName = pe.getAttributeValue("typeName");
				String cnName = pe.getAttributeValue("CNName");
				String tableNames = pe.getAttributeValue("tableNames");
				String condition = pe.getAttributeValue("condition");

				SearchViewProviderImpl svpi = new SearchViewProviderImpl();

				svpi.setTypeName(typeName);
				svpi.setCNName(cnName);
				svpi.setTableNames(tableNames);
				svpi.setCondition(condition);

				List<Element> es = pe.getChildren();
				for (Element e : es) {
					String asname = e.getAttributeValue("name");
					String name = e.getText();
					svpi.setColumnParam(asname, name);
				}
				types.put(typeName, svpi);
			}
		}
	}

	/**
	 * 
	 * ��ȡ���е��������ͣ���Map<String,String>�����ͷ���<br>
	 * �磺{"Document","�ĵ�"}
	 * 
	 * @author ������ 2012-3-30 ����2:43:41
	 * @return
	 */
	public Map<String, String> getAllSearchTypes() {
		Map<String, String> curNames = new HashMap<String, String>();
		for (Map.Entry<String, SearchViewProvider> entry : types.entrySet()) {
			curNames.put(entry.getKey(), entry.getValue().getCNName());
		}
		return curNames;
	}

	/**
	 * 
	 * ��ȡ�������ĵĶ������������,��Ӣ������,�Ա����ڳ�ʼ��ʱ��ȡ,�ڳ�ʼ��ʱʹ��<br>
	 * �����ͬ���ĵ�������
	 * 
	 * @author ������ 2012-3-27 ����10:07:33
	 * @return {types:"Document,Baseline",cnName:"�ĵ�,����"}
	 */
	public Map<String, String> getSearchTypeNames() {
		StringBuffer sbType = new StringBuffer();
		StringBuffer sbCnName = new StringBuffer();
		boolean isFirst = true;
		for (Map.Entry<String, SearchViewProvider> entry : types.entrySet()) {
			if (isFirst == true) {
				isFirst = false;
			} else {
				sbType.append(",");
				sbCnName.append(",");
			}
			sbType.append(entry.getValue().getTypeName());
			sbCnName.append(entry.getValue().getCNName());
		}
		Map<String, String> result = new HashMap<String, String>();
		result.put("types", sbType.toString());
		result.put("cnName", sbCnName.toString());
		return result;
	}

	/**
	 * ��ȡ���Ͷ�Ӧ����������,�м���","����.�ڵ��������б���ʹ��
	 * 
	 * @author ������ 2012-3-30 ����2:40:57
	 * @param types
	 *            ���磺"Document,Baseline"
	 * @return
	 */
	public String getTypeNames(String typeNames) {
		StringBuffer sb = new StringBuffer();
		String[] ts = typeNames.split(",");
		boolean first = true;
		for (String t : ts) {
			if (getViewProviderByName(t) == null) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(" " + types.get(t).getCNName());
		}

		return sb.toString();
	}

	/**
	 * 
	 * ��typesList��������ȡ���е�JSONS
	 * 
	 * @author ������ 2012-3-30 ����2:39:20
	 * @return
	 */
	public String getTypesJson() {
		StringBuffer sb = new StringBuffer();
		// Map<String, String> curNames = names.get("search");
		Map<String, SearchViewProvider> cnList = types;
		boolean isFirst = true;
		sb.append("[");
		for (Map.Entry<String, SearchViewProvider> entry : cnList.entrySet()) {
			if (isFirst == true) {
				isFirst = false;
			} else {
				sb.append(",");
			}
			sb.append("{");
			sb.append("ID :\"" + entry.getKey() + "\",");
			sb.append("NAME : \"" + entry.getValue().getCNName() + "\"");
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}
}
