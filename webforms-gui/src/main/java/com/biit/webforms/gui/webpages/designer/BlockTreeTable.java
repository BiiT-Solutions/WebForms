package com.biit.webforms.gui.webpages.designer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.biit.form.TreeObject;
import com.biit.webforms.authentication.UserSessionHandler;
import com.biit.webforms.authentication.WebformsActivity;
import com.biit.webforms.authentication.WebformsAuthorizationService;
import com.biit.webforms.gui.common.utils.MessageManager;
import com.biit.webforms.gui.common.utils.SpringContextHelper;
import com.biit.webforms.gui.components.TableTreeObjectLabel;
import com.biit.webforms.gui.webpages.blockmanager.TreeObjectUpdateDateComparator;
import com.biit.webforms.language.LanguageCodes;
import com.biit.webforms.persistence.dao.IBlockDao;
import com.biit.webforms.persistence.entity.Block;
import com.biit.webforms.persistence.entity.Form;
import com.liferay.portal.model.Organization;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;

public class BlockTreeTable extends TableTreeObjectLabel {

	private static final long serialVersionUID = 2428935033753125285L;

	private IBlockDao blockDao;

	private enum BlockTreeTableProperties {
		ORGANIZATION,
	};

	public BlockTreeTable() {
		super();
		// Add Vaadin conext to Spring, and get beans for DAOs.
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		blockDao = (IBlockDao) helper.getBean("blockDao");

		addContainerProperty(BlockTreeTableProperties.ORGANIZATION, String.class, null,
				LanguageCodes.CAPTION_ORGANIZATION.translation(), null, Align.LEFT);

		initializeBlockTable();
	}

	private void initializeBlockTable() {
		Set<Organization> organizations = WebformsAuthorizationService.getInstance()
				.getUserOrganizationsWhereIsAuthorized(UserSessionHandler.getUser(), WebformsActivity.READ);
		List<Block> blocks = new ArrayList<>();
		try {
			for (Organization organization : organizations) {
				blocks.addAll(blockDao.getAll(organization.getOrganizationId()));
			}
			Collections.sort(blocks, new TreeObjectUpdateDateComparator());

			for (Block block : blocks) {
				loadTreeObject(block, null);
				setCollapsed(block, true);
			}
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Item addRow(TreeObject element, TreeObject parent) {
		// Add the name of the organization if the row contains an instance of
		// Form.
		Item item = super.addRow(element, parent);
		if (item != null) {
			if (element instanceof Form) {
				Long organizationId = ((Form) element).getOrganizationId();
				Organization organization = WebformsAuthorizationService.getInstance().getOrganization(
						UserSessionHandler.getUser(), organizationId);
				if (organization != null) {
					item.getItemProperty(BlockTreeTableProperties.ORGANIZATION).setValue(organization.getName());
				}
			}
		}
		return item;
	}
}
