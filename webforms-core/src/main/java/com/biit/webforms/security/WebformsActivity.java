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

import com.biit.usermanager.security.IActivity;

/**
 * Activities are used for authorization system to define what a user can do and
 * cannot do. The string for each activity must be unique.
 */
public enum WebformsActivity implements IActivity {
	READ("Read"),

	BUILDING_BLOCK_EDITING("BuildingBlockEditing"),

	BUILDING_BLOCK_FLOW_EDITING("BuildingBlockFLowEditing"),

	BUILDING_BLOCK_ADD_FROM_FORM("BuildingBlockAdd"),

	FORM_EDITING("FormEditing"),

	FORM_FLOW_EDITING("FormFlowEditing"),

	FORM_STATUS_UPGRADE("FormStatusUpgrade"),

	FORM_STATUS_DOWNGRADE("FormStatusDowngrade"),

	FORM_NEW_VERSION("CreateNewFormVersion"),

	XFORMS_EXPORT("XFormsExport"),

	XFORMS_PUBLISH("XFormsPublish"),

	XML_VALIDATOR_AGAINST_FORM("XmlValidatorAgainstForm"),

	FORM_ANALYSIS("FormAnalysis"),

	ADMIN_FORMS("AdminRights"),

	EVICT_CACHE("EvictCache"),

	FORM_REMOVE("FormRemove"),

	BLOCK_REMOVE("BlockRemove"),

	IMPORT_JSON("ImportJson"),

	EXPORT_JSON("ExportJson"),

	EXPORT_ABCD("ExportAbcd"),
	
	EXPORT_SCORECARD_XLS("ExportScorecardXls"),

	EXPORT_TO_CSV("ExportToCsv"),

	PUBLISH_TO_KNOWLEDGE_MANAGER("PublishToKnowledgeManager"),

	;

	private String tag;

	WebformsActivity(String tag) {
		this.tag = tag;
	}

	@Override
	public String getTag() {
		return tag;
	}

}
