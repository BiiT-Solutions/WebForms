package com.biit.webforms.gui;

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
