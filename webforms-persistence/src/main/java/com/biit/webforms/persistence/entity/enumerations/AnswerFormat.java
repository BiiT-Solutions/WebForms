package com.biit.webforms.persistence.entity.enumerations;

/**
 * Used only for text inputs.
 */
public enum AnswerFormat {

	TEXT(AnswerSubformat.TEXT,new AnswerSubformat[]{AnswerSubformat.TEXT,AnswerSubformat.EMAIL,AnswerSubformat.PHONE,AnswerSubformat.IBAN,AnswerSubformat.BSN}),

	NUMBER(AnswerSubformat.NUMBER, new AnswerSubformat[]{AnswerSubformat.NUMBER,AnswerSubformat.FLOAT}),

	DATE(AnswerSubformat.DATE, new AnswerSubformat[]{AnswerSubformat.DATE,AnswerSubformat.DATE_PAST,AnswerSubformat.DATE_FUTURE,AnswerSubformat.DATE_PERIOD}),

	POSTAL_CODE(AnswerSubformat.POSTAL_CODE, new AnswerSubformat[]{AnswerSubformat.POSTAL_CODE})
	
	;
	
	private final AnswerSubformat defaultSubformat;
	private final AnswerSubformat[] subformats;
	
	private AnswerFormat(AnswerSubformat defaultSubformat, AnswerSubformat[] subformats) {
		this.defaultSubformat = defaultSubformat;
		this.subformats = subformats;
	}
	
	public AnswerSubformat[] getSubformats(){
		return subformats;
	}
	
	public AnswerSubformat getDefaultSubformat(){
		return defaultSubformat;
	}
	
	public boolean isSubformat(AnswerSubformat subformat){
		for(AnswerSubformat value: subformats){
			if(value.equals(subformat)){
				return true;
			}
		}
		return false;
	}
}
