package com.wliafdew.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name="password")
    private  String password;

    @Column(name = "fristname")
    private String fristname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "isactive")
    private  boolean isactive;

    @Column(name = "createdat")
    private Date createdat;

    @Column(name = "updatedat")
    private Date updatedat;

    public users() {

    }

    public users(UUID id, String email, String password, String fristname, String lastname, String avatar,
            boolean isactive, Date createdat, Date updatedat) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fristname = fristname;
        this.lastname = lastname;
        this.avatar = avatar;
        this.isactive = isactive;
        this.createdat = createdat;
        this.updatedat = updatedat;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFristname() {
        return fristname;
    }

    public void setFristname(String fristname) {
        this.fristname = fristname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("users{");
        sb.append("id=").append(id);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", fristname=").append(fristname);
        sb.append(", lastname=").append(lastname);
        sb.append(", avatar=").append(avatar);
        sb.append(", isactive=").append(isactive);
        sb.append(", createdat=").append(createdat);
        sb.append(", updatedat=").append(updatedat);
        sb.append('}');
        return sb.toString();
    }
}
