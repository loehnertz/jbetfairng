package com.jbetfairng.entities;

public class CancelInstruction {
    private String betId;
    private double sizeReduction;

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public double getSizeReduction() {
        return sizeReduction;
    }

    public void setSizeReduction(double sizeReduction) {
        this.sizeReduction = sizeReduction;
    }
}
