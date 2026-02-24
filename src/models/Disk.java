package models;

public class Disk {
    private final int size;

    public Disk(int size) {
        if (size <= 0) throw new IllegalArgumentException("La taille doit être strictement positive");
        this.size = size;
    }
    public int getSize() {
        return size;
    }

}
