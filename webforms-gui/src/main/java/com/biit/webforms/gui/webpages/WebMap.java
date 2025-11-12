package com.biit.webforms.gui.webpages;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

@SuppressWarnings("rawtypes")
public enum WebMap {
	
	ERROR_PAGE(ErrorPage.class),
	
	NOT_FOUND_PAGE(NotFoundPage.class),
	
	LOGIN_PAGE(Login.class),

	FORM_MANAGER(FormManager.class),

	BLOCK_MANAGER(BlockManager.class),

	DESIGNER_EDITOR(Designer.class),

	FLOW_EDITOR(FlowEditor.class),

	VALIDATION(Validation.class),

	COMPARE_STRUCTURE(CompareStructure.class),

	COMPARE_CONTENT(CompareContent.class), 
	
	WEBSERVICE_CALL_EDITOR(WebserviceCallEditor.class),

	;

	private static WebMap loginPage = WebMap.LOGIN_PAGE;

	private static WebMap defaultPage = WebMap.FORM_MANAGER;
	
	private static WebMap errorPage = WebMap.ERROR_PAGE;
	
	private static WebMap notFoundPage = WebMap.NOT_FOUND_PAGE;

	private Class redirectTo;

	WebMap(Class redirectTo) {
		this.redirectTo = redirectTo;
	}

	public Class getWebPageJavaClass() {
		return redirectTo;
	}

	public static WebMap getLoginPage() {
		return loginPage;
	}

	public static WebMap getMainPage() {
		return defaultPage;
	}

	public static WebMap getErrorPage() {
		return errorPage;
	}

	public static WebMap getNotFoundPage() {
		return notFoundPage;
	}
}
