package com.quocnam.myapplication.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDTO {
    Long userId;
    String userName;
    String password;
    String userType;
    Boolean status;

    public UserDTO(ResultSet resultSet) {
        try {
            userId = resultSet.getLong("USER_ID");
            userName = resultSet.getString("USER_NAME");
            password = resultSet.getString("PASSWORD");
            userType = resultSet.getString("USER_TYPE");
            status = resultSet.getBoolean("STATUS");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public UserDTO(Long userId, String userName, String password, String userType, Boolean status) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
