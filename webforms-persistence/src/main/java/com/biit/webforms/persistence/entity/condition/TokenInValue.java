package com.biit.webforms.persistence.entity.condition;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;

@Entity
@Table(name = "token_in_value")
public class TokenInValue extends StorableObject {

	@ManyToOne
	private TokenIn tokenIn;

	@Column(nullable = false)
	private long sortSeq = 0;

	@ManyToOne(fetch = FetchType.EAGER)
	private Answer answerValue;

	public Answer getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(Answer answerValue) {
		this.answerValue = answerValue;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return null;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenInValue) {
			copyBasicInfo(object);
			TokenInValue token = (TokenInValue) object;
			this.answerValue = token.answerValue;
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenInValue.class.getName());
		}
	}

	public TokenInValue generateCopy() {
		try {
			TokenInValue newInstance = this.getClass().newInstance();
			newInstance.copyData(this);
			return newInstance;
		} catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException e) {
			// Impossible
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			return null;
		}
	}

	@Override
	public String toString() {
		return answerValue.getName();
	}

	public void setTokenIn(TokenIn tokenIn) {
		this.tokenIn = tokenIn;
	}

	public boolean isContentEqual(TokenInValue tokenInValue) {
		if(answerValue.isContentEqual(tokenInValue.answerValue)){
			return true;
		}
		return false;
	}
}
