import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.junit.Test;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.Central;
import javax.speech.EngineException;
import java.util.Locale;

import services.TextToSpeech;

import static org.mockito.Mockito.*;

public class TextToSpeechTest {

    private TextToSpeech tts;
    private Synthesizer mockSynthesizer;

    @BeforeEach
    void setUp() throws EngineException {
        mockSynthesizer = mock(Synthesizer.class);
        Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

        // Mock the Synthesizer initialization
        try {
            when(Central.createSynthesizer(new SynthesizerModeDesc(Locale.US))).thenReturn(mockSynthesizer);

            // Mock void methods using doNothing
            doNothing().when(mockSynthesizer).allocate();
            doNothing().when(mockSynthesizer).resume();
            doNothing().when(mockSynthesizer).deallocate();
        } catch (Exception e) {
            Assertions.fail("Failed to mock Synthesizer initialization: " + e.getMessage());
        }
    }


    @Test
    public void testInitialization() {
        Assertions.assertDoesNotThrow(() -> new TextToSpeech(), "TextToSpeech initialization failed.");
    }

    @Test
    public void testSpeak() {
        try {
            // Create a test instance
            tts = new TextToSpeech();
            tts.speak("Hello, this is a test.");

            // Verify the behavior of the mock synthesizer
            verify(mockSynthesizer, times(1)).speakPlainText("Hello, this is a test.", null);
            verify(mockSynthesizer, times(1)).waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (Exception e) {
            Assertions.fail("Failed to execute speak method: " + e.getMessage());
        }
    }
}