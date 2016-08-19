package com.bjsasc.adm.active.model.activeset;

import com.bjsasc.adm.active.model.ActiveBase;
import com.bjsasc.adm.active.model.ActiveObjSet;
import com.bjsasc.adm.active.model.ActiveOrdered;
import com.bjsasc.adm.active.model.AdmMaster;
import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.type.TypeDefinition;
import com.bjsasc.plm.core.type.TypeHelper;

/**
 * ������ģ�͡�
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveSet extends ActiveBase implements Recycledable, ActiveObjSet, ActiveOrdered {

	/** serialVersionUID */
	private static final long serialVersionUID = -6509148365523073600L;

	public static final String CLASSID = ActiveSet.class.getSimpleName();

	/** ������Դ */
	private String dataSource;
	/** ҳ�� */
	private int pages;
	/** ���� */
	private int count;
	/** ��ע */
	private String note;
	/** �����ļ���� */
	private String activeDocumentNumber;
	/** ���� */
	private String activeCode;

	@Override
	public String getPropertiesUrl() {
		return "/plm/common/visit.jsp";
	}

	/**
	 * ȡ������Դ
	 * @return dataSource
	 * */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * ��������Դ
	 * @param dataSource Ҫ���õ�dataSource
	 * */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * ȡ��ҳ��
	 * @return pages
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * ����ҳ��
	 * @param pages Ҫ���õ� pages
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * ȡ�÷���
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * ���÷���
	 * @param count Ҫ���õ� count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * ȡ�ñ�ע
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * ���ñ�ע
	 * @param note Ҫ���õ� note
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getActiveDocumentNumber() {
		return activeDocumentNumber;
	}

	public void setActiveDocumentNumber(String activeDocumentNumber) {
		this.activeDocumentNumber = activeDocumentNumber;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public String getFolderPath() {
		// �ļ�����Ϣ
		FolderInfo folderInfo = getFolderInfo();
		if (folderInfo != null) {
			Folder folder = folderInfo.getFolder();
			if (folder != null) {
				// �ļ���PATH
				return FolderHelper.getService().getFolderPathStr(folder);
			}
		}
		return "";
	}
	
	public String getModelName() {
		TypeDefinition sourceDef = TypeHelper.getService().getType(getClassId());
		return sourceDef.getName();
	}

	public String getContextName() {
		Context context = getContextInfo().getContext();
		// ������NAME
		return context.getName();
	}

	public String getSecLevelName() {
		try {
			AdmMaster master = (AdmMaster) getMaster();
			return master.getSecurityLevelInfo().getSecurityLevel().getName();
		} catch (Exception ex) {
			return "";
		}
	}

	public String getVersionName() {
		return getIterationInfo().getFullVersionNo();
	}
}
