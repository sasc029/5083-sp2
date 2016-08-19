package com.bjsasc.adm.common;

import com.bjsasc.plm.type.attr.Attr;
import com.bjsasc.plm.type.validator.ValidateService;
import com.bjsasc.plm.type.validator.Validator;

/**
 * ������֤�ࡣ
 * 
 * @author gengancong 2013-07-01
 */
public class IntegerValidatorImpl implements ValidateService {
	
	@Override
	public String validate(String inputValue, Attr attr, Validator validator,String oid) {
		if (inputValue == null || inputValue.length() == 0) {
			return null;
		}
		try {
			Integer.parseInt(inputValue);
		} catch (Exception ex) {
			return "��������";
		}
		return null;
	}
}
