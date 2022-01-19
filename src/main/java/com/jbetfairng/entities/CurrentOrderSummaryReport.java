package com.jbetfairng.entities;

import java.util.List;

public class CurrentOrderSummaryReport {
    private List<CurrentOrderSummary> currentOrders;
    private boolean moreAvailable;

    public List<CurrentOrderSummary> getCurrentOrders() {
        return currentOrders;
    }

    public void setCurrentOrders(List<CurrentOrderSummary> currentOrders) {
        this.currentOrders = currentOrders;
    }

    public boolean isMoreAvailable() {
        return moreAvailable;
    }

    public void setMoreAvailable(boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }
}
