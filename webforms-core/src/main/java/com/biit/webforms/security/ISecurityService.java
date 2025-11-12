package com.biit.webforms.security;

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
import java.util.List;
import java.util.Set;

import com.biit.form.entity.IBaseFormView;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.IAuthorizationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface ISecurityService {

	Set<IGroup<Long>> getUserOrganizationsWhereIsAuthorized(IUser<Long> user, IActivity activity);

	boolean isAuthorizedToForm(IBaseFormView form, IUser<Long> user);

	IGroup<Long> getOrganization(IUser<Long> user, Long organizationId);

	boolean isAuthorizedActivity(IUser<Long> user, Long organizationId, IActivity activity);

	boolean isAuthorizedActivity(IUser<Long> user, IBaseFormView form, IActivity activity);

	boolean isUserAuthorizedInAnyOrganization(IUser<Long> user, IActivity activity) throws IOException,
			AuthenticationRequired;

	Set<IActivity> getActivitiesOfRoles(List<IRole<Long>> roles);

	IAuthorizationService<Long, Long, Long> getAuthorizationService();

	IAuthenticationService<Long, Long> getAuthenticationService();

	Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException;

	boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException;

	void reset();

	boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity)
			throws UserManagementException;

	IGroup<Long> getOrganization(long organizationId) throws UserManagementException;

	IUser<Long> getUserById(Long userId) throws UserDoesNotExistException;

}
