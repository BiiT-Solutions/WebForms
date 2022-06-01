package com.biit.webforms.webservice.rest.client;

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



    public int login(String username, String password, Long userId) {
        String url = WebformsConfigurationReader.getInstance().getKnowledgeManagerServiceLoginUrl();
        String jsonInputString = "{\"username\":" + "\"" + username + "\"" + ",\"password\":" + "\"" + password + "\"" + "}";
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity(jsonInputString));
            httpPost.setHeader("Content-Type", "application/json");
            CloseableHttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                UserToken userToken = new UserToken();
                userToken.setUserId(userId);
                userToken.setKnowledgeManagerAuthToken(response.getFirstHeader("Authorization").getValue());
                userTokenDao.merge(userToken);
            }
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
            return 500;
        }
    }

    public int publishToKnowledgeManager(String value, String authToken, String email) {
        String url = WebformsConfigurationReader.getInstance().getKnowledgeManagerServicePublishUrl();
        String jsonInputString = "{\"value\":" + value + ",\"email\":" + email + "}";
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new StringEntity(jsonInputString));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + authToken);
            CloseableHttpResponse response = client.execute(httpPost);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            return 500;
        }
    }
}
