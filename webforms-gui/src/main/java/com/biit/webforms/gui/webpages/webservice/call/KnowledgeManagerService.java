package com.biit.webforms.gui.webpages.webservice.call;

import com.biit.usermanager.entity.IUser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.io.IOException;

public class KnowledgeManagerService {

    @Autowired
    private Environment environment;

    private final String LOGIN_URL = "/api/public/login";
    private final String PUBLISH_URL = "/forms/addForm";

    public KnowledgeManagerService() {

    }
    public CloseableHttpResponse login(String username, String password) {
        String baseUrl = environment.getProperty("knowledge-manager.url");
        System.out.println(baseUrl);
        String jsonInputString = "{\"username\":" + "\"" + username + "\"" + ",\"password\":" + "\"" + password + "\"" + "}";
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(baseUrl + LOGIN_URL);

            httpPost.setEntity(new StringEntity(jsonInputString));
            httpPost.setHeader("Content-Type", "application/json");
            return client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int publishToKnowledgeManager(String value, String authToken, String email) {
        String baseUrl = environment.getProperty("knowledge-manager.url");
        System.out.println(baseUrl);
        String jsonInputString = "{\"value\":" + value + ",\"email\":" + email + "}";
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(baseUrl + PUBLISH_URL);

            httpPost.setEntity(new StringEntity(jsonInputString));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + authToken);
            CloseableHttpResponse response = client.execute(httpPost);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            return 0;
        }
    }
}
