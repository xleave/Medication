import javax.speech.AudioException;
import javax.speech.EngineStateError;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.util.Locale;

public class TextToSpeech {

    private Synthesizer synthesizer;

    public TextToSpeech() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        try {
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
            synthesizer.resume();
        } catch (EngineException | AudioException | EngineStateError e) {
            e.printStackTrace();
        }
    }

    public void speak(String text) {
        try {
            if (synthesizer != null) {
                synthesizer.speakPlainText(text, null);
                synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            }
        } catch (InterruptedException | EngineStateError e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (synthesizer != null) {
                synthesizer.deallocate();
            }
        } catch (EngineException | EngineStateError e) {
            e.printStackTrace();
        }
    }
}