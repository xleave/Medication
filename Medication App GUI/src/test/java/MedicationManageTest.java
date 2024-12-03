// // Test Case 1: Successful save
// @Test
// public void testSaveMedicationInfo_SuccessfulSave() throws Exception {
//     // Arrange
//     String targetUserName = "testUser";
//     JTextField[] textFields = {
//         new JTextField("Medication1"),
//         new JTextField("Dosage1"),
//         new JTextField("Frequency1"),
//         new JTextField("Route1"),
//         new JTextField("Notes1"),
//         new JTextField("Duration1")
//     };
//     JButton acceptButton = new JButton();
//     JFrame frame = mock(JFrame.class);

//     // Ensure medications directory exists
//     File medicationsDir = new File("src/main/resources/medications");
//     medicationsDir.mkdirs();

//     // Delete the file if it already exists
//     File medicationFile = new File(medicationsDir, targetUserName + "_medications.csv");
//     if (medicationFile.exists()) {
//         medicationFile.delete();
//     }

//     // Act
//     MedicationManage.saveMedicationInfo(targetUserName, textFields, acceptButton, frame);

//     // Assert
//     assertTrue("Medication file should be created", medicationFile.exists());

//     List<String> lines = Files.readAllLines(medicationFile.toPath());
//     String expectedData = "Medication1,Dosage1,Frequency1,Route1,Notes1,Duration1";
//     assertEquals("Medication data should match", expectedData, lines.get(lines.size() - 1));

//     // Verify that frame.dispose() is called
//     verify(frame).dispose();

//     // Cleanup
//     medicationFile.delete();
// }

// // Test Case 2: IOException handling
// @Test
// public void testSaveMedicationInfo_IOException() throws Exception {
//     // Arrange
//     String targetUserName = "testUser";
//     JTextField[] textFields = {
//         new JTextField("Medication1")
//     };
//     JButton acceptButton = new JButton();
//     JFrame frame = mock(JFrame.class);

//     // Make the medications directory read-only to simulate IOException
//     File medicationsDir = new File("src/main/resources/medications");
//     medicationsDir.mkdirs();
//     medicationsDir.setWritable(false);

//     // Act
//     MedicationManage.saveMedicationInfo(targetUserName, textFields, acceptButton, frame);

//     // Assert
//     // Verify that frame.dispose() is not called due to error
//     verify(frame, never()).dispose();

//     // Cleanup - reset directory permissions
//     medicationsDir.setWritable(true);
// }