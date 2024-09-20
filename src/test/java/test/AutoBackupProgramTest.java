//package com.mycompany.autobackupprogram;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.DisplayName;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class AutoBackupProgramTest {
//
//    private AutoBackupProgram program;
//
//    @BeforeEach
//    void setup() {
//        program = new AutoBackupProgram();
//    }
//
//    @AfterEach
//    void tearDown() {
//        program = null;
//    }
//
//    @Test
//    @DisplayName("Test clear method")
//    void testClear() {
//        program.Clear();
//        assertEquals(0, program.getFiles().size());
//    }
//
//    @Test
//    @DisplayName("Test exit method")
//    void testExit() {
//        program.Exit();
//        assertTrue(program.isExited());
//    }
//
//    @Test
//    @DisplayName("Test help method")
//    void testHelp() {
//        program.Help();
//        assertTrue(program.isHelpDisplayed());
//    }
//
//    @Test
//    @DisplayName("Test credits method")
//    void testCredits() {
//        program.Credits();
//        assertTrue(program.isCreditsDisplayed());
//    }
//
//    @Test
//    @DisplayName("Test share method")
//    void testShare() {
//        program.Share();
//        assertTrue(program.isShared());
//    }
//
//    @Test
//    @DisplayName("Test view history method")
//    void testViewHistory() {
//        program.ViewHistory();
//        assertTrue(program.isHistoryViewed());
//    }
//
//    @Test
//    @DisplayName("Test new file method")
//    void testNewFile() {
//        program.NewFile();
//        assertTrue(program.isFileCreated());
//    }
//
//    @Test
//    @DisplayName("Test remove single file method")
//    void testRemoveSingleFile() {
//        program.RemoveSingleFile();
//        assertTrue(program.isFileRemoved());
//    }
//
//    @Test
//    @DisplayName("Test save with name method")
//    void testSaveWithName() {
//        program.SaveWithName();
//        assertTrue(program.isSavedWithName());
//    }
//
//    @Test
//    @DisplayName("Test save method")
//    void testSave() {
//        program.Save();
//        assertTrue(program.isSaved());
//    }
//
//    @Test
//    @DisplayName("Test backup list method")
//    void testBackupList() {
//        program.BackupList();
//        assertTrue(program.isBackupListDisplayed());
//    }
//
//    @Test
//    @DisplayName("Test single backup method")
//    void testSingleBackup() {
//        program.SingleBackup();
//        assertTrue(program.isSingleBackupPerformed());
//    }
//
//    @Test
//    @DisplayName("Test automatic backup method")
//    void testAutomaticBackup() {
//        program.AutomaticBackup();
//        assertTrue(program.isAutomaticBackupPerformed());
//    }
//
//    @Test
//    @DisplayName("Test change BTN auto backup option method")
//    void testChangeBTNAutoBackupOption() {
//        program.ChangeBTNAutoBackupOption();
//        assertTrue(program.isAutoBackupOptionChanged());
//    }
//
//    @Test
//    @DisplayName("Test get string to text method")
//    void testGetStringToText() {
//        program.GetStringToText();
//        assertNotNull(program.getStringToText());
//    }
//
//    @Test
//    @DisplayName("Test check input correct method")
//    void testCheckInputCorrect() {
//        program.checkInputCorrect();
//        assertTrue(program.isInputCorrect());
//    }
//}
