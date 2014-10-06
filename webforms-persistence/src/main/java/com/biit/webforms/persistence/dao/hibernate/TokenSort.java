package com.biit.webforms.persistence.dao.hibernate;

import java.util.Comparator;

import com.biit.webforms.persistence.entity.condition.Token;

public class TokenSort implements Comparator<Token> {

	@Override
	public int compare(Token arg0, Token arg1) {
		return Long.compare(arg0.getSortSeq(),arg1.getSortSeq());
	}

}
