package com.senith.smartcampus.model;

import java.util.Map;

public class ApiInfo {
    private String apiName;
    private String version;
    private String adminContact;
    private Map<String, String> resources;

    public ApiInfo() {

    }

    public ApiInfo(String apiName, String version, String adminContact, Map<String, String> resources) {
        this.apiName = apiName;
        this.version = version;
        this.adminContact = adminContact;
        this.resources = resources;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(String adminContact) {
        this.adminContact = adminContact;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }
}






