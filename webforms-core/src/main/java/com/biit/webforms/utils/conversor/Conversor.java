package com.biit.webforms.utils.conversor;

public interface Conversor<O, D> {

	public D convert(O origin);
	
}
