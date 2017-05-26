package com.mackersD.jrgisync.rest;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdminModel {
    
    @XmlElement(name = "defaultProject")
    private String defaultProject;
    
    @XmlElement(name = "defaultUser")
    private String defaultUser;
    
    @XmlElement(name = "preSharedSecret")
    private byte[] preSharedSecret;

    public AdminModel() {
    }

    public String getDefaultProject() {
        return this.defaultProject;
    }

    public void setDefaultProject(String project) {
        this.defaultProject = project;
    }
    
    public String getDefaultUser() {
        return this.defaultUser;
    }
    
    public void setDefaultUser(String user) {
        this.defaultUser = user;
    }
    
    public byte[] getPreSharedSecret() {
        return this.preSharedSecret;
    }
    
    public void setPreSharedSecret(byte[] secret) {
        this.preSharedSecret = secret;
    }
}