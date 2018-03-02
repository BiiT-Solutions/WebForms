package com.biit.webforms.utils.conversor.abcd.importer;

/**
 * Conversor from O class types to D class types
 * 
 *
 * @param <O>
 * @param <D>
 */
public interface Conversor<O, D> {

	public D convert(O origin);
	
}
