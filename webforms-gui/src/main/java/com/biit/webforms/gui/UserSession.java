package com.biit.webforms.gui;

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.biit.webforms.gui.exceptions.SessionHasAlreadyUser;
import com.biit.webforms.gui.webpages.WebMap;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * New object to control the user session
 * @author joriz_000
 *
 */
public class UserSession {

	private static final String USER_CREDENTIAL = "user-credential";
	private static final String LAST_PAGE = "last-page";

	public static void setUser(IUser<Long> user){
		try{
			VaadinSession.getCurrent().getLockInstance().lock();
			VaadinSession.getCurrent().setAttribute(USER_CREDENTIAL, user);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static IUser<Long> getUser() {
		try{
			VaadinSession.getCurrent().getLockInstance().lock();
			return (IUser<Long>) VaadinSession.getCurrent().getAttribute(USER_CREDENTIAL);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
	}
	
	public static IUser<Long> login(String userEmail, String password) throws UserManagementException, AuthenticationRequired, InvalidCredentialsException,
			SessionHasAlreadyUser, UserDoesNotExistException {
		return ((ApplicationUi)ApplicationUi.getCurrent()).login(userEmail,password);
	}
	
	public static void logout(){
		for(final UI ui: VaadinSession.getCurrent().getUIs()){
			ui.access(new Runnable() {
				
				@Override
				public void run() {
					ui.getPage().setLocation("./VAADIN/logout.html");
				}
			});
		}
		VaadinSession.getCurrent().close();
	}
	
	public static WebMap getUserLastPage() {
		try{
			VaadinSession.getCurrent().getLockInstance().lock();
			return (WebMap) VaadinSession.getCurrent().getAttribute(LAST_PAGE);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
	}

	public static void setUserLastPage(WebMap page) {
		try{
			VaadinSession.getCurrent().getLockInstance().lock();
			VaadinSession.getCurrent().setAttribute(LAST_PAGE, page);
		} finally {
			VaadinSession.getCurrent().getLockInstance().unlock();
		}
	}
}
