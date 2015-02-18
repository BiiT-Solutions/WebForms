package com.biit.webforms.gui.webpages;

@SuppressWarnings("rawtypes")
public enum WebMap {
	
	ERROR_PAGE(ErrorPage.class),
	
	LOGIN_PAGE(Login.class),

	FORM_MANAGER(FormManager.class),

	BLOCK_MANAGER(BlockManager.class),

	DESIGNER_EDITOR(Designer.class),

	FLOW_EDITOR(FlowEditor.class),

	VALIDATION(Validation.class),

	COMPARE_STRUCTURE(CompareStructure.class),

	COMPARE_CONTENT(CompareContent.class),

	;

	private static WebMap loginPage = WebMap.LOGIN_PAGE;

	private static WebMap defaultPage = WebMap.FORM_MANAGER;
	
	private static WebMap errorPage = WebMap.ERROR_PAGE;

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

	public static void setErrorPage(WebMap errorPage) {
		WebMap.errorPage = errorPage;
	}

}
