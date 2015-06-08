package com.biit.webforms.xforms;

/**
 * Creates a hidden field that stores the email of the user that submits the form. For this, in Liferay the iFrame must
 * have:
 * 
 * Authenticate (enabled):
 * 
 * Authentication Type: Form Form Method: 'Get'
 * 
 * In User Name: Field Name: 'liferay_email_address' Value: '@email_address@'
 * 
 * In the XML result will appear: <liferay_email_address>usermail@liferay.com</liferay_email_address> as first element.
 */
public class XFormsHiddenEmailField {
	private static final String MODEL_NAME = "liferay_email_address";
	private static final String BIND_NAME = MODEL_NAME + "-bind";
	private static final String CONTROL_NAME = MODEL_NAME + "-control";

	public static String getModel() {
		return "<" + MODEL_NAME + "/>";
	}

	public static String getBinding() {
		return "<xf:bind id=\""
				+ BIND_NAME
				+ "\" name=\""
				+ MODEL_NAME
				+ "\" ref=\""
				+ MODEL_NAME
				+ "\"  relevant=\"false\" type=\"xf:email\" xxf:default=\"xxf:get-request-parameter('liferay_email_address')\"/>";
	}

	public static String getResources() {
		return "<" + MODEL_NAME + "><label>eMail</label><hint/><alert/></" + MODEL_NAME + ">";
	}

	public static String getBody() {
		return "<xf:input bind=\"" + BIND_NAME + "\" id=\"" + CONTROL_NAME + "\">"
				+ "<xf:label mediatype=\"text/html\" ref=\"instance('fr-form-resources')/resource/" + MODEL_NAME + "/label\"/>"
				+ "<xf:hint ref=\"instance('fr-form-resources')/resource/" + MODEL_NAME + "/hint\"/>" + "<xf:alert ref=\"instance('fr-form-resources')/resource/"
				+ MODEL_NAME + "/alert\"/>" + "</xf:input>";

	}
}
