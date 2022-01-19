package com.jbetfairng.entities;

import java.util.List;

public class MarketProfitAndLoss {
    private String marketId;
    private double commissionApplied;
    private List<RunnerProfitAndLoss> profitAndLosses;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public double getCommissionApplied() {
        return commissionApplied;
    }

    public void setCommissionApplied(double commissionApplied) {
        this.commissionApplied = commissionApplied;
    }

    public List<RunnerProfitAndLoss> getProfitAndLosses() {
        return profitAndLosses;
    }

    public void setProfitAndLosses(List<RunnerProfitAndLoss> profitAndLosses) {
        this.profitAndLosses = profitAndLosses;
    }

}
