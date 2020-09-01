package com.karaoke.main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements AutoCloseable {
    private boolean released;
    private AudioInputStream stream = null;
    private Clip clip = null;
    private boolean playing = false;

    Sound(String name) {
        try {
            File f = new File("./songs/" + name + ".wav");
            stream = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.addLineListener(new Listener());
            released = true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
            released = false;
            close();
        }
    }

    private boolean isPlaying() {
        return playing;
    }

    private void play(boolean breakOld) {
        if (released) {
            if (breakOld) {
                clip.stop();
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            } else if (!isPlaying()) {
                clip.setFramePosition(0);
                clip.start();
                playing = true;
            }
        }
    }

    void play() {
        play(true);
    }

    void stop() {
        if (playing) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null)
            clip.close();

        if (stream != null)
            try {
                stream.close();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
    }

    private class Listener implements LineListener {
        public void update(LineEvent ev) {
            if (ev.getType() == LineEvent.Type.STOP) {
                playing = false;
                synchronized(clip) {
                    clip.notify();
                }
            }
        }
    }
}