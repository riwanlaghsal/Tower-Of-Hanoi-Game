package models;

import java.time.Duration;
import java.time.Instant;
import javax.swing.Timer;

public class GameTimer {
    private Instant timer;
    private boolean isRunning;
    private int timeLimit = 0;
    private Timer checkLimitTimer;
    private HanoiGame game;
    private boolean isTimedMode = false;

    public GameTimer(){
        this.isRunning = false;
    }

    public GameTimer(HanoiGame game){
        this.isRunning = false;
        this.game = game;
    }

    public void setTimedMode(boolean isTimedMode) {
        this.isTimedMode = isTimedMode;
    }

    public void setTimeLimit(int seconds) {
        this.timeLimit = seconds;

        if (timeLimit > 0 && isRunning) {
            if (checkLimitTimer != null) {
                checkLimitTimer.stop();
            }

            checkLimitTimer = new Timer(1000, e -> {
                if (isRunning && getElapsedTime() >= timeLimit) {
                    stop();
                    game.handleTimeUp(false);
                }
            });
            checkLimitTimer.start();
        }
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public boolean isTimeUp() {
        return timeLimit > 0 && getElapsedTime() >= timeLimit;
    }

    public void start(){
        if (!isRunning) {
            this.timer = Instant.now();
            this.isRunning = true;

            if (isTimedMode && timeLimit > 0) {
                if (checkLimitTimer != null) {
                    checkLimitTimer.stop();
                }

                checkLimitTimer = new Timer(1000, e -> {
                    if (isRunning && getElapsedTime() >= timeLimit) {
                        stop();
                        game.handleTimeUp(false);
                    }
                });
                checkLimitTimer.start();
            }
        }
    }

    public double getElapsedTime(){
        if (timer == null) return 0;
        return Duration.between(timer, Instant.now()).toMillis() / 1000.0;
    }

    public double getDisplayTime() {
        if (isTimedMode && timeLimit > 0) {
            double remaining = timeLimit - getElapsedTime();
            return remaining > 0 ? remaining : 0;
        } else {
            return getElapsedTime();
        }
    }

    public double stop(){
        if (isRunning){
            this.isRunning = false;
            if (checkLimitTimer != null) {
                checkLimitTimer.stop();
                checkLimitTimer = null;
            }
            return getElapsedTime();
        }
        return 0;
    }

    public String getFormattedTime() {
        double timeToDisplay = getDisplayTime();
        int minutes = (int) (timeToDisplay / 60);
        double remainingSeconds = timeToDisplay % 60;
        return String.format("%02d:%05.2f", minutes, remainingSeconds);
    }

    public boolean isRunning() {
        return isRunning;
    }
}