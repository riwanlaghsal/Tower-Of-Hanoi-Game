package models;

import exceptions.InvalidMoveException;

import java.util.Stack;



public class Pole {
    private final Stack<Disk> disks;

    public Pole() {
        this.disks = new Stack<>();
    }

    public void push(Disk d) {
        if (!disks.isEmpty() && disks.peek().getSize() < d.getSize()) {
            throw new InvalidMoveException("Impossible d'empiler un disque plus grand sur un plus petit !");
        }
        disks.push(d);
    }

    public Disk pop() {
        if (disks.isEmpty()) {
            throw new InvalidMoveException("Le poteau est vide !");
        }
        return disks.pop();
    }

    public boolean isEmpty(){
        return disks.isEmpty();
    }

    public Stack<Disk> getDisks() {
        return this.disks;
    }

    public int getDisksCount() {
        return disks.size();
    }

    public boolean containsDisk(int diskSize) {
        for (Disk disk : disks) {
            if (disk.getSize() == diskSize) {
                return true;
            }
        }
        return false;
    }


}