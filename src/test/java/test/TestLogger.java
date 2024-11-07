//package test;
//
//import com.mycompany.autobackupprogram.JSONConfigReader;
//import com.mycompany.autobackupprogram.Logger;
//import org.junit.jupiter.api.*;
//import org.mockito.*;
//
//import java.io.*;
//import java.nio.file.*;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class TestLogger {
//
//    private static final String TEST_LOG_PATH = "src/test/resources/log_test";
//    private static final String TEST_JSON_PATH = "src/test/resources/config_test.json";
//    
//    @Mock
//    private static JSONConfigReader mockConfigReader;
//    
//    @BeforeAll
//    static void setUpBeforeClass() throws IOException {
//        // Create test configuration file
//        String jsonContent = """
//                             {
//                             "LogService": {
//                                 "ERROR": true,
//                                 "INFO": true,
//                                 "WARN": true,
//                                 "DEBUG": true,
//                                 "MaxLines": {
//                                   "value": 1500,
//                                   "type": "int",
//                                   "description": "Maximum number of logs for the file"
//                                 },
//                                 "LinesToKeepAfterFileClear": {
//                                   "value": 150,
//                                   "type": "int",
//                                   "description": "Lines to keep after file clear"
//                                 }
//                             }
//                             }""";
//        Files.write(Paths.get(TEST_JSON_PATH), jsonContent.getBytes());
//
//        // Set up the mock config reader
//        mockConfigReader = mock(JSONConfigReader.class);
//        when(mockConfigReader.getMaxLines()).thenReturn(100);
//        when(mockConfigReader.getLinesToKeepAfterFileClear()).thenReturn(50);
//        when(mockConfigReader.isLogLevelEnabled("INFO")).thenReturn(true);
//        when(mockConfigReader.isLogLevelEnabled("DEBUG")).thenReturn(true);
//        when(mockConfigReader.isLogLevelEnabled("WARN")).thenReturn(true);
//        when(mockConfigReader.isLogLevelEnabled("ERROR")).thenReturn(true);
//
//        Logger.configReader = mockConfigReader;
//    }
//
//    @BeforeEach
//    void setup() {
//        // Reset the console logging flag before each test
//        Logger.setConsoleLoggingEnabled(false);
//    }
//
//    @Test
//    void testLogMessageInfoLevel() throws IOException {
//        Logger.logMessage("Test info message", Logger.LogLevel.INFO);
//
//        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_PATH));
//        System.out.println(lines.toString());
//        assertTrue(lines.stream().anyMatch(line -> line.contains("INFO")));
//    }
//
//    @Test
//    void testLogMessageDebugLevel() throws IOException {
//        Logger.logMessage("Test debug message", Logger.LogLevel.DEBUG);
//
//        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_PATH));
//        assertTrue(lines.stream().anyMatch(line -> line.contains("DEBUG") && line.contains("Test debug message")));
//    }
//
//    @Test
//    void testLogMessageErrorLevel() throws IOException {
//        Logger.logMessage("Test error message", Logger.LogLevel.ERROR);
//
//        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_PATH));
//        assertTrue(lines.stream().anyMatch(line -> line.contains("ERROR") && line.contains("Test error message")));
//    }
//
//    @Test
//    void testLogMessageWithException() throws IOException {
//        Exception testException = new Exception("Test exception");
//        Logger.logMessage("Test message with exception", Logger.LogLevel.ERROR, testException);
//
//        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_PATH));
//        assertTrue(lines.stream().anyMatch(line -> line.contains("ERROR") && line.contains("Test message with exception")));
//        assertTrue(lines.stream().anyMatch(line -> line.contains("Exception: java.lang.Exception - Test exception")));
//    }
//
//    @Test
//    void testConsoleLoggingEnabled() {
//        Logger.setConsoleLoggingEnabled(true);
//
//        // Capture the console output
//        PrintStream originalOut = System.out;
//        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(consoleOutput));
//
//        Logger.logMessage("Test console logging", Logger.LogLevel.INFO);
//
//        assertTrue(consoleOutput.toString().contains("Test console logging"));
//
//        // Reset the console output
//        System.setOut(originalOut);
//    }
//
//    @Test
//    void testFileLoggingWithMaxLines() throws IOException {
//        // Create a large number of log entries to test maxLines
//        for (int i = 0; i < 200; i++) {
//            Logger.logMessage("Log entry " + i, Logger.LogLevel.INFO);
//        }
//
//        List<String> lines = Files.readAllLines(Paths.get(TEST_LOG_PATH));
//        assertEquals(100, lines.size()); // After trimming, only 100 lines should remain
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        // Clean up log file after each test
//        Files.deleteIfExists(Paths.get(TEST_LOG_PATH));
//    }
//
//    @AfterAll
//    static void tearDownAfterClass() throws IOException {
//        // Clean up the config file after all tests
//        Files.deleteIfExists(Paths.get(TEST_JSON_PATH));
//    }
//}
