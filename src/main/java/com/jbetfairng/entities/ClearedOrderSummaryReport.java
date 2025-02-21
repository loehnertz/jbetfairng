package com.jbetfairng.entities;

import java.util.List;

public class ClearedOrderSummaryReport {
    private boolean moreAvailable;
    private List<ClearedOrderSummary> clearedOrders;

    public List<ClearedOrderSummary> getClearedOrders() {
        return clearedOrders;
    }

    public void setClearedOrders(List<ClearedOrderSummary> clearedOrders) {
        this.clearedOrders = clearedOrders;
    }

    public boolean isMoreAvailable() {
        return moreAvailable;
    }

    public void setMoreAvailable(boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }
}

