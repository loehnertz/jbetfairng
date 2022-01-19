package com.jbetfairng.entities;

import com.jbetfairng.enums.InstructionReportErrorCode;
import com.jbetfairng.enums.InstructionReportStatus;

public class UpdateInstructionReport {
    private InstructionReportStatus status;
    private InstructionReportErrorCode errorCode;
    private UpdateInstruction instruction;

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

    public UpdateInstruction getInstruction() {
        return instruction;
    }

    public void setInstruction(UpdateInstruction instruction) {
        this.instruction = instruction;
    }
}
