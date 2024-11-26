package me.Votifier.server.model;

public class Bin {
    private int binNumber;
    private int[] range;
    private String color;

    public Bin(int binNumber, int[] range, String color){
        this.binNumber = binNumber;
        this.range = range;
        this.color = color;
    }

    public int getBinNumber() { 
        return binNumber; 
    }

    public void setBinNumber(int binNumber) { 
        this.binNumber = binNumber; 
    }

    public int[] getRange() { 
        return range; 
    }

    public void setRange(int[] range) { 
        this.range = range; 
    }

    public String getColor() { 
        return color; 
    }

    public void setColor(String color) { 
        this.color = color; 
    }

    public boolean isInRange(int value) {
        return value >= range[0] && value <= range[1];
    }
}