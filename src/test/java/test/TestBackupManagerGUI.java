package test;

import com.mycompany.autobackupprogram.BackupManagerGUI;

import org.junit.jupiter.api.BeforeEach;
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