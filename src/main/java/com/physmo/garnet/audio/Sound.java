package com.physmo.garnet.audio;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages loading and playback of audio clips.
 * <p>
 * Sounds are loaded from classpath resources via {@link #loadSound} which returns an integer
 * handle. Use {@link #playSound(int)} or {@link #playSound(int, float, float)} to play a
 * sound on a background thread. Master volume scales all playback.
 */
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

    /**
     * Initialises the audio subsystem. Currently a no-op; reserved for future setup.
     */
    public void init() {

    }

    /**
     * Loads a sound file from the classpath and returns a handle for later playback.
     *
     * @param fileName the classpath-relative path to the audio file (e.g. {@code "sounds/boom.wav"})
     * @return an integer handle to pass to {@link #playSound(int)}
     * @throws RuntimeException if the file cannot be read
     */
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


    /**
     * Plays the sound identified by {@code id} at the default volume and centre pan on a new thread.
     *
     * @param id the handle returned by {@link #loadSound}
     */
    public void playSound(int id) {
        //new Thread(() -> playSound2(id, defaultClipVolume, defaultClipPan)).start();
        new Thread(() -> {
            Clip clip = playSound2(id, defaultClipVolume, defaultClipPan);
//            clip.addLineListener(event -> {
//                if (event.getType()== LineEvent.Type.STOP) {
//                    System.out.println("Stopping thread");
//                    Thread.currentThread().stop();
//                }
//            });
        }
        ).start();
    }

    /**
     * Sets the volume of an already-obtained {@link Clip}, scaled by the master volume.
     *
     * @param clip   the clip to adjust
     * @param volume the desired volume in the range 0.0 (silent) to 1.0 (full)
     */
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

    /**
     * @param id
     * @param volume
     * @param pan    -1.0 to 1.0 To set position between left and right speaker.
     */
    private Clip playSound2(int id, float volume, float pan) {
        Clip clip;
        AudioFile audioFile = audioFileMap.get(id);
        clip = audioFile.getFreeClip();
        setClipVolume(clip, volume);
        setClipPan(clip, pan);
        clip.setFramePosition(0);
        clip.start();
        return clip;
    }

    /**
     * Plays the sound identified by {@code id} with explicit volume and stereo pan on a new thread.
     *
     * @param id     the handle returned by {@link #loadSound}
     * @param volume playback volume in the range 0.0 (silent) to 1.0 (full)
     * @param pan    stereo position: -1.0 = full left, 0.0 = centre, +1.0 = full right
     */
    public void playSound(int id, float volume, float pan) {
        new Thread(() -> {
            Clip clip = playSound2(id, volume, pan);
//            clip.addLineListener(event -> {
//                if (event.getType()== LineEvent.Type.STOP) {
//                    System.out.println("Stopping thread");
//                    Thread.currentThread().interrupt();
//                }
//            });
        }
        ).start();
    }

}
