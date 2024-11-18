package com.mycompany.autobackupprogram.Entities;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.autobackupprogram.Logger;
import com.mycompany.autobackupprogram.Logger.LogLevel;
import com.mycompany.autobackupprogram.Enums.ConfigKey;
import com.mycompany.autobackupprogram.Enums.LanguagesEnum;
import com.mycompany.autobackupprogram.Enums.ThemesEnum;

public class Preferences {
    private static LanguagesEnum language = LanguagesEnum.ENG;
    private static ThemesEnum theme = ThemesEnum.INTELLIJ;

    public static void loadPreferencesFromJSON() {
        try (FileReader reader = new FileReader(ConfigKey.CONFIG_DIRECTORY_STRING.getValue() + ConfigKey.PREFERENCES_FILE_STRING.getValue())) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
    
            // Map the "Language" JSON value to the LanguagesEnum
            String languageFileName = jsonObject.get("Language").getAsString();
            for (LanguagesEnum lang : LanguagesEnum.values()) {
                if (lang.getFileName().equals(languageFileName)) {
                    language = lang;
                    break;
                }
            }
            
            // Map the "Theme" JSON value to the ThemesEnum
            String themeName = jsonObject.get("Theme").getAsString();
            for (ThemesEnum t : ThemesEnum.values()) {
                if (t.getThemeName().equals(themeName)) {
                    theme = t;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePreferencesToJSON() {
        try (FileWriter writer = new FileWriter(ConfigKey.CONFIG_DIRECTORY_STRING.getValue() + ConfigKey.PREFERENCES_FILE_STRING.getValue())) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("Language", language.getFileName());
            jsonObject.addProperty("Theme", theme.getThemeName());

            // Convert JsonObject to JSON string using Gson
            Gson gson = new Gson();
            gson.toJson(jsonObject, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LanguagesEnum getLanguage() {
        return language;
    }
    public static ThemesEnum getTheme() {
        return theme;
    }
    public static void setLanguage(LanguagesEnum language) {
        Preferences.language = language;
    }
    public static void setTheme(ThemesEnum theme) {
        Preferences.theme = theme;
    }
    public static void setLanguage(String selectedLanguage) {
        try {
            for (LanguagesEnum lang : LanguagesEnum.values()) {
                if (lang.getLanguageName().equalsIgnoreCase(selectedLanguage)) {
                    language = lang;
                    Logger.logMessage("Language set to: " + language.getLanguageName(), LogLevel.INFO);
                    return;
                }
            }
            Logger.logMessage("Invalid language name: " + selectedLanguage, LogLevel.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setTheme(String selectedTheme) {
        try {
            for (ThemesEnum t : ThemesEnum.values()) {
                if (t.getThemeName().equalsIgnoreCase(selectedTheme)) {
                    theme = t;
                    Logger.logMessage("Theme set to: " + theme.getThemeName(), LogLevel.INFO);
                    return;
                }
            }
            Logger.logMessage("Invalid theme name: " + selectedTheme, LogLevel.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
