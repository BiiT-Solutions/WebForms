package com.biit.webforms.rest.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.webforms.security.ISecurityService;
import com.biit.webservice.rest.RestAuthorizationService;
import com.biit.webservice.rest.RestServiceActivity;

@Component
public class WebformsRestAuthorizationService extends RestAuthorizationService {

	@Autowired
	private ISecurityService securityService;

	@Override
	public Boolean checkSpecificAuthorization(IUser<Long> user) {
		try {
			return securityService.isUserAuthorizedInAnyOrganization(user, RestServiceActivity.USE_WEB_SERVICE);
		} catch (IOException | AuthenticationRequired e) {
			return false;
		}
	}

	@Override
	public IAuthenticationService<Long, Long> getAuthenticationService() {
		return securityService.getAuthenticationService();
	}
}
