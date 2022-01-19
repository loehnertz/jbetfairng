package com.jbetfairng.entities;

import com.jbetfairng.enums.PersistenceType;

public class UpdateInstruction {
    private String betId;
    private PersistenceType newPersistenceType;

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public PersistenceType getNewPersistenceType() {
        return newPersistenceType;
    }

    public void setNewPersistenceType(PersistenceType newPersistenceType) {
        this.newPersistenceType = newPersistenceType;
    }
}
