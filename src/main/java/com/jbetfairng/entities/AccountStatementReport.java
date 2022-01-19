package com.jbetfairng.entities;

import java.util.List;

public class AccountStatementReport {
    private List<StatementItem> accountStatement;
    private boolean moreAvailable;

    public List<StatementItem> getAccountStatement() {
        return accountStatement;
    }

    public void setAccountStatement(List<StatementItem> accountStatement) {
        this.accountStatement = accountStatement;
    }

    public boolean getMoreAvailable() {
        return moreAvailable;
    }

    public void setMoreAvailable(boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }
}
