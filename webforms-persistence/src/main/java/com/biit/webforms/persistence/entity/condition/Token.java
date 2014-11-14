package com.biit.webforms.persistence.entity.condition;

import java.util.HashMap;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Flow;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

/**
 * Base class for any kind of token.
 * 
 */
@Entity
@Table(name = "token")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Token extends StorableObject {

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TokenTypes type;

	@Column(nullable = false)
	private long sortSeq = 0;

	@ManyToOne
	private Flow flow;

	protected Token() {
		super();
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

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Token) {
			copyBasicInfo(object);
			Token token = (Token) object;
			this.type = token.type;
			this.sortSeq = token.sortSeq;
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ Token.class.getName());
		}
	}

	public Token generateCopy() {
		try {
			Token newInstance = this.getClass().newInstance();
			newInstance.copyData(this);
			return newInstance;
		} catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	public long getSortSeq() {
		return sortSeq;
	}

	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
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

	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		// There are no references to update
	}

	public static Token not() {
		return getToken(TokenTypes.NOT);
	}

	public static Token leftPar() {
		return getToken(TokenTypes.LEFT_PAR);
	}

	public static Token rigthPar() {
		return getToken(TokenTypes.RIGHT_PAR);
	}

	public static Token and() {
		return getToken(TokenTypes.AND);
	}

	public static Token or() {
		return getToken(TokenTypes.OR);
	}

	/**
	 * Returns a copy with the inverse of the current token comparation.
	 * 
	 * @return
	 */
	public Token inverse() {
		try {
			Token copiedToken = generateCopy();
			switch (getType()) {
			case LT:
				copiedToken.setType(TokenTypes.GE);
				break;
			case LE:
				copiedToken.setType(TokenTypes.GT);
				break;
			case GT:
				copiedToken.setType(TokenTypes.LE);
				break;
			case GE:
				copiedToken.setType(TokenTypes.LT);
				break;
			case EQ:
				copiedToken.setType(TokenTypes.NE);
				break;
			case NE:
				copiedToken.setType(TokenTypes.EQ);
				break;
			default:
				return null;
			}

			return copiedToken;
		} catch (NotValidTokenType e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return null;
	}

	public String getExpressionSimplifierRepresentation() {
		return type.getExpressionSimplifierRepresentation();
	}

	public static Token getFromSimplifierRepresentation(String representation) {
		TokenTypes tokenTypes = TokenTypes.getFromExpressionSimplifierRepresentation(representation);
		if (tokenTypes == null) {
			return null;
		}
		return getToken(tokenTypes);
	}
}
