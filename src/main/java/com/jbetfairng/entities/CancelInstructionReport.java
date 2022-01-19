package com.jbetfairng.entities;

import com.jbetfairng.enums.InstructionReportErrorCode;
import com.jbetfairng.enums.InstructionReportStatus;
import java.util.Date;

public class CancelInstructionReport {
    private InstructionReportStatus status;
    private InstructionReportErrorCode errorCode;
    private CancelInstruction instruction;
    private double sizeCancelled;
    private Date cancelledDate;

    public InstructionReportStatus getStatus() {
        return status;
    }

    public void setStatus(InstructionReportStatus status) {
        this.status = status;
    }

    public InstructionReportErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(InstructionReportErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CancelInstruction getInstruction() {
        return instruction;
    }

    public void setInstruction(CancelInstruction instruction) {
        this.instruction = instruction;
    }

    public double getSizeCancelled() {
        return sizeCancelled;
    }

    public void setSizeCancelled(double sizeCancelled) {
        this.sizeCancelled = sizeCancelled;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }
}
