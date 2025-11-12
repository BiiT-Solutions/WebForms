package com.biit.webforms.gui.common.utils;

/*-
 * #%L
 * Pilot Agile Testing for WebForms (GUI)
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

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * As suggested in https://vaadin.com/wiki/-/wiki/Main/Spring%20Integration, to access your Spring managed beans, you
 * will need a helper class capable of using your Vaadin Application class to get the relevant session information, and
 * thus the Spring context:
 */
public class SpringContextHelper {

	private ApplicationContext context;

	public SpringContextHelper(ServletContext servletContext) {
		context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}

	public ApplicationContext getContext() {
		return context;
	}

	public Object getBean(final String beanRef) {
		return context.getBean(beanRef);
	}
}
