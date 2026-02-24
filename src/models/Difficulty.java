package models;

import java.util.Arrays;

public enum Difficulty {
    EASY("Facile",3, false),
    MEDIUM("moyen",5, false),
    HARD("Difficile",7, false),
    TIMED_EASY("Facile chronométré",3, true, 90),
    TIMED_MEDIUM("Moyen chronométré",5, true, 240),
    TIMED_HARD("Difficile chronométré",7, true, 480),
    FREE("Libre",0, false);

    private final String displayName;
    private final int totalDisks;
    private final boolean timed;
    private int time;

    Difficulty(String displayName, int totalDisks, boolean timed, int time){
        this.displayName = displayName;
        this.totalDisks = totalDisks;
        this.timed = timed;
        this.time = time;
    }
    Difficulty(String displayName, int totalDisks, boolean timed){

        this.displayName = displayName;
        this.totalDisks = totalDisks;
        this.timed = timed;
    }

    public String getDisplayName(){
        return displayName;
    }

    public int getDiskCount() {
        return totalDisks;
    }

    public boolean isTimed(){
        return timed;
    }

    public int getTimeMax(){
        return this.time;
    }


    public boolean isFreeMode(){
        return this == FREE;
    }

    public static String[] getDisplayOptions(){
        return Arrays.stream(values())
                .map(Difficulty::getDisplayName)
                .toArray(String[]::new);
    }
}
