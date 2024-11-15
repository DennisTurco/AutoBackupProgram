package com.mycompany.autobackupprogram.Enums;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PreferencesLoader {

    public enum PreferencesKey {
        LANGUAGE("Language", "eng.json"),
        THEME("Theme", "light");

        private final String keyName;
        private String value;
        private final String defaultValue;

        private static final Map<String, PreferencesKey> lookup = new HashMap<>();

        static {
            for (PreferencesKey key : PreferencesKey.values()) {
                lookup.put(key.keyName, key);
            }
        }

        PreferencesKey(String keyName, String defaultValue) {
            this.keyName = keyName;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        public String getKeyName() {
            return keyName;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static PreferencesKey fromKeyName(String keyName) {
            return lookup.get(keyName);
        }

        @Override
        public String toString() {
            return keyName + ": " + value;
        }
    }

    public static void loadPreferences(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            for (Map.Entry<String, ?> entry : jsonObject.entrySet()) {
                String keyName = entry.getKey();
                String value = jsonObject.get(keyName).getAsString();

                PreferencesKey prefKey = PreferencesKey.fromKeyName(keyName);
                if (prefKey != null) {
                    prefKey.setValue(value); 
                }
            }
        }
    }

    // only for test
    public static void main(String[] args) {
        try {
            loadPreferences("src/main/resources/res/config/preferences.json");

            System.out.println(PreferencesKey.LANGUAGE);
            System.out.println(PreferencesKey.THEME);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
