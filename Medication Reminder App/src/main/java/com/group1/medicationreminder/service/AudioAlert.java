package com.group1.medicationreminder.service;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class AudioAlert {

    private static final String VOICE_NAME = "kevin16";
    private Voice voice;

    public AudioAlert() {
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICE_NAME);
        if (voice != null) {
            voice.allocate();
        } else {
            System.err.println("Voice not found: " + VOICE_NAME);
        }
    }

    public void playAlert(String message) {
        if (voice != null) {
            voice.speak(message);
        }
    }

    public void shutdown() {
        if (voice != null) {
            voice.deallocate();
        }
    }
}