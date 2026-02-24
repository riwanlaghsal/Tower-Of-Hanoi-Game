package models;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    private boolean isSoundEnabled;
    private Clip moveSound;
    private Clip winSound;
    private Clip ost;

    public SoundManager() {
        this.isSoundEnabled = true;
        loadSounds();
    }

    private void loadSounds() {
        try {
            moveSound = loadClip("/sounds/move.wav");
            winSound = loadClip("/sounds/win.wav");
            ost = loadClip("/sounds/ost/win");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des sons : " + e.getMessage());
        }
    }

    private Clip loadClip(String soundPath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        URL soundUrl = getClass().getResource(soundPath);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }

    public void playOst(){
        if (isSoundEnabled && ost != null) {
            ost.setFramePosition(0);
            ost.loop(ost.LOOP_CONTINUOUSLY);
        }
    }

    public void playMoveSound() {
        if (isSoundEnabled && moveSound != null) {
            moveSound.setFramePosition(0);
            moveSound.start();
        }
    }

    public void playWinSound() {
        if (isSoundEnabled && winSound != null) {
            winSound.setFramePosition(0);
            winSound.start();
        }
    }

    public void toggleSound() {
        isSoundEnabled = !isSoundEnabled;
    }

    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }
}
