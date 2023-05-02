package com.physmo.garnet.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioFile {
    byte[] bytes;
    AudioInputStream audioInputStream;
    int numClips = 5;
    List<Clip> clips = new ArrayList<>();

    public AudioFile(String path) throws IOException {
        bytes = readFileBytes(path);

        for (int i = 0; i < numClips; i++) {
            clips.add(createClip());
        }
    }

    public Clip createClip() {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            audioInputStream = AudioSystem.getAudioInputStream(bis);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] readFileBytes(String path) throws IOException {
        File file = new File(path);
        FileInputStream fl = new FileInputStream(file);

        byte[] arr = new byte[(int) file.length()];

        fl.read(arr);

        fl.close();

        return arr;
    }

    public Clip getFreeClip() {
        for (Clip clip : clips) {
            if (!clip.isActive()) return clip;
        }
        return null;
    }
}
