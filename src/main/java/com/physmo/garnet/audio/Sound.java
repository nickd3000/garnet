package com.physmo.garnet.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sound {

    private static final float defaultClipVolume = 1.0f;
    private static final float defaultClipPan = 0.0f;
    Map<Integer, AudioFile> audioFileMap = new HashMap<>();
    int audioFileNumberFountain = 1;
    float masterVolume = 1.0f;

    public float getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
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

    public void playSound(int id) {
        new Thread(() -> playSound2(id, defaultClipVolume, defaultClipPan)).start();
    }

    /**
     * @param id
     * @param volume
     * @param pan    -1.0 to 1.0 To set position between left and right speaker.
     */
    private void playSound2(int id, float volume, float pan) {
        Clip clip;
        AudioFile audioFile = audioFileMap.get(id);
        clip = audioFile.getFreeClip();
        setClipVolume(clip, volume);
        setClipPan(clip, pan);
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

    /**
     * -1.0 left - +1.0 Right
     * 0 = Center
     */
    public void setClipPan(Clip clip, float pan) {
        if (!clip.isControlSupported(FloatControl.Type.BALANCE)) {
            //System.out.println("pan not supported");
            return;
        }

        FloatControl panControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
        panControl.setValue(pan);
    }

    public void playSound(int id, float volume, float pan) {
        new Thread(() -> playSound2(id, volume, pan)).start();
    }

}
