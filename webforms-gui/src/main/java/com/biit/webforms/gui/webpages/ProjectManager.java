package com.biit.webforms.gui.webpages;

import java.util.List;

import com.biit.liferay.security.IActivity;
import com.biit.webforms.gui.common.components.SecuredWebPage;
import com.biit.webforms.gui.components.TreeTableFormVersion;

public class ProjectManager extends SecuredWebPage {

	private static final long serialVersionUID = 4853622392162188013L;

	private TreeTableFormVersion treeTableFormVersion;

	@Override
	protected void initContent() {
		addCentralPanel();

		treeTableFormVersion = new TreeTableFormVersion();
		treeTableFormVersion.setSizeFull();
		getWorkingArea().addComponent(treeTableFormVersion);
	}

	@Override
	public List<IActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

}
