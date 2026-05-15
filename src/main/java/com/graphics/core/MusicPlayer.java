package com.graphics.core;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

// Sistema de audio simple por estados y acciones.
// Genera tonos en memoria para no depender de archivos externos.
public class MusicPlayer {

    private final AudioFormat format = new AudioFormat(22050.0f, 16, 1, true, false);

    private Clip loopClip;
    private byte[] menuLoopData;
    private byte[] gameLoopData;
    private byte[] gameOverData;
    private byte[] jumpData;
    private byte[] scoreData;
    private byte[] hitData;

    // Genera todos los sonidos del juego en memoria.
    public void init() {
        menuLoopData = buildMelody(
                new float[] {392.00f, 440.00f, 523.25f, 440.00f, 349.23f, 392.00f, 440.00f, 392.00f},
                0.32f,
                7000.0f,
                0.10);
        gameLoopData = buildMelody(
                new float[] {440.00f, 523.25f, 587.33f, 523.25f, 392.00f, 440.00f, 523.25f, 440.00f},
                0.24f,
                6500.0f,
                0.12);
        gameOverData = buildMelody(
                new float[] {523.25f, 493.88f, 440.00f, 392.00f},
                0.18f,
                7000.0f,
                0.08);
        jumpData = buildMelody(new float[] {659.25f, 783.99f}, 0.035f, 6500.0f, 0.08);
        scoreData = buildMelody(new float[] {659.25f, 783.99f, 880.00f}, 0.04f, 7500.0f, 0.10);
        hitData = buildNoiseBurst(0.12f, 7000.0f);
    }

    public void playMenuLoop() {
        startLoop(menuLoopData);
    }

    public void playGameLoop() {
        startLoop(gameLoopData);
    }

    public void playGameOverJingle() {
        stopLoop();
        playEffect(gameOverData);
    }

    public void playJump() {
        playEffect(jumpData);
    }

    public void playScore() {
        playEffect(scoreData);
    }

    public void playHit() {
        playEffect(hitData);
    }

    public void cleanup() {
        stopLoop();
    }

    private void startLoop(byte[] data) {
        if (data == null) {
            return;
        }
        stopLoop();
        try {
            loopClip = AudioSystem.getClip();
            loopClip.open(createStream(data));
            loopClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception exception) {
            loopClip = null;
        }
    }

    private void stopLoop() {
        if (loopClip == null) {
            return;
        }
        loopClip.stop();
        loopClip.close();
        loopClip = null;
    }

    private void playEffect(byte[] data) {
        if (data == null) {
            return;
        }
        try {
            Clip clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.open(createStream(data));
            clip.start();
        } catch (Exception exception) {
            // Si el audio falla, el juego sigue funcionando.
        }
    }

    private AudioInputStream createStream(byte[] data) {
        return new AudioInputStream(
                new ByteArrayInputStream(data),
                format,
                data.length / format.getFrameSize());
    }

    // Construye una melodia simple usando ondas seno.
    private byte[] buildMelody(float[] notes, float beatSeconds, float amplitude, double harmonicFactor) {
        int sampleRate = (int) format.getSampleRate();
        int samplesPerBeat = Math.round(sampleRate * beatSeconds);
        byte[] data = new byte[notes.length * samplesPerBeat * 2];

        int index = 0;
        for (float note : notes) {
            for (int i = 0; i < samplesPerBeat; i++) {
                double time = i / (double) sampleRate;
                double fade = 1.0 - (i / (double) samplesPerBeat) * 0.40;
                double baseWave = Math.sin(2.0 * Math.PI * note * time);
                double harmonic = Math.sin(2.0 * Math.PI * note * 2.0 * time) * harmonicFactor;
                short sample = (short) ((baseWave * 0.65 + harmonic) * fade * amplitude);
                data[index++] = (byte) (sample & 0xFF);
                data[index++] = (byte) ((sample >> 8) & 0xFF);
            }
        }
        return data;
    }

    // Genera un ruido corto que se usa como golpe.
    private byte[] buildNoiseBurst(float seconds, float amplitude) {
        int sampleRate = (int) format.getSampleRate();
        int sampleCount = Math.round(sampleRate * seconds);
        byte[] data = new byte[sampleCount * 2];

        int index = 0;
        for (int i = 0; i < sampleCount; i++) {
            double fade = 1.0 - (i / (double) sampleCount);
            double noise = (Math.random() * 2.0 - 1.0) * fade;
            short sample = (short) (noise * amplitude);
            data[index++] = (byte) (sample & 0xFF);
            data[index++] = (byte) ((sample >> 8) & 0xFF);
        }
        return data;
    }
}
