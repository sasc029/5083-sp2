package com.bjsasc.adm.active.model.activeorder;

import org.apache.log4j.Logger;

import com.bjsasc.adm.active.model.ActiveBase;
import com.bjsasc.adm.active.model.ActiveObjOrder;
import com.bjsasc.adm.active.model.ActiveSeted;
import com.bjsasc.adm.active.model.AdmMaster;
import com.bjsasc.adm.active.model.Recycledable;
import com.bjsasc.plm.core.attachment.FileHolder;
import com.bjsasc.plm.core.context.model.Context;
import com.bjsasc.plm.core.folder.Folder;
import com.bjsasc.plm.core.folder.FolderHelper;
import com.bjsasc.plm.core.folder.FolderInfo;
import com.bjsasc.plm.core.type.TypeDefinition;
import com.bjsasc.plm.core.type.TypeHelper;
import com.bjsasc.plm.core.util.DateTimeUtil;

/**
 * ���е���ģ�͡�
 * 
 * @author yanjia 2013-5-13
 */
public class ActiveOrder extends ActiveBase implements ActiveSeted,
		Recycledable, FileHolder, ActiveObjOrder {

	/** serialVersionUID */
	private static final long serialVersionUID = -1649908282603536516L;

	public static final String CLASSID = ActiveOrder.class.getSimpleName();
	
	private static final Logger LOG = Logger.getLogger(ActiveOrder.class);

	/** ������Դ */
	private String dataSource;
	/** �������� */
	private String authorName;
	/** �ļ�����ʱ�� */
	private long authorTime;
	/** ����������λ */
	private String authorUnit;
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
	 * 
	 * @return dataSource
	 * */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * ��������Դ
	 * 
	 * @param dataSource
	 *            Ҫ���õ�dataSource
	 * */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * ȡ����������
	 * 
	 * @return authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * ������������
	 * 
	 * @param authorName
	 *            Ҫ���õ� authorName
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * ȡ���ļ�����ʱ��
	 * 
	 * @return authorTime
	 */
	public long getAuthorTime() {
		return authorTime;
	}

	/**
	 * �����ļ�����ʱ��
	 * 
	 * @param authorTime
	 *            Ҫ���õ� authorTime
	 */
	public void setAuthorTime(long authorTime) {
		this.authorTime = authorTime;
	}
	
	public String getDisAuthorTime() {
		return DateTimeUtil.dateDisplay(authorTime, DateTimeUtil.DATE_YYYYMMDD);
	}

	/**
	 * ȡ������������λ
	 * 
	 * @return authorUnit
	 */
	public String getAuthorUnit() {
		return authorUnit;
	}

	/**
	 * ��������������λ
	 * 
	 * @param authorUnit
	 *            Ҫ���õ� authorUnit
	 */
	public void setAuthorUnit(String authorUnit) {
		this.authorUnit = authorUnit;
	}

	/**
	 * ȡ��ҳ��
	 * 
	 * @return pages
	 */
	public int getPages() {
		return pages;
	}

	/**
	 * ����ҳ��
	 * 
	 * @param pages
	 *            Ҫ���õ� pages
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}

	/**
	 * ȡ�÷���
	 * 
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * ���÷���
	 * 
	 * @param count
	 *            Ҫ���õ� count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * ȡ�ñ�ע
	 * 
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * ���ñ�ע
	 * 
	 * @param note
	 *            Ҫ���õ� note
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
		try {
			// �ļ�����Ϣ
			FolderInfo folderInfo = getFolderInfo();
			if (folderInfo != null) {
				Folder folder = folderInfo.getFolder();
				if (folder != null) {
					// �ļ���PATH
					return FolderHelper.getService().getFolderPathStr(folder);
				}
			}
		} catch (Exception ex) {
			LOG.debug("�ļ�����Ϣȡ��ʧ�ܡ�");
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
