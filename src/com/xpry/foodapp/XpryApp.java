package com.xpry.foodapp;

//XpryApp.java - Main Launcher
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class XpryApp {
 public static void main(String[] args) {
     try {
         UIManager.setLookAndFeel(new FlatLightLaf());
     } catch (Exception e) {
         System.err.println("FlatLaf setup failed");
     }

     javax.swing.SwingUtilities.invokeLater(() -> new DashboardGUI());
 }
}
