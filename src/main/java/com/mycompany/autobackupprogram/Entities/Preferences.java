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

public class Preferences {
    private static LanguagesEnum language = LanguagesEnum.ENG;
    private static String theme = "light";

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
            
            theme = jsonObject.get("Theme").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePreferencesToJSON() {
        try (FileWriter writer = new FileWriter(ConfigKey.CONFIG_DIRECTORY_STRING.getValue() + ConfigKey.PREFERENCES_FILE_STRING.getValue())) {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("Language", language.getFileName());
            jsonObject.addProperty("Theme", theme);

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
    public static String getTheme() {
        return theme;
    }
    public static void setLanguage(LanguagesEnum language) {
        Preferences.language = language;
    }
    public static void setTheme(String theme) {
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

    // for test
    public static void main(String[] args) {
        String CONFIG = "src/main/resources/res/config/config.json";
        ConfigKey.loadFromJson(CONFIG);
        loadPreferencesFromJSON();
        updatePreferencesToJSON();
    }

}
