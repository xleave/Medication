// Java
import static org.junit.Assert.*;

import gui.MedicationHistoryGUI;
import org.junit.Before;
import org.junit.Test;
import services.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GraphTest{
    private User testuser;
    private HistoryTracker testHistoryTracker;
    private MedicationHistoryGUI medicationHistoryGUI;
    private JPanel drawGraph;

    @Before
    public void setUp(){
        testuser = mock(User.class);
        testHistoryTracker = mock(HistoryTracker.class);

        Map<String, Integer> medicationLimits = new HashMap<>();
        medicationLimits.put("MedA", 3);
        medicationLimits.put("MedB", 2);

        Map<String, Integer> medicationTakenCounts = new HashMap<>();
        medicationTakenCounts.put("MedA", 2);
        medicationTakenCounts.put("MedB", 1);

        when(testHistoryTracker.getMedicationDailyLimits()).thenReturn(medicationLimits);
        when(testHistoryTracker.getTakenCount("MedA")).thenReturn(2);
        when(testHistoryTracker.getTakenCount("MedB")).thenReturn(1);

        medicationHistoryGUI = new MedicationHistoryGUI(testuser);

        drawGraph = medicationHistoryGUI.new drawGraph();
    }
    @Test
    public void testDataInitialization(){
        MedicationHistoryGUI.drawGraph graph = (MedicationHistoryGUI.drawGraph) drawGraph;
        // Test the initial data is loaded correctly
        assertEquals(3, (int) graph.medicationLimits.get("MedA"));
        assertEquals(2, (int) graph.medicationLimits.get("MedB"));
        assertEquals(2, (int) graph.medicationTakenCounts.get("MedA"));
        assertEquals(1, (int) graph.medicationTakenCounts.get("MedB"));
    }
    @Test
    public void testMouseHoverUpdatesMedication() {
        MedicationHistoryGUI.drawGraph graph = (MedicationHistoryGUI.drawGraph) drawGraph;

        // Simulate a mouse move event
        int mockX = 60;
        int mockY = 230;
        graph.mouseMoved(new MouseEvent(graph, 0, 0, 0, mockX, mockY, 0, false));
        assertEquals("MedA", graph.hoveredMedication);
    }
    @Test
    public void testMouseExitClearsHover() {
        MedicationHistoryGUI.drawGraph graph = (MedicationHistoryGUI.drawGraph) drawGraph;

        graph.hoveredMedication = "MedA";
        graph.mouseExited(new MouseEvent(graph, 0, 0, 0, 0, 0, 0, false));
        assertNull(graph.hoveredMedication);
    }
    @Test
    public void testBarRendering() {
        MedicationHistoryGUI.drawGraph graph = (MedicationHistoryGUI.drawGraph) drawGraph;

        Graphics2D mockGraphics = mock(Graphics2D.class);
        graph.paintComponent(mockGraphics);

        verify(mockGraphics, atLeastOnce()).setColor(any(Color.class));
        verify(mockGraphics, atLeastOnce()).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(mockGraphics, atLeastOnce()).drawString(anyString(), anyInt(), anyInt());
    }

}