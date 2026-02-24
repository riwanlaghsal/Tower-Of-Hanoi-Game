package models;

import java.util.Vector;

public class PlayerStats extends Player {
    private int totalGames;
    private int lastScore;
    private int bestScore;
    private double averageScore;
    private double lastTime;
    private double bestTime;
    private Vector<String> unlockedRewards;
    private int totalChallengesCompleted;

    public PlayerStats(){
        this.totalGames = 0;
        this.lastScore = 0;
        this.bestScore = 0;
        this.averageScore = 0.0;
        this.bestTime = 0.0;
        this.lastTime = 0.0;
        this.unlockedRewards = new Vector<>();
        this.totalChallengesCompleted = 0;
    }

    public void updateStats(int moveCount, double timeTaken) {
        ++totalGames;
        this.lastScore = moveCount;
        if (bestScore == 0 || moveCount < bestScore) bestScore = moveCount;
        averageScore = (averageScore * (totalGames - 1) + moveCount) / totalGames;

        this.lastTime = timeTaken;
        if (bestTime == 0.0 || timeTaken < bestTime) {
            this.bestTime = timeTaken;
        }

    }

    public void addReward(String reward) {
        unlockedRewards.add(reward);
        incrementChallengesCompleted();
    }

    public Vector<String> getUnlockedRewards(){
        return unlockedRewards;
    }

    public void incrementChallengesCompleted() {
        ++totalChallengesCompleted;
    }

    public int getTotalChallengesCompleted(){
        return totalChallengesCompleted;
    }
    public int getBestScore(){
        return this.bestScore;
    }
    public int getLastScore(){
        return this.lastScore;
    }
    public int getTotalGames() {
        return totalGames;
    }
    public double getAverageScore() {
        return averageScore;
    }
    public double getBestTime() {
        return bestTime;
    }
    public double getLastTime() {
        return lastTime;
    }
    public boolean hasReward(String reward){
        return (unlockedRewards.contains(reward));
    }

    public void setTotalChallengesCompleted(int newTotalChallengesCompleted) {
        this.totalChallengesCompleted = newTotalChallengesCompleted;
    }
    public void setBestScore(int newBestScore){
        this.bestScore = newBestScore;
    }
    public void setLastScore(int newLastScore){
        this.lastScore = newLastScore;
    }
    public void setTotalGames(int newTotalGames){
        this.totalGames = newTotalGames;
    }
    public void setAverageScore(double newAverageScore){
        this.averageScore = newAverageScore;
    }
    public void setBestTime(double newBestTime){
        this.bestTime = newBestTime;
    }
    public void setLastTime(double newLastTime){
        this.lastTime = newLastTime;
    }
}
