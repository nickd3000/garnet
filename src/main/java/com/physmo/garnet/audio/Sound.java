package com.physmo.garnet.audio;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Sound {

    Map<Integer, AudioFile> audioFileMap = new HashMap<>();
    int audioFileNumberFountain = 1;

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

        new Thread(() -> playSound2(id)).start();


    }

    public void playSound2(int id) {
        Clip clip;
        AudioFile audioFile = audioFileMap.get(id);
        clip = audioFile.getFreeClip();
        clip.setFramePosition(0);
        clip.start();
    }
}
