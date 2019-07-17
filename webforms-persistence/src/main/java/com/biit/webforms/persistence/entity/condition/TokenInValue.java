package com.biit.webforms.persistence.entity.condition;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Answer;

@Entity
@Table(name = "token_in_value")
public class TokenInValue extends StorableObject {
	private static final long serialVersionUID = 4271257510746487089L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "token_in")
	private TokenIn tokenIn;

	@Column(name = "sort_seq", nullable = false)
	private long sortSeq = 0;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "answer_value")
	private Answer answerValue;

	public Answer getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(Answer answerValue) {
		this.answerValue = answerValue;
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		return new HashSet<>();
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenInValue) {
			copyBasicInfo(object);
			TokenInValue token = (TokenInValue) object;
			this.answerValue = token.answerValue;
		} else {
			throw new NotValidStorableObjectException(
					object.getClass().getName() + " is not compatible with " + TokenInValue.class.getName());
		}
	}

	public TokenInValue generateCopy() {
		try {
			TokenInValue newInstance = this.getClass().getDeclaredConstructor().newInstance();
			newInstance.copyData(this);
			return newInstance;
		} catch (InstantiationException | IllegalAccessException | NotValidStorableObjectException
				| IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
		if (answerValue.isContentEqual(tokenInValue.answerValue)) {
			return true;
		}
		return false;
	}
}
