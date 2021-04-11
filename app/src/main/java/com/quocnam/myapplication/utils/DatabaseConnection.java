package com.quocnam.myapplication.utils;

import android.os.AsyncTask;

import com.quocnam.myapplication.model.dto.UserDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection extends AsyncTask<Query, Void, ResultSet> {

   final static String url = "jdbc:mysql://192.168.1.153:3306/kidMonitoringApplicationDb";
   final static String username = "andro";
   final static String password = "andro";

   ArrayList<UserDTO> result;

   public DatabaseConnection(ArrayList<UserDTO> out){
      result = out;
   }

   private static ResultSet getConnection() {
      return null;
   }

   private static void setParameters(PreparedStatement prepStmt, List<Object> parameters) {
      if (parameters == null) {
         return;
      }
      try {
         for (int i = 0; i < parameters.size(); i++) {
            Object para = parameters.get(i);

            if (para instanceof Boolean) prepStmt.setBoolean(i + 1, (Boolean) para);
            else if (para instanceof Byte) prepStmt.setByte(i + 1, (Byte) para);
            else if (para instanceof Integer) prepStmt.setInt(i + 1, (Integer) para);
            else if (para instanceof Float) prepStmt.setFloat(i + 1, (Float) para);
            else if (para instanceof Long) prepStmt.setLong(i + 1, (Long) para);
            else if (para instanceof Double) prepStmt.setDouble(i + 1, (Double) para);
            else if (para instanceof BigDecimal) prepStmt.setBigDecimal(i + 1, (BigDecimal) para);
            else if (para instanceof String) prepStmt.setString(i + 1, (String) para);
         }
      } catch (SQLException exception) {
         exception.printStackTrace();
      }
   }

   @Override
   protected ResultSet doInBackground(Query... queries) {
      Connection conn = null;
      try {
         Class.forName("com.mysql.jdbc.Driver");
         conn = DriverManager.getConnection(url, username, password);
      } catch (SQLException | ClassNotFoundException e) {
         e.printStackTrace();
      }

      PreparedStatement prepStmt;
      ResultSet rs = null;

      if (conn == null) {
         return null;
      }
      try {
         prepStmt = conn.prepareStatement(queries[0].getSqlQuery());
         if (queries[0].getParameters() != null) {
            setParameters(prepStmt, queries[0].getParameters());
         }
         rs = prepStmt.executeQuery(queries[0].getSqlQuery());
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return rs;
   }

   @Override
   protected void onPostExecute(ResultSet resultSet) {
      //super.onPostExecute(resultSet);
      //delegate.processFinish(resultSet);

      try {
         while (resultSet.next()) {
            UserDTO userModel = new UserDTO(resultSet);
            result.add(userModel);
         }
      } catch (SQLException exception) {
         exception.printStackTrace();
      }
   }
}
