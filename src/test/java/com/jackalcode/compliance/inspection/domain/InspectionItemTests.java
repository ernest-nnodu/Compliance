package com.jackalcode.compliance.inspection.domain;

import com.jackalcode.compliance.inspection.exception.InvalidInspectionStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InspectionItemTests {

    @Test
    @DisplayName("Recording a result for an inspection item should update the inspection item")
    void recordResult_whenResultIsValid_shouldUpdateInspectionItem() {

        InspectionItem inspectionItem = InspectionItem.of(FireSafetyCheckType.FIRE_DOORS);
        inspectionItem.recordResult(CheckResult.PASS, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(CheckResult.PASS, inspectionItem.getResult(), "Inspection item result should be updated to PASS"),
                () -> Assertions.assertNull(inspectionItem.getObservation(), "Observation should be null")
        );
    }

    @Test
    @DisplayName("Recording a result for an inspection item with an invalid result should throw an exception")
    void recordResult_whenResultIsInvalid_shouldThrowException() {
        InspectionItem inspectionItem = InspectionItem.of(FireSafetyCheckType.FIRE_DOOR_GUARDS);
        assertThrows(NullPointerException.class,
                () -> inspectionItem.recordResult(null, "Invalid observation"),
                "Recording an invalid result should throw an exception");
    }

    @Test
    @DisplayName("Recording a result for an inspection item with FAIL result should require an observation")
    void recordResult_WhenResultIsFail_ObservationIsRequired() {

        InspectionItem inspectionItem = InspectionItem.of(FireSafetyCheckType.FIRE_EXITS);
        assertThrows(InvalidInspectionStateException.class,
                () -> inspectionItem.recordResult(CheckResult.FAIL, null),
                "Recording a FAIL result without an observation should throw an exception");
    }
}
