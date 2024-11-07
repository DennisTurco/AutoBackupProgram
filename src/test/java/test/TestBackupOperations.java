package test;

import com.mycompany.autobackupprogram.BackupOperations;
import com.mycompany.autobackupprogram.JSONConfigReader;
import com.mycompany.autobackupprogram.Logger;
import com.mycompany.autobackupprogram.MainApp;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class TestBackupOperations {
    
    private static final File TEST_LOG_PATH = new File("src/test/resources/log_test");

    @Mock
    private static JSONConfigReader mockConfigReader;

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        // Create test configuration file
        if (Files.notExists(TEST_LOG_PATH.toPath())) {
            Files.createFile(TEST_LOG_PATH.toPath());
        } else {
            Files.write(TEST_LOG_PATH.toPath(), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
        }

        // Set up the mock config reader
        mockConfigReader = mock(JSONConfigReader.class);
        when(mockConfigReader.getMaxLines()).thenReturn(100);
        when(mockConfigReader.getLinesToKeepAfterFileClear()).thenReturn(50);
        when(mockConfigReader.isLogLevelEnabled("INFO")).thenReturn(true);
        when(mockConfigReader.isLogLevelEnabled("DEBUG")).thenReturn(true);
        when(mockConfigReader.isLogLevelEnabled("WARN")).thenReturn(true);
        when(mockConfigReader.isLogLevelEnabled("ERROR")).thenReturn(true);

        Logger.configReader = mockConfigReader;

        Logger.setLogFilePath(TEST_LOG_PATH.getPath());
    }

    @BeforeEach
    void setup() throws IOException {
        // Reset the console logging flag before each test
        if (Files.notExists(TEST_LOG_PATH.toPath())) {
            Files.createFile(TEST_LOG_PATH.toPath());
        } else {
            Files.write(TEST_LOG_PATH.toPath(), new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
        }
        Logger.setConsoleLoggingEnabled(false);
    }

    @Test
    void testCheckInputCorrectWrongFilePaths() {
        String path1 = "/wrong/path/file.txt";
        String path2 = "/wrong/path/dir";    
        assertFalse(BackupOperations.CheckInputCorrect("backup", path1, path2, null));
    }

    @Test
    void testCheckInputCorrectCorrectFilePaths() throws IOException {
        File tempFile1 = File.createTempFile("file1", ".txt");
        File tempFile2 = File.createTempFile("file2", ".txt");  
        assertTrue(BackupOperations.CheckInputCorrect("backup", tempFile1.getPath(), tempFile2.getPath(), null));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up log file after each test
        Files.deleteIfExists(TEST_LOG_PATH.toPath());
    }
}
