package com.biit.webforms.security;

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
