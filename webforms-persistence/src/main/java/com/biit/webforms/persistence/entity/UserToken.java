package com.biit.webforms.persistence.entity;

import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_tokens")
public class UserToken {

    @Id
    Long userId;

    @Column(name = "knowledge_manager_auth_token")
    String knowledgeManagerAuthToken;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKnowledgeManagerAuthToken() {
        return this.knowledgeManagerAuthToken;
    }

    public void setKnowledgeManagerAuthToken(String knowledgeManagerAuthToken) {
        this.knowledgeManagerAuthToken = knowledgeManagerAuthToken;
    }
}
