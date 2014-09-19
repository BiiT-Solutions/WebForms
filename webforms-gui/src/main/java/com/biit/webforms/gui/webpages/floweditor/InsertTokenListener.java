package com.biit.webforms.gui.webpages.floweditor;

import com.biit.form.TreeObject;
import com.biit.webforms.persistence.entity.Answer;
import com.biit.webforms.utils.lexer.TokenTypes;

public interface InsertTokenListener {

	void insert(TokenTypes type);

	void insert(TreeObject currentTreeObjectReference);
	
	void insert(Answer answerValue);

}
