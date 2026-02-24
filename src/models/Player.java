package models;

import java.util.Scanner;

public class Player {
    private int id;
    private String name;
    private transient Scanner scanner = new Scanner(System.in);
    private PlayerStats stats;

    public Player(){}
    public Player(int id) {
        this.id = id;
        stats = new PlayerStats();
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public PlayerStats getStats(){
        return this.stats;
    }

    public int getId(){
        return this.id;
    }

    public String askForName() {
        System.out.print("Entrez votre nom: ");
        this.name = scanner.nextLine();
        return this.name;
    }
}