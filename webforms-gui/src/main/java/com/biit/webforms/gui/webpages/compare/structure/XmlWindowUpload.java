package com.biit.webforms.gui.webpages.compare.structure;

import com.biit.webforms.gui.common.components.FileUpload;
import com.biit.webforms.gui.common.components.WindowUpload;

/**
 * Specific WindowUpload for Xml like files.
 * 
 */
public class XmlWindowUpload extends WindowUpload {
	private static final long serialVersionUID = -2641060032099315217L;

	/**
	 * Override to implement a check when the file has finished
	 * @param fileUpload
	 */
	protected void check(FileUpload fileUpload) {
//		deleteFile(fileUpload);
	}
}
