package test;

import org.junit.jupiter.api.BeforeEach;

import com.mycompany.autobackupprogram.GUI.BackupManagerGUI;

import org.junit.jupiter.api.AfterEach;

public class TestBackupManagerGUI {

    private BackupManagerGUI program;

    @BeforeEach
    void setup() {
        program = new BackupManagerGUI();
    }

    @AfterEach
    void tearDown() {
        program = null;
    }
}