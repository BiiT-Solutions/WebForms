package com.biit.webforms.gui.webpages;

@SuppressWarnings("rawtypes")
public enum WebMap {
	LOGIN_PAGE(Login.class),
	
	PROJECT_MANAGER(ProjectManager.class);
	

	private static WebMap loginPage = WebMap.LOGIN_PAGE;
	
	private static WebMap defaultPage = WebMap.PROJECT_MANAGER;

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

}
