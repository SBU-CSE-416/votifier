package me.Votifier.server.model.configurations.bins;

public class Bin {
    private String binLabel;
    private String binDataLabel;
    private int binNumber;
    private int[] binRange;
    private String[] binColors; 

    public Bin(
        String binLabel, 
        String binDataLabel,
        int binNumber, 
        int[] binRange, 
        String[] binColors
        ) {
        this.binLabel = binLabel;
        this.binDataLabel = binDataLabel;
        this.binNumber = binNumber;
        this.binRange = binRange;
        this.binColors = binColors;
    }

    public String getBinLabel() {
        return binLabel;
    }

    public void setBinLabel(String binLabel) {
        this.binLabel = binLabel;
    }

    public String getBinDataLabel() {
        return binDataLabel;
    }

    public void setBinDataLabel(String binDataLabel) {
        this.binDataLabel = binDataLabel;
    }

    public int getBinNumber() { 
        return binNumber; 
    }

    public void setBinNumber(int binNumber) { 
        this.binNumber = binNumber; 
    }

    public int[] getBinRange() { 
        return binRange; 
    }

    public void setBinRange(int[] binRange) { 
        this.binRange = binRange; 
    }

    public String[] getBinColors() { 
        return binColors; 
    }

    public void setBinColors(String[] binColors) { 
        this.binColors = binColors; 
    }

    public boolean isInBinRange(int value) {
        return value >= binRange[0] && value <= binRange[1];
    }
}