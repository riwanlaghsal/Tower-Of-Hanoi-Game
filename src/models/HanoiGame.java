package models;

import exceptions.InvalidMoveException;

import java.io.IOException;
import java.util.Observable;
import vue.HanoiGUI;



public class HanoiGame extends Observable {
    private Pole[] poles;
    private int numberOfDisks;
    private int moveCount;
    private Difficulty difficulty;
    private Player player;
    private GameTimer time;
    private GameHistory history;
    private Challenge currentChallenge;
    private HanoiGUI gui;

    public HanoiGame(){
        this.poles = new Pole[3];
        this.time = new GameTimer(this);
        this.history = new GameHistory();
    }

    public HanoiGame(Player player, Difficulty difficulty) {
        this();
        this.setPlayer(player);
        this.startGame(difficulty, 0);
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public void startGame(Difficulty d, int customDisks){
        this.difficulty = d;
        this.moveCount = 0;
        this.time.start();

        if (difficulty.isFreeMode()) {
            if (customDisks < 3 || customDisks > 10) {
                throw new IllegalArgumentException("Le mode libre nécessite 3 à 10 disques.");
            }
            this.numberOfDisks = customDisks;
        } else {
            this.numberOfDisks = difficulty.getDiskCount();
            if(difficulty.getDiskCount() == Difficulty.EASY.getDiskCount()) {
                currentChallenge = new Challenge(7, "Finir le jeu en 7 coups maximum");
            }
            else if(difficulty.getDiskCount() == Difficulty.MEDIUM.getDiskCount()){
                currentChallenge = new Challenge(31, "Finir le jeu en 31 coups maximum");
            }
            else if(difficulty.getDiskCount() == Difficulty.HARD.getDiskCount()){
                currentChallenge = new Challenge(127, "Finir le jeu en 127 coups maximum");
            } else if (difficulty.name().startsWith("TIMED_")) {
                currentChallenge = new Challenge(difficulty.getTimeMax(), "Finir le jeu avant la fin du chronomètre");
            }
        }

        if (difficulty == Difficulty.TIMED_EASY || difficulty == Difficulty.TIMED_MEDIUM || difficulty == Difficulty.TIMED_HARD) {
            time.setTimedMode(true);
            time.setTimeLimit(difficulty.getTimeMax());

            currentChallenge = new Challenge(difficulty.getTimeMax(), "Finir le jeu avant la fin du chronomètre");
        } else {
            time.setTimedMode(false);
        }

        for (int i = 0; i < 3; i++) {
            poles[i] = new Pole();
        }
        for(int size = this.numberOfDisks; size >= 1; size--){
            poles[0].push(new Disk(size));
        }

    }

    public void moveDisk(int from, int to){
        if (from < 0 || from > 2 || to < 0 || to > 2) {
            throw new InvalidMoveException("Indice de poteau invalide !");
        }

        Pole source = poles[from];
        Pole destination = poles[to];

        if (source.isEmpty()) {
            throw new InvalidMoveException("Le poteau source est vide !");
        }

        Disk sourceDisk = source.getDisks().peek();
        if (!destination.isEmpty() && sourceDisk.getSize() > destination.getDisks().peek().getSize()) {
            throw new InvalidMoveException("Disque trop grand pour être déplacé !");
        }

        Disk movedDisk = source.pop();
        destination.push(movedDisk);
        moveCount++;
        history.saveMove(from, to, movedDisk);
        setChanged();
        notifyObservers();
    }

    public boolean isGameOver(){
        return (poles[2].getDisksCount() == this.numberOfDisks);
    }

    public boolean isOptimal() {
        int optimalMoves = (int) Math.pow(2, numberOfDisks) - 1;
        return moveCount == optimalMoves;
    }

    public void endGame(){
        if (isGameOver() && player != null) {
            double timeTaken = time.stop();
            player.getStats().updateStats(moveCount, timeTaken);
            handleVictory();
            try {
                DataManager.savePlayerData(player);
                System.out.println("✅ Données sauvegardées !");
            } catch (IOException e) {
                System.err.println("❌ Erreur de sauvegarde : " + e.getMessage());
            }
            if (currentChallenge != null && currentChallenge.isCompleted(this)){
                player.getStats().addReward(currentChallenge.getReward());
            }
        }
    }
    public void endGame(boolean isWin) {
        time.stop();

        if (isWin && isGameOver() && player != null) {
            double timeTaken = time.getElapsedTime();
            player.getStats().updateStats(moveCount, timeTaken);
            handleVictory();
            try {
                DataManager.savePlayerData(player);
                System.out.println("✅ Données sauvegardées !");
            } catch (IOException e) {
                System.err.println("❌ Erreur de sauvegarde : " + e.getMessage());
            }
            if (currentChallenge != null && currentChallenge.isCompleted(this)){
                player.getStats().addReward(currentChallenge.getReward());
            }
        }
    }
    public void handleTimeUp(boolean succes) {
        this.gui.showResultTimed(succes);
        endGame(succes);
    }

    public void handleVictory() {
        if (difficulty.isTimed()) {
            gui.showResultTimed(true);
        } else {
            gui.showResultDialog(isOptimal());
        }
    }

    public Difficulty getDifficulty(){
        return this.difficulty;
    }

    public int getMoveCount(){
        return this.moveCount;
    }
    public GameTimer getTime(){
        return this.time;
    }

    public GameHistory getHistory(){
        return this.history;
    }

    public Pole getPole(int i){
        return this.poles[i];
    }

    public void setGui(HanoiGUI gui) {
        this.gui = gui;
    }

    public HanoiGUI getGui() {
        return gui;
    }

    public int getNumberOfDisks(){
        return this.numberOfDisks;
    }

    public void autoMoveDisk(int from, int to) throws InvalidMoveException {
        if (from < 0 || from > 2 || to < 0 || to > 2) {
            throw new InvalidMoveException("Indice de poteau invalide !");
        }

        Pole source = poles[from];
        Pole destination = poles[to];

        if (source.isEmpty()) {
            throw new InvalidMoveException("Le poteau source est vide !");
        }

        Disk sourceDisk = source.getDisks().peek();
        if (!destination.isEmpty() && sourceDisk.getSize() > destination.getDisks().peek().getSize()) {
            throw new InvalidMoveException("Disque trop grand pour être déplacé !");
        }

        Disk movedDisk = source.pop();
        destination.push(movedDisk);
        history.saveMove(from, to, movedDisk);
        setChanged();
        notifyObservers();
    }
}
