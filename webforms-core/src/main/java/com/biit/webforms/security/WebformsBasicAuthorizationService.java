package com.biit.webforms.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.form.entity.IBaseFormView;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.AuthorizationService;
import com.biit.liferay.security.IActivity;
import com.biit.webforms.enumerations.FormWorkStatus;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.IWebformsFormView;
import com.biit.webforms.persistence.entity.SimpleBlockView;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;

public class WebformsBasicAuthorizationService extends AuthorizationService {

	/**
	 * Read
	 */
	private static final WebformsActivity[] READ_ONLY = {

	WebformsActivity.READ,

	};

	/**
	 * Basic activities to manage building blocks
	 */
	private static final WebformsActivity[] MANAGE_BUILDING_BLOCKS = {

	WebformsActivity.BUILDING_BLOCK_EDITING,

	WebformsActivity.BUILDING_BLOCK_ADD_FROM_FORM,

	};

	/**
	 * Extra activities for building blocks 
	 */
	private static final WebformsActivity[] BUILDING_BLOCKS_EXTRA_PERMISSIONS = {

	WebformsActivity.BLOCK_REMOVE

	};

	/**
	 * Extra activities to manage forms
	 */
	private static final WebformsActivity[] MANAGE_FORMS = {

	WebformsActivity.FORM_EDITING,

	WebformsActivity.FORM_FLOW_EDITING,

	WebformsActivity.FORM_STATUS_UPGRADE,

	WebformsActivity.FORM_NEW_VERSION,

	};

	/**
	 * Activities that only administrator user can do. Import/Export Json, remove 
	 */
	private static final WebformsActivity[] FORMS_ADMINISTRATOR_EXTRA_PERMISSIONS = {
	
	WebformsActivity.IMPORT_JSON,
	
	WebformsActivity.EXPORT_JSON,

	WebformsActivity.FORM_STATUS_DOWNGRADE,

	WebformsActivity.FORM_REMOVE,

	};

	/**
	 * Manage general application options.
	 */
	private static final WebformsActivity[] APPLICATION_ADMINISTRATOR = {

	WebformsActivity.EVICT_CACHE

	};

	private static List<IActivity> buildingBlockManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> blockAdministratorPermissions = new ArrayList<IActivity>();
	private static List<IActivity> applicationAdministratorPermissions = new ArrayList<IActivity>();

	private static WebformsBasicAuthorizationService instance = new WebformsBasicAuthorizationService();

	static {
		for (IActivity activity : READ_ONLY) {
			readOnlyPermissions.add(activity);
		}
		for (IActivity activity : MANAGE_BUILDING_BLOCKS) {
			buildingBlockManagerPermissions.add(activity);
		}
		for (IActivity activity : MANAGE_FORMS) {
			formManagerPermissions.add(activity);
		}
		for (IActivity activity : FORMS_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
		for (IActivity activity : BUILDING_BLOCKS_EXTRA_PERMISSIONS) {
			blockAdministratorPermissions.add(activity);
		}
		for (IActivity activity : APPLICATION_ADMINISTRATOR) {
			applicationAdministratorPermissions.add(activity);
		}
	}

	public static WebformsBasicAuthorizationService getInstance() {
		return instance;
	}

	public WebformsBasicAuthorizationService() {
		super();
	}

	/**
	 * Get activities associated to a role that has been assigned to the user.
	 */
	@Override
	public Set<IActivity> getRoleActivities(Role role) {
		Set<IActivity> activities = new HashSet<IActivity>();
		WebformsRoles webFormRole = WebformsRoles.parseTag(role.getName());
		switch (webFormRole) {
		case FORM_ADMIN:
			activities.addAll(formAdministratorPermissions);
			activities.addAll(blockAdministratorPermissions);
			activities.addAll(buildingBlockManagerPermissions);
			activities.addAll(formManagerPermissions);
			activities.addAll(readOnlyPermissions);
			break;
		case BLOCK_EDIT:
			activities.addAll(buildingBlockManagerPermissions);
			activities.addAll(readOnlyPermissions);
			break;
		case FORM_EDIT:
			activities.addAll(formManagerPermissions);
			activities.addAll(readOnlyPermissions);
			break;
		case READ:
			activities.addAll(readOnlyPermissions);
			break;
		case APPLICATION_ADMIN:
			activities.addAll(readOnlyPermissions);
			activities.addAll(applicationAdministratorPermissions);
			break;
		case NULL:
			break;
		}
		return activities;
	}

	public Set<IActivity> getActivitiesOfRoles(List<Role> roles) {
		Set<IActivity> activities = new HashSet<>();
		for (Role role : roles) {
			activities.addAll(getRoleActivities(role));
		}
		return activities;
	}

	public boolean isUserAuthorizedInAnyOrganization(User user, IActivity activity) throws IOException,
			AuthenticationRequired {
		// Check isUserAuthorizedActivity (own permissions)
		if (isAuthorizedActivity(user, activity)) {
			return true;
		}

		// Get all organizations of user
		Set<Organization> organizations = getUserOrganizations(user);
		for (Organization organization : organizations) {
			if (isAuthorizedActivity(user, organization, activity)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAuthorizedActivity(User user, IBaseFormView form, IActivity activity) {
		if (form == null || form.getOrganizationId() == null) {
			return false;
		}
		return isAuthorizedActivity(user, form.getOrganizationId(), activity);
	}

	public boolean isAuthorizedActivity(User user, Long organizationId, IActivity activity) {
		if (organizationId == null) {
			return false;
		}
		Organization organization = getOrganization(user, organizationId);
		if (organization == null) {
			return false;
		}
		try {
			return isAuthorizedActivity(user, organization, activity);
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			// For security
			return false;
		}
	}

	public Organization getOrganization(User user, Long organizationId) {
		try {
			Set<Organization> organizations = getUserOrganizations(user);
			for (Organization organization : organizations) {
				if (organization.getOrganizationId() == organizationId) {
					return organization;
				}
			}
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}

		return null;
	}

	public Set<Organization> getUserOrganizationsWhereIsAuthorized(User user, IActivity activity) {
		Set<Organization> organizations = new HashSet<>();
		try {
			organizations = getUserOrganizations(user);
			Iterator<Organization> itr = organizations.iterator();
			while (itr.hasNext()) {
				Organization organization = itr.next();
				if (!isAuthorizedActivity(user, organization, activity)) {
					itr.remove();
				}
			}
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
		}
		return organizations;
	}

	public boolean isAuthorizedToForm(IBaseFormView form, User user) {
		if (form == null || user == null) {
			return false;
		}

		if (form instanceof Block || form instanceof SimpleBlockView) {
			return isAuthorizedActivity(user, form, WebformsActivity.BUILDING_BLOCK_EDITING);
		} else {
			return isAuthorizedActivity(user, form, WebformsActivity.FORM_EDITING)
					&& (((IWebformsFormView) form).getStatus() == FormWorkStatus.DESIGN);
		}
	}


}
