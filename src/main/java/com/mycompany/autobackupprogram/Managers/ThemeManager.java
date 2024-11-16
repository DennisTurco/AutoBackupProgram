package com.mycompany.autobackupprogram.Managers;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.mycompany.autobackupprogram.Entities.Preferences;

public class ThemeManager {

    public static void updateThemeFrame(Frame frame) {
        updateTheme();

        // Update all components and Revalidate and repaint
        SwingUtilities.updateComponentTreeUI(frame);
        frame.revalidate();
        frame.repaint();
    }

    public static void updateThemeDialog(Dialog dialog) {
        updateTheme();
        
        // Update all components and Revalidate and repaint
        SwingUtilities.updateComponentTreeUI(dialog);
        dialog.revalidate();
        dialog.repaint();
    }

    private static void updateTheme() {
        try {
            String selectedTheme = Preferences.getTheme().getThemeName();

            switch (selectedTheme.toLowerCase()) {
                case "light":
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                case "dark":
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case "intellij":
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    break;
                case "darcula":
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    break;
                case "macos light":
                    UIManager.setLookAndFeel(new FlatMacLightLaf());
                    break;
                case "macos dark":
                    UIManager.setLookAndFeel(new FlatMacDarkLaf());
                    break;
                default:
                    // If no match, apply the default theme
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
            }

        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Error setting LookAndFeel: " + ex.getMessage());
        }
    }
}