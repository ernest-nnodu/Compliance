package com.jackalcode.Compliance.inspection.exception;

import java.util.UUID;

public class InspectionItemNotFoundException extends RuntimeException {

    public InspectionItemNotFoundException(UUID inspectionItemId) {
        super("Inspection item with ID " + inspectionItemId + " not found.");
    }
}
