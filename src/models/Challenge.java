package models;

public class Challenge {
    private int targetMove;
    private double targetTime;
    private String description;

    public Challenge(int targetMove, String description){
        this.targetMove = targetMove;
        this.description = description;
    }

    public Challenge(double targetTime, String description) {
        this.targetTime = targetTime;
        this.description = description;
    }

    public boolean isCompleted(HanoiGame game){
        return ((game.getMoveCount()<= this.targetMove) || (game.getTime().stop() <= this.targetTime));
    }
    public String getReward(){
        return this.description;
    }

    public int getTargetMove(){
        return this.targetMove;
    }

    public double getTargetTime(){
        return this.targetTime;
    }
}
