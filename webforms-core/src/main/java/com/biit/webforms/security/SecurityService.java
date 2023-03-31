package com.biit.webforms.security;

import com.biit.form.entity.IBaseFormView;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IActivityManager;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.IAuthorizationService;
import com.biit.usermanager.security.exceptions.*;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.SimpleBlockView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class SecurityService implements ISecurityService {

    @Autowired
    private IAuthenticationService<Long, Long> authenticationService;

    @Autowired
    private IAuthorizationService<Long, Long, Long> authorizationService;

    @Autowired
    private IActivityManager<Long, Long, Long> activityManager;

    public SecurityService() {
        super();
    }

    @Override
    public Set<IActivity> getActivitiesOfRoles(List<IRole<Long>> roles) {
        Set<IActivity> activities = new HashSet<>();
        for (IRole<Long> role : roles) {
            try {
                activities.addAll(getActivityManager().getRoleActivities(role));
            } catch (InvalidCredentialsException e) {
                WebformsLogger.errorMessage(this.getClass().getName(), e);
            }
        }
        return activities;
    }

    @Override
    public boolean isUserAuthorizedInAnyOrganization(IUser<Long> user, IActivity activity) throws IOException,
            AuthenticationRequired {
        try {
            // Check isUserAuthorizedActivity (own permissions)
            if (isAuthorizedActivity(user, activity)) {
                return true;
            }

            // Get all organizations of user
            Set<IGroup<Long>> organizations = getUserOrganizations(user);
            for (IGroup<Long> organization : organizations) {

                if (isAuthorizedActivity(user, organization, activity)) {
                    return true;
                }
            }
        } catch (UserManagementException e) {
            return false;
        }
        return false;

    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IBaseFormView form, IActivity activity) {
        if (form == null || form.getOrganizationId() == null) {
            return false;
        }
        return isAuthorizedActivity(user, form.getOrganizationId(), activity);
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, Long organizationId, IActivity activity) {
        if (organizationId == null) {
            return false;
        }
        IGroup<Long> organization = getOrganization(user, organizationId);
        if (organization == null) {
            return false;
        }
        try {
            return isAuthorizedActivity(user, organization, activity);
        } catch (UserManagementException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
            // For security
            return false;
        }
    }

    @Override
    public IGroup<Long> getOrganization(IUser<Long> user, Long organizationId) {
        try {
            Set<IGroup<Long>> organizations = getUserOrganizations(user);
            for (IGroup<Long> organization : organizations) {
                if (organization.getUniqueId().equals(organizationId)) {
                    return organization;
                }
            }
        } catch (UserManagementException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }

        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizationsWhereIsAuthorized(IUser<Long> user, IActivity activity) {
        Set<IGroup<Long>> organizations = new HashSet<>();
        try {
            organizations = getUserOrganizations(user);
            Iterator<IGroup<Long>> itr = organizations.iterator();
            while (itr.hasNext()) {
                IGroup<Long> organization = itr.next();
                if (!isAuthorizedActivity(user, organization, activity)) {
                    itr.remove();
                }
            }
        } catch (UserManagementException e) {
            WebformsLogger.errorMessage(this.getClass().getName(), e);
        }
        return organizations;
    }

    @Override
    public boolean isAuthorizedToForm(IBaseFormView form, IUser<Long> user) {
        if (form == null || user == null) {
            return false;
        }

        if (form instanceof Block || form instanceof SimpleBlockView) {
            return isAuthorizedActivity(user, form, WebformsActivity.BUILDING_BLOCK_EDITING);
        } else {
            return isAuthorizedActivity(user, form, WebformsActivity.FORM_EDITING)
                    && (((IWebformsFormView) form).getStatus() == null || (((IWebformsFormView) form).getStatus() == FormWorkStatus.DESIGN));
        }
    }

    @Override
    public IUser<Long> getUserById(Long userId) {
        if (userId != null) {
            try {
                return getAuthenticationService().getUserById(userId);
            } catch (UserManagementException | UserDoesNotExistException | InvalidCredentialsException e) {
                WebformsLogger.warning(this.getClass().getName(), "No user exists with id '" + userId + "'.");
            }
        }
        return null;
    }

    @Override
    public Set<IGroup<Long>> getUserOrganizations(IUser<Long> user) throws UserManagementException {
        try {
            return getAuthorizationService().getUserOrganizations(user);
        } catch (UserDoesNotExistException | InvalidCredentialsException e) {
            throw new UserManagementException("Error connecting to the User Manager System", e);
        }
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) throws UserManagementException {
        try {
            return getActivityManager().isAuthorizedActivity(user, activity);
        } catch (InvalidCredentialsException | UserDoesNotExistException e) {
            throw new UserManagementException("Error connecting to the User Manager System", e);
        }
    }

    @Override
    public void reset() {
        getAuthorizationService().reset();
    }

    @Override
    public IGroup<Long> getOrganization(long organizationId) throws UserManagementException {
        try {
            return getAuthorizationService().getOrganization(organizationId);
        } catch (InvalidCredentialsException | OrganizationDoesNotExistException e) {
            throw new UserManagementException("Error connecting to the User Manager System", e);
        }
    }

    @Override
    public boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity)
            throws UserManagementException {
        try {
            return getActivityManager().isAuthorizedActivity(user, organization, activity);
        } catch (InvalidCredentialsException | OrganizationDoesNotExistException | UserDoesNotExistException e) {
            throw new UserManagementException("Error connecting to the User Manager System", e);
        }
    }

    @Override
    public IAuthorizationService<Long, Long, Long> getAuthorizationService() {
        return authorizationService;
    }

    public void setAuthenticationService(IAuthenticationService<Long, Long> authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public IAuthenticationService<Long, Long> getAuthenticationService() {
        return authenticationService;
    }

    public void setAuthorizationService(IAuthorizationService<Long, Long, Long> authorizationService) {
        this.authorizationService = authorizationService;
    }

    public IActivityManager<Long, Long, Long> getActivityManager() {
        return activityManager;
    }

    public void setActivityManager(IActivityManager<Long, Long, Long> activityManager) {
        this.activityManager = activityManager;
    }
}
