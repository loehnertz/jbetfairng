package com.jbetfairng.entities;

import com.jbetfairng.enums.InstructionReportErrorCode;
import com.jbetfairng.enums.InstructionReportStatus;
import java.util.List;

public class UpdateExecutionReport {
    private String customerRef;
    private InstructionReportStatus status;
    private InstructionReportErrorCode errorCode;
    private List<UpdateInstructionReport> instructionReports;

    public String getCustomerRef() {
        return customerRef;
    }

    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }

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

    public List<UpdateInstructionReport> getInstructionReports() {
        return instructionReports;
    }

    public void setInstructionReports(List<UpdateInstructionReport> instructionReports) {
        this.instructionReports = instructionReports;
    }
}
