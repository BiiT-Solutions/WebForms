package com.biit.webforms.exporters.xforms;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (Core)
 * %%
 * Copyright (C) 2014 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

/**
 * Creates a hidden field that stores the email of the user that submits the form. For this, in Liferay the iFrame must
 * have:
 * <p>
 * Authenticate (enabled):
 * <p>
 * Authentication Type: Form Form Method: 'Get'
 * <p>
 * In User Name: Field Name: 'liferay_email_address' Value: '@email_address@'
 * <p>
 * In the XML result will appear: &lt;liferay_email_address&gt;usermail@liferay.com&lt;liferay_email_address&gt; as first element.
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
