package com.xpry.foodapp;

//FoodDAO.java - DB Operations
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;


public class FoodDAO {
 public List<FoodItem> getAllItems() {
     List<FoodItem> list = new ArrayList<>();
     try (Connection conn = DBConnection.getConnection();
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery("SELECT * FROM food_items")) {
         while (rs.next()) {
             int id = rs.getInt("id");
             String name = rs.getString("name");
             LocalDate date = rs.getDate("expiry_date").toLocalDate();
             list.add(new FoodItem(id, name, date));
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
     return list;
 }

 public void addItem(String name, LocalDate expiryDate) {
     String sql = "INSERT INTO food_items (name, expiry_date) VALUES (?, ?)";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setString(1, name);
         ps.setDate(2, Date.valueOf(expiryDate));
         ps.executeUpdate();
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }

 public void deleteItem(int id) {
     String sql = "DELETE FROM food_items WHERE id = ?";
     try (Connection conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql)) {
         ps.setInt(1, id);
         ps.executeUpdate();
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
}
