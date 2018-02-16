package com.biit.webforms.persistence.entity.condition;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.TokenTypes;
import com.biit.webforms.persistence.entity.WebformsBaseQuestion;
import com.biit.webforms.persistence.entity.condition.exceptions.NotValidTokenType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TokenWithQuestion extends Token {
	private static final long serialVersionUID = 3644011189971053183L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "question")
	private WebformsBaseQuestion question;

	public TokenWithQuestion() {
		super();
	}

	public TokenWithQuestion(TokenTypes tokenType) throws NotValidTokenType {
		setType(tokenType);
	}

	public WebformsBaseQuestion getQuestion() {
		return question;
	}

	public void setQuestion(WebformsBaseQuestion question) {
		this.question = question;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenWithQuestion) {
			super.copyData(object);
			TokenWithQuestion token = (TokenWithQuestion) object;
			this.question = token.getQuestion();
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with " + TokenWithQuestion.class.getName());
		}
	}

	@Override
	public void updateReferences(HashMap<String, TreeObject> mappedElements) {
		if (question != null) {
			question = (WebformsBaseQuestion) mappedElements.get(question.getOriginalReference());
		}
	}

	@Override
	public boolean isContentEqual(Token token) {
		if (token instanceof TokenWithQuestion) {
			if (super.isContentEqual(token)) {
				if (!getQuestion().getPathName().equals(((TokenWithQuestion) token).getQuestion().getPathName())) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

}
