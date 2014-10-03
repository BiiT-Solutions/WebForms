package com.biit.webforms.persistence.entity.condition;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.transaction.NotSupportedException;

import com.biit.persistence.entity.StorableObject;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Rule;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

/**
 * Base class for any kind of token.
 * 
 */
@Entity
@Table(name = "token")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Token extends StorableObject {

	@Enumerated(EnumType.STRING)
	private TokenTypes type;

	@Column(nullable = false)
	private long sortSeq = 0;

	@ManyToOne
	private Rule rule;

	protected Token() {

	}

	public Token(TokenTypes tokenType) throws NotValidTokenType {
		setType(tokenType);
	}

	public TokenTypes[] getValidTokenTypes() {
		return null;
	}

	private boolean isValidTokenType(TokenTypes tokenType) {
		TokenTypes[] tokenTypes = getValidTokenTypes();
		if (tokenTypes == null) {
			return true;
		}
		for (int i = 0; i < tokenTypes.length; i++) {
			if (tokenTypes[i].equals(tokenType)) {
				return true;
			}
		}
		return false;
	}

	public TokenTypes getType() {
		return type;
	}

	protected void setType(TokenTypes tokenType) throws NotValidTokenType {
		if (isValidTokenType(tokenType)) {
			this.type = tokenType;
		} else {
			throw new NotValidTokenType("Token type " + tokenType + " is not valid for token class "
					+ this.getClass().getName());
		}
	}

	@Override
	public String toString() {
		return type.toString();
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return null;
	}

	public void copyData(Token token) throws NotSupportedException {
		this.type = token.type;
		this.sortSeq = token.sortSeq;
	}

	public Token generateCopy() {
		try {
			Token newInstance = this.getClass().newInstance();
			newInstance.copyData(this);
			return newInstance;
		} catch (InstantiationException | IllegalAccessException | NotSupportedException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	public long getSortSeq() {
		return sortSeq;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void setSortSeq(long sortSeq) {
		this.sortSeq = sortSeq;
	}

	public static Token getToken(TokenTypes tokenType) {
		try {
			return new Token(tokenType);
		} catch (NotValidTokenType e) {
			// Impossible
			WebformsLogger.errorMessage(Token.class.getName(), e);
			return null;
		}
	}
}
