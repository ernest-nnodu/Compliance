package com.jackalcode.Compliance.inspection.domain;

import com.jackalcode.Compliance.inspection.exception.InvalidInspectionStateException;

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

    void recordResult(CheckResult result, String observation) {
        CheckResult resultToRecord = Objects.requireNonNull(result, "result cannot be null");

        //if the result is FAIL, observation is required
        if (resultToRecord == CheckResult.FAIL && (observation == null || observation.isBlank())) {
            throw new InvalidInspectionStateException("Observation is required when check result is FAIL");
        }

        this.result = resultToRecord;
        this.observation = observation;
    }

    public boolean hasResult() {
        return result != null;
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
