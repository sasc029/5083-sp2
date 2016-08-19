package com.bjsasc.ddm.common;

import com.bjsasc.plm.grid.editor.Editor;
import com.bjsasc.plm.grid.editor.EditorBuilder;

public class HiddenEditorBuilder implements EditorBuilder {
	public String build(Editor editor) {
		String code = "hidden=\"true\"";
		return code;
	}
}
