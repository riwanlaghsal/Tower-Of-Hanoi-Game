package models;

import java.util.Stack;

public class GameHistory {
    private Stack<String> moves;
    private Stack<Disk> movedDisks;

    public GameHistory(){
        this.movedDisks = new Stack<>();
        this.moves = new Stack<>();
    }

    public void saveMove(int from, int to, Disk disk) {
        moves.push(from + " -> " + to);
        movedDisks.push(disk);
    }

    public void saveAutoMove(int from, int to) {
        moves.push("[AUTO] : " + from + " -> " + to);
    }

    public String undoLastMove(HanoiGame game) {
        if (moves.isEmpty()) return "Aucun mouvement à annuler";

        String lastMove = moves.pop();
        Disk lastDisk = movedDisks.pop();

        String[] parts = lastMove.split(" -> ");
        int toPole = Integer.parseInt(parts[1]);
        int fromPole = Integer.parseInt(parts[0]);

        game.getPole(toPole).pop();
        game.getPole(fromPole).push(lastDisk);

        return "Annulé : " + lastMove;
    }

    public Stack<String> getMoves(){
        return moves;
    }

    public String getFullHistory() {
        return String.join(", ", moves);
    }

}
