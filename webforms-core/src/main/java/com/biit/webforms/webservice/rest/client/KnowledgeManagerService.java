package com.biit.webforms.webservice.rest.client;

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

import com.biit.webforms.configuration.WebformsConfigurationReader;
import com.biit.webforms.persistence.dao.IUserTokenDao;
import com.biit.webforms.persistence.entity.UserToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KnowledgeManagerService {

    @Autowired
    private IUserTokenDao userTokenDao;



    public CloseableHttpResponse login(String username, String password, Long userId) throws IOException{
        String url = WebformsConfigurationReader.getInstance().getKnowledgeManagerServiceLoginUrl();
        String jsonInputString = "{\"username\":" + "\"" + username + "\"" + ",\"password\":" + "\"" + password + "\"" + "}";
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity(jsonInputString));
            httpPost.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 300) {
                UserToken userToken = new UserToken();
                userToken.setUserId(userId);
                userToken.setKnowledgeManagerAuthToken(response.getFirstHeader("Authorization").getValue());
                userTokenDao.merge(userToken);
            }
            return response;
    }

    public CloseableHttpResponse publishToKnowledgeManager(String value, String authToken, String email) throws IOException{
        String url = WebformsConfigurationReader.getInstance().getKnowledgeManagerServicePublishUrl();
        String jsonInputString = "{\"value\":" + value + ",\"email\":" + email + "}";
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity(jsonInputString));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + authToken);
            CloseableHttpResponse response = client.execute(httpPost);
            return response;
    }
}
