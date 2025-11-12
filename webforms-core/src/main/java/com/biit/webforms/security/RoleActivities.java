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

import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.security.IActivity;
import com.biit.usermanager.security.IRoleActivities;
import com.biit.webservice.rest.RestServiceActivity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleActivities implements IRoleActivities {

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

            WebformsActivity.EXPORT_SCORECARD_XLS,

    };

    /**
     * Activities that only administrator user can do. Import/Export Json,
     * remove
     */
    private static final WebformsActivity[] FORMS_ADMINISTRATOR_EXTRA_PERMISSIONS = {

            WebformsActivity.IMPORT_JSON,

            WebformsActivity.EXPORT_JSON,

            WebformsActivity.FORM_STATUS_DOWNGRADE,

            WebformsActivity.FORM_REMOVE,

            WebformsActivity.EXPORT_ABCD,

            WebformsActivity.EXPORT_TO_CSV,

            WebformsActivity.PUBLISH_TO_KNOWLEDGE_MANAGER

    };

    /**
     * Manage general application options.
     */
    private static final WebformsActivity[] APPLICATION_ADMINISTRATOR = {

            WebformsActivity.EVICT_CACHE

    };

    private static final RestServiceActivity[] WEB_SERVICES_PERMISSIONS = {

            RestServiceActivity.USE_WEB_SERVICE

    };

    private static List<IActivity> buildingBlockManagerPermissions = new ArrayList<IActivity>();
    private static List<IActivity> formManagerPermissions = new ArrayList<IActivity>();
    private static List<IActivity> readOnlyPermissions = new ArrayList<IActivity>();
    private static List<IActivity> formAdministratorPermissions = new ArrayList<IActivity>();
    private static List<IActivity> blockAdministratorPermissions = new ArrayList<IActivity>();
    private static List<IActivity> applicationAdministratorPermissions = new ArrayList<IActivity>();
    private static List<IActivity> webServiceUserPermissions = new ArrayList<IActivity>();

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
        for (RestServiceActivity activity : WEB_SERVICES_PERMISSIONS) {
            webServiceUserPermissions.add(activity);
        }
    }

    /**
     * Get activities associated to a role that has been assigned to the user.
     */
    @Override
    public Set<IActivity> getRoleActivities(IRole<Long> role) {
        if (role != null) {
            return getRoleActivities(role.getUniqueName());
        }
        return new HashSet<>();
    }

    @Override
    public Set<IActivity> getRoleActivities(String roleName) {
        Set<IActivity> activities = new HashSet<>();
        WebformsRoles webFormRole = WebformsRoles.parseTag(roleName);
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
            case WEB_SERVICE_USER:
                activities.addAll(webServiceUserPermissions);
                break;
        }
        return activities;
    }

}
