package com.quocnam.myapplication.model.dao;

import com.quocnam.myapplication.interfaces.IDataGet;
import com.quocnam.myapplication.interfaces.IDataUpdateAutoIncrement;
import com.quocnam.myapplication.model.dto.UserDTO;
import com.quocnam.myapplication.utils.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserDAO implements IDataGet<Long, UserDTO>, IDataUpdateAutoIncrement<Long, UserDTO> {
    @Override
    public ArrayList<UserDTO> gets() {
        ArrayList<UserDTO> result = new ArrayList<>();

        String query = "SELECT * FROM USER;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, null);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                UserDTO userModel = new UserDTO(resultSet);
                result.add(userModel);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    @Override
    public UserDTO getById(Long id) {
        String query = "SELECT * FROM USER WHERE USER_ID = " + id + ";";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, null);

        try {
            if (resultSet != null && resultSet.next()) {
                return new UserDTO(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public Long insert(UserDTO dto) {
        String sql = "INSERT INTO USER(USER_NAME, PASSWORD, USER_TYPE)" +
                "VALUES (?, ?, ?);";

        List<Object> parameters = Arrays.asList(
                dto.getUserName(),
                dto.getPassword(),
                dto.getUserType()
        );
        return (Long) DatabaseUtils.executeUpdateAutoIncrement(sql, parameters);
    }

    @Override
    public int update(UserDTO dto) {
        String sql = "UPDATE USER SET USER_NAME = ?, PASSWORD = ?, USER_TYPE = ?, STATUS = ? WHERE USER_ID = ?";
        List<Object> parameters = Arrays.asList(
                dto.getUserName(),
                dto.getPassword(),
                dto.getUserType(),
                dto.getStatus(),
                dto.getUserId()
        );
        return DatabaseUtils.executeUpdate(sql, parameters);
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM USER WHERE USER_ID = ?";
        List<Object> parameters = Collections.singletonList(id);
        return DatabaseUtils.executeUpdate(sql, parameters);
    }

    private static UserDAO instance = null;

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }
}
