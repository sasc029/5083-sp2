package com.bjsasc.ddm.distribute.pojo.commonsearch;

import static com.bjsasc.plm.core.util.StringUtil.encoding;

import com.bjsasc.ddm.distribute.pojo.BasePOJO;

/**
 * 综合查询画面模型。
 * 
 * @author gengancong 2013-3-11
 */
public class CommonSearchPOJO extends BasePOJO {

	/** serialVersionUID */
	private static final long serialVersionUID = -2009379315882825330L;

	private String keywords = "";
	private String queryName = "";
	private String queryNumber = "";
	private String queryCode = "";
	private String creator = "";
	private String modifier = "";
	private String hidDistributeType = "";
	private String hidDistributeTypes = "";
	private String hidDistributeState = "";
	private String hidDistributeStates = "";
	private String hidCreateDate = "";
	private String hidModifyDate = "";
	private String hidBlur = "";
	private String createPastDays = "";
	private String queryCreateDateFrom = "";
	private String queryCreateDateTo = "";
	private String modifyPastDays = "";
	private String queryModifyDateFrom = "";
	private String queryModifyDateTo = "";
	private String distributeTypes = "";
	private String distributeStates = "";
	private String creatorName = "";
	private String modifierName = "";

	/**
	 * @return keywords
	 */
	public String getKeywords() {
		return encoding(keywords);
	}

	/**
	 * @param keywords 要设置的 keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return queryName
	 */
	public String getQueryName() {
		return encoding(queryName);
	}

	/**
	 * @param queryName 要设置的 queryName
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**
	 * @return queryNumber
	 */
	public String getQueryNumber() {
		return encoding(queryNumber);
	}

	/**
	 * @param queryNumber 要设置的 queryNumber
	 */
	public void setQueryNumber(String queryNumber) {
		this.queryNumber = queryNumber;
	}

	/**
	 * @return queryCode
	 */
	public String getQueryCode() {
		return encoding(queryCode);
	}

	/**
	 * @param queryCode 要设置的 queryCode
	 */
	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	/**
	 * @return creator
	 */
	public String getCreator() {
		return encoding(creator);
	}

	/**
	 * @param creator 要设置的 creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return modifier
	 */
	public String getModifier() {
		return encoding(modifier);
	}

	/**
	 * @param modifier 要设置的 modifier
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return hidDistributeType
	 */
	public String getHidDistributeType() {
		return encoding(hidDistributeType);
	}

	/**
	 * @param hidDistributeType 要设置的 hidDistributeType
	 */
	public void setHidDistributeType(String hidDistributeType) {
		this.hidDistributeType = hidDistributeType;
	}

	/**
	 * @return hidDistributeTypes
	 */
	public String getHidDistributeTypes() {
		return encoding(hidDistributeTypes);
	}

	/**
	 * @param hidDistributeTypes 要设置的 hidDistributeTypes
	 */
	public void setHidDistributeTypes(String hidDistributeTypes) {
		this.hidDistributeTypes = hidDistributeTypes;
	}

	/**
	 * @return hidDistributeState
	 */
	public String getHidDistributeState() {
		return encoding(hidDistributeState);
	}

	/**
	 * @param hidDistributeState 要设置的 hidDistributeState
	 */
	public void setHidDistributeState(String hidDistributeState) {
		this.hidDistributeState = hidDistributeState;
	}

	/**
	 * @return hidDistributeStates
	 */
	public String getHidDistributeStates() {
		return encoding(hidDistributeStates);
	}

	/**
	 * @param hidDistributeStates 要设置的 hidDistributeStates
	 */
	public void setHidDistributeStates(String hidDistributeStates) {
		this.hidDistributeStates = hidDistributeStates;
	}

	/**
	 * @return hidCreateDate
	 */
	public String getHidCreateDate() {
		return encoding(hidCreateDate);
	}

	/**
	 * @param hidCreateDate 要设置的 hidCreateDate
	 */
	public void setHidCreateDate(String hidCreateDate) {
		this.hidCreateDate = hidCreateDate;
	}

	/**
	 * @return hidModifyDate
	 */
	public String getHidModifyDate() {
		return encoding(hidModifyDate);
	}

	/**
	 * @param hidModifyDate 要设置的 hidModifyDate
	 */
	public void setHidModifyDate(String hidModifyDate) {
		this.hidModifyDate = hidModifyDate;
	}

	/**
	 * @return hidBlur
	 */
	public String getHidBlur() {
		return encoding(hidBlur);
	}

	/**
	 * @param hidBlur 要设置的 hidBlur
	 */
	public void setHidBlur(String hidBlur) {
		this.hidBlur = hidBlur;
	}

	/**
	 * @return createPastDays
	 */
	public String getCreatePastDays() {
		return encoding(createPastDays);
	}

	/**
	 * @param createPastDays 要设置的 createPastDays
	 */
	public void setCreatePastDays(String createPastDays) {
		this.createPastDays = createPastDays;
	}

	/**
	 * @return queryCreateDateFrom
	 */
	public String getQueryCreateDateFrom() {
		return encoding(queryCreateDateFrom);
	}

	/**
	 * @param queryCreateDateFrom 要设置的 queryCreateDateFrom
	 */
	public void setQueryCreateDateFrom(String queryCreateDateFrom) {
		this.queryCreateDateFrom = queryCreateDateFrom;
	}

	/**
	 * @return queryCreateDateTo
	 */
	public String getQueryCreateDateTo() {
		return encoding(queryCreateDateTo);
	}

	/**
	 * @param queryCreateDateTo 要设置的 queryCreateDateTo
	 */
	public void setQueryCreateDateTo(String queryCreateDateTo) {
		this.queryCreateDateTo = queryCreateDateTo;
	}

	/**
	 * @return modifyPastDays
	 */
	public String getModifyPastDays() {
		return encoding(modifyPastDays);
	}

	/**
	 * @param modifyPastDays 要设置的 modifyPastDays
	 */
	public void setModifyPastDays(String modifyPastDays) {
		this.modifyPastDays = modifyPastDays;
	}

	/**
	 * @return queryModifyDateFrom
	 */
	public String getQueryModifyDateFrom() {
		return encoding(queryModifyDateFrom);
	}

	/**
	 * @param queryModifyDateFrom 要设置的 queryModifyDateFrom
	 */
	public void setQueryModifyDateFrom(String queryModifyDateFrom) {
		this.queryModifyDateFrom = queryModifyDateFrom;
	}

	/**
	 * @return queryModifyDateTo
	 */
	public String getQueryModifyDateTo() {
		return encoding(queryModifyDateTo);
	}

	/**
	 * @param queryModifyDateTo 要设置的 queryModifyDateTo
	 */
	public void setQueryModifyDateTo(String queryModifyDateTo) {
		this.queryModifyDateTo = queryModifyDateTo;
	}

	/**
	 * @return distributeTypes
	 */
	public String getDistributeTypes() {
		return encoding(distributeTypes);
	}

	/**
	 * @param distributeTypes 要设置的 distributeTypes
	 */
	public void setDistributeTypes(String distributeTypes) {
		this.distributeTypes = distributeTypes;
	}

	/**
	 * @return distributeStates
	 */
	public String getDistributeStates() {
		return encoding(distributeStates);
	}

	/**
	 * @param distributeStates 要设置的 distributeStates
	 */
	public void setDistributeStates(String distributeStates) {
		this.distributeStates = distributeStates;
	}

	/**
	 * @return creatorName
	 */
	public String getCreatorName() {
		return encoding(creatorName);
	}

	/**
	 * @param creatorName 要设置的 creatorName
	 */
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/**
	 * @return modifierName
	 */
	public String getModifierName() {
		return encoding(modifierName);
	}

	/**
	 * @param modifierName 要设置的 modifierName
	 */
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

}
