package com.biit.webforms.utils.conversor;

public abstract class ConversorClass<O,D> implements Conversor<O, D> {

	public D convert(O origin){
		D destiny = createDestinyInstance();
		copyData(origin, destiny);
		return destiny;
	}
	
	public abstract D createDestinyInstance();
	
	public abstract void copyData(O origin, D destiny);
	
}
