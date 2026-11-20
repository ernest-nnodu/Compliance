package com.jackalcode.Compliance.inspection.domain;

import java.util.Objects;
import java.util.UUID;

public class InspectionItem {

    private UUID id;
    private FireSafetyCheckType checkType;
    private CheckResult result;
    private String observation;

    private InspectionItem(FireSafetyCheckType checkType) {
        this.id = UUID.randomUUID();
        this.checkType = Objects.requireNonNull(checkType, "checkType cannot be null");
    }

    public static InspectionItem of(FireSafetyCheckType checkType) {
        return new InspectionItem(checkType);
    }

    public String getObservation() {
        return observation;
    }

    public CheckResult getResult() {
        return result;
    }

    public FireSafetyCheckType getCheckType() {
        return checkType;
    }

    public UUID getId() {
        return id;
    }
}
