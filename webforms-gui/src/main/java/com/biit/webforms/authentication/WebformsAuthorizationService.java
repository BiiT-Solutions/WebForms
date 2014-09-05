package com.biit.webforms.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.security.AuthorizationService;
import com.biit.liferay.security.IActivity;
import com.biit.webforms.gui.UiAccesser;
import com.biit.webforms.logger.WebformsLogger;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Form;
import com.biit.webforms.persistence.entity.enumerations.FormWorkStatus;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;

public class WebformsAuthorizationService extends AuthorizationService {

	/**
	 * Read
	 */
	private static final WebformsActivity[] WEBFORMS_READ = {

	WebformsActivity.READ,

	};
	private static final WebformsActivity[] WEBFORMS_MANAGE_EDIT_BUILDING_BLOCKS = {

	WebformsActivity.BUILDING_BLOCK_EDITING,

	WebformsActivity.BUILDING_BLOCK_ADD_FROM_FORM,

	};

	private static final WebformsActivity[] WEBFORMS_MANAGE_EDIT_FORMS = {

	WebformsActivity.FORM_EDITING,

	WebformsActivity.FORM_FLOW_EDITING,

	WebformsActivity.FORM_STATUS_UPGRADE,

	WebformsActivity.FORM_NEW_VERSION,

	};

	private static final WebformsActivity[] WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS = {

	WebformsActivity.FORM_STATUS_DOWNGRADE,
	
	WebformsActivity.ADMIN_RIGHTS,

	};

	private static List<IActivity> buildingBlockManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
	private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
	private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();

	private static WebformsAuthorizationService instance = new WebformsAuthorizationService();

	static {
		for (IActivity activity : WEBFORMS_READ) {
			readOnlyPermissions.add(activity);
		}
		for (IActivity activity : WEBFORMS_MANAGE_EDIT_BUILDING_BLOCKS) {
			buildingBlockManagerPermissions.add(activity);
			formAdministratorPermissions.add(activity);
		}
		for (IActivity activity : WEBFORMS_MANAGE_EDIT_FORMS) {
			formManagerPermissions.add(activity);
			formAdministratorPermissions.add(activity);
		}
		for (IActivity activity : WEBFORMS_ADMINISTRATOR_EXTRA_PERMISSIONS) {
			formAdministratorPermissions.add(activity);
		}
	}

	public static WebformsAuthorizationService getInstance() {
		return instance;
	}

	private UserGroupService userGroupService = new UserGroupService();

	public WebformsAuthorizationService() {
		super();
		userGroupService.serverConnection();
	}

	/**
	 * Get activities associated to a role that has been assigned to the user.
	 */
	@Override
	public List<IActivity> getRoleActivities(Role role) {
		List<IActivity> activities = new ArrayList<IActivity>();
		System.out.println(role.getName());
		WebformsRoles webFormRole = WebformsRoles.parseTag(role.getName());
		switch (webFormRole) {
		case ADMIN:
			activities.addAll(formAdministratorPermissions);
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
		case NULL:
			break;
		}
		System.out.println(activities);
		return activities;
	}

	// public boolean isAuthorizedActivity(User user, Form form, IActivity
	// activity) {
	// try {
	// UserGroup userGroup =
	// userGroupService.getUserGroup(form.getUserGroupId());
	// return isAuthorizedActivity(user, userGroup, activity);
	// } catch (NotConnectedToWebServiceException |
	// UserGroupDoesNotExistException | IOException
	// | AuthenticationRequired | WebServiceAccessError e) {
	// WebformsLogger.errorMessage(this.getClass().getName(), e);
	// }
	// return false;
	// }
	//
	// public boolean isAuthorizedActivity(User user, UserGroup group, IActivity
	// activity) {
	// try {
	// List<UserGroup> userGroups = getUserGroups(user);
	// if (!userGroups.contains(group)) {
	// // User is not assigned to that user group.
	// return false;
	// }
	// // Now check the roles of the user in the group.
	// List<Role> userRolesForGroup = getUserGroupRoles(group);
	// Set<IActivity> userActivitiesForGroup =
	// getActivitiesOfRoles(userRolesForGroup);
	// return userActivitiesForGroup.contains(activity);
	//
	// } catch (IOException | AuthenticationRequired e) {
	// WebformsLogger.errorMessage(this.getClass().getName(), e);
	// }
	// return false;
	// }

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
		List<Organization> organizations = getUserOrganizations(user);
		for (Organization organization : organizations) {
			if (isAuthorizedActivity(user, organization, activity)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAuthorizedActivity(User user, Form form, IActivity activity) {
		if (form == null || form.getOrganizationId() == null) {
			return false;
		}
		Organization organization = getOrganization(user, form.getOrganizationId());
		if (organization == null) {
			return false;
		}
		try {
			return isAuthorizedActivity(user, organization, activity);
		} catch (IOException | AuthenticationRequired e) {
			WebformsLogger.errorMessage(this.getClass().getName(), e);
			//For security
			return false;
		}
	}

	public Organization getOrganization(User user, Long organizationId) {
		try {
			List<Organization> organizations = getUserOrganizations(user);
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

	public List<Organization> getUserOrganizationsWhereIsAuthorized(User user, IActivity activity) {
		List<Organization> organizations = new ArrayList<>();
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
	
	public boolean isAuthorizedToForm(Form form, User user){
		boolean formIsBlock = form instanceof Block;
		boolean formIsInDesign = form.getStatus() == FormWorkStatus.DESIGN;
		boolean blockEditAuthorized = isAuthorizedActivity(user, form, WebformsActivity.BUILDING_BLOCK_EDITING);
		boolean formEditAuthorized = isAuthorizedActivity(user, form, WebformsActivity.FORM_EDITING);
		return formIsInDesign && ((formIsBlock && blockEditAuthorized) || (!formIsBlock && formEditAuthorized));
	}

	public boolean isFormEditable(Form form, User user) {
		boolean userLockedForm = UiAccesser.isUserUserUsingForm(user,form);	
		return userLockedForm && isAuthorizedToForm(form, user);
	}

	public boolean isFormReadOnly(Form form, User user) {
		boolean formIsInUse = UiAccesser.getUserUsingForm(form)!=null;
		System.out.println(!formIsInUse +" "+ !isAuthorizedToForm(form, user) +" "+ (formIsInUse && UiAccesser.getUserUsingForm(form)!=user));
		return (!formIsInUse && !isAuthorizedToForm(form, user)) || (formIsInUse && UiAccesser.getUserUsingForm(form)!=user);
	}

	//
	// @Override
	// public boolean isAuthorizedActivity(User user, IActivity activity) throws
	// IOException, AuthenticationRequired {
	// if (user == null) {
	// return false;
	// }
	// // Is it in the pool?
	// Boolean authorized = authorizationPool.isAuthorizedActivity(user,
	// activity);
	// if (authorized != null) {
	// return authorized;
	// }
	//
	// // Calculate authorization.
	// authorized = getUserActivitiesAllowed(user).contains(activity);
	// authorizationPool.addUser(user, activity, authorized);
	// return authorized;
	// }
}
