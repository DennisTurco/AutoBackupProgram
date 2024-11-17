package test;

import com.mycompany.autobackupprogram.JSONAutoBackup;
import com.mycompany.autobackupprogram.Interfaces.IJSONAutoBackup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class TestJSONAutoBackup {

    private IJSONAutoBackup json;

    @BeforeEach
    void setup() {
        json = new JSONAutoBackup();
    }

    @AfterEach
    void tearDown() {
        json = null;
    }
}