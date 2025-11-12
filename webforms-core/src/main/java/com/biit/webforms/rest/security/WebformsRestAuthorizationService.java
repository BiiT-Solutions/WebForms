package com.biit.webforms.rest.security;

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

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.webforms.security.ISecurityService;
import com.biit.webservice.rest.RestAuthorizationService;
import com.biit.webservice.rest.RestServiceActivity;

@Component
public class WebformsRestAuthorizationService extends RestAuthorizationService {

	@Autowired
	private ISecurityService securityService;

	@Override
	public Boolean checkSpecificAuthorization(IUser<Long> user) {
		try {
			return securityService.isUserAuthorizedInAnyOrganization(user, RestServiceActivity.USE_WEB_SERVICE);
		} catch (IOException | AuthenticationRequired e) {
			return false;
		}
	}

	@Override
	public IAuthenticationService<Long, Long> getAuthenticationService() {
		return securityService.getAuthenticationService();
	}
}
