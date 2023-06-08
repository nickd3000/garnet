package com.physmo.garnet.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sound {

    Map<Integer, AudioFile> audioFileMap = new HashMap<>();
    int audioFileNumberFountain = 1;
    private static final float defaultClipVolume = 1.0f;
    float masterVolume = 1.0f;

    public float getMasterVolume() {
        return masterVolume;
    }

    public void init() {

    }

    public int loadSound(String fileName) {

        try {
            AudioFile audioFile = new AudioFile(fileName);
            audioFileMap.put(audioFileNumberFountain, audioFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        audioFileNumberFountain++;
        return audioFileNumberFountain - 1;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
    }

    public void playSound(int id) {
        new Thread(() -> playSound2(id, defaultClipVolume)).start();
    }

    private void playSound2(int id, float volume) {
        Clip clip;
        AudioFile audioFile = audioFileMap.get(id);
        clip = audioFile.getFreeClip();
        setClipVolume(clip, volume);
        clip.setFramePosition(0);
        clip.start();
    }

    public void setClipVolume(Clip clip, float volume) {

        float v = masterVolume * volume;
        if (v < 0) v = 0;
        if (v > 1) v = 1;

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(v));
    }

    public void playSound(int id, float volume) {
        new Thread(() -> playSound2(id, volume)).start();
    }
}
