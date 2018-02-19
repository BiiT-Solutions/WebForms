package com.biit.webforms.utils.conversor.abcd.importer;

/**
 * Conversor abstract class Which is derived by all conversors..
 *
 * @param <O>
 * @param <D>
 */
public abstract class ConversorClass<O,D> implements Conversor<O, D> {

	public D convert(O origin){
		D destiny = createDestinyInstance();
		copyData(origin, destiny);
		return destiny;
	}
	
	public abstract D createDestinyInstance();
	
	public abstract void copyData(O origin, D destiny);
	
}
