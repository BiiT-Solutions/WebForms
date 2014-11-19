package com.biit.webforms.persistence.entity.condition;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.biit.webforms.enumerations.AnswerSubformat;
import com.biit.webforms.enumerations.AnswerType;
import com.biit.webforms.enumerations.DatePeriodUnit;
import com.biit.webforms.persistence.entity.Question;

@Entity
@Table(name = "token_in")
public class TokenIn extends TokenComplex {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Question question;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AnswerSubformat subformat;

	@Enumerated(EnumType.STRING)
	private DatePeriodUnit datePeriodUnit;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "tokenIn")
	@BatchSize(size = 100)
	@OrderBy(value = "sortSeq ASC")
	private List<TokenInValue> values;

	@Override
	public List<Token> getSimpleTokens() {
		List<Token> simpleTokens = new ArrayList<Token>();
		simpleTokens.add(Token.leftPar());
		if (!values.isEmpty()) {
			if (question.getAnswerType() == AnswerType.INPUT) {
				simpleTokens.add(TokenComparationValue.getTokenEqual(question, subformat, datePeriodUnit, values.get(0).getTextFieldvalue()));
				for(int i=1;i<values.size();i++){
					simpleTokens.add(TokenComparationValue.or());
					simpleTokens.add(TokenComparationValue.getTokenEqual(question, subformat, datePeriodUnit, values.get(i).getTextFieldvalue()));
				}
			} else {
				simpleTokens.add(TokenComparationAnswer.getTokenEqual(question, values.get(0).getAnswerValue()));
				for(int i=1;i<values.size();i++){
					simpleTokens.add(TokenComparationValue.or());
					simpleTokens.add(TokenComparationAnswer.getTokenEqual(question, values.get(i).getAnswerValue()));
				}
			}
		}
		simpleTokens.add(Token.rigthPar());
		return simpleTokens;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TokenIn) {
			super.copyData(object);
			TokenIn token = (TokenIn) object;
			this.question = token.question;
			this.subformat = token.subformat;
			this.datePeriodUnit = token.datePeriodUnit;
			this.values = new ArrayList<>();
			for(TokenInValue value: token.values){
				this.values.add(value.generateCopy());
			}
		} else {
			throw new NotValidStorableObjectException(object.getClass().getName() + " is not compatible with "
					+ TokenComparationAnswer.class.getName());
		}
	}
}
