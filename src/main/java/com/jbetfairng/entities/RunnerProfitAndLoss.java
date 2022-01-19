package com.jbetfairng.entities;

public class RunnerProfitAndLoss {
    private long selectionId;
    private double ifWin;
    private double ifLose;

    public long getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(long selectionId) {
        this.selectionId = selectionId;
    }

    public double getIfWin() {
        return ifWin;
    }

    public void setIfWin(double ifWin) {
        this.ifWin = ifWin;
    }

    public double getIfLose() {
        return ifLose;
    }

    public void setIfLose(double ifLose) {
        this.ifLose = ifLose;
    }
}
