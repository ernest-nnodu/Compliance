package com.jackalcode.Compliance.inspection.domain;

import com.jackalcode.Compliance.inspection.exception.InspectionItemNotFoundException;
import com.jackalcode.Compliance.inspection.exception.InvalidInspectionStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FireSafetyInspectionTests {

    private FireSafetyInspection inspection;
    private UUID homeId;

    @BeforeEach
    void setUp() {
        homeId = UUID.randomUUID();
        inspection = FireSafetyInspection.start(homeId);
    }

    @Test
    @DisplayName("Starting a FireSafetyInspection with a valid homeId should create an inspection with IN_PROGRESS status")
    void start_whenHomeIdProvided_shouldCreateInspectionWithInProgressStatus() {

        assertAll(
                () -> assertNotNull(inspection.getId(), "Inspection ID should not be null"),
                () -> assertEquals(homeId, inspection.getHomeId(), "Home ID should match the provided value"),
                () -> assertEquals(InspectionStatus.IN_PROGRESS, inspection.getStatus(), "Inspection status should be IN_PROGRESS"),
                () -> assertNotNull(inspection.getStartedAt(), "StartedAt timestamp should not be null"),
                () -> assertNull(inspection.getCompletedAt(), "CompletedAt timestamp should be null for in-progress inspections")
        );
    }

    @Test
    @DisplayName("Starting a FireSafetyInspection with a valid homeId should initialise inspection items")
    void start_whenHomeIdProvided_shouldInitialiseInspectionItems() {

        assertAll(
                () -> assertNotNull(inspection.getInspectionItems(), "Inspection items should not be null"),
                () -> assertEquals(FireSafetyCheckType.values().length, inspection.getInspectionItems().size(), "Number of inspection items should match the number of FireSafetyCheckType values"),
                () -> assertTrue(inspection.getInspectionItems().stream().allMatch(item -> item.getCheckType() != null), "All inspection items should have a non-null checkType"),
                () -> assertTrue(inspection.getInspectionItems().stream().allMatch(item -> item.getResult() == null), "All inspection items should have a null result")
        );
    }

    @Test
    @DisplayName("Starting a FireSafetyInspection with a null homeId should throw a NullPointerException")
    void start_whenHomeIdIsNull_shouldThrowNullPointerException() {

        assertThrows(NullPointerException.class, () -> FireSafetyInspection.start(null));
    }

    @Test
    @DisplayName("Recording a result for an inspection item should update the inspection item")
    void recordResult_whenResultIsValid_shouldUpdateInspectionItem() {

        UUID inspectionItemId = inspection.getInspectionItems().getFirst().getId();

        inspection.recordResult(inspectionItemId, CheckResult.PASS, null);

        InspectionItem inspectionItem = inspection.getInspectionItems().stream()
                .filter(item -> item.getId().equals(inspectionItemId)).findFirst()
                .orElseThrow();
        assertEquals(CheckResult.PASS, inspectionItem.getResult(),
                "Inspection item result should be updated to PASS");
    }

    @Test
    @DisplayName("Recording a result for an inspection item that already has a result should replace the existing result")
    void recordResult_whenInspectionItemHasResult_shouldReplaceExistingResult() {

        UUID inspectionItemId = inspection.getInspectionItems().getFirst().getId();

        inspection.recordResult(inspectionItemId, CheckResult.FAIL, "Existing observation");
        inspection.recordResult(inspectionItemId, CheckResult.PASS, "New observation");

        InspectionItem inspectionItem = inspection.getInspectionItems().stream()
                .filter(item -> item.getId().equals(inspectionItemId)).findFirst()
                .orElseThrow();
        assertEquals(CheckResult.PASS, inspectionItem.getResult(),
                "Inspection item result should be updated to PASS");
        assertEquals("New observation", inspectionItem.getObservation(),
                "Inspection item observation should be updated to New observation");
    }

    @Test
    @DisplayName("Recording a result for an inspection item that does not exist should throw an Exception")
    void recordResult_whenInspectionItemNotFound_shouldThrowException() {

        UUID invalidInspectionItemId = UUID.randomUUID();

        assertThrows(InspectionItemNotFoundException.class,
                () -> inspection.recordResult(invalidInspectionItemId, CheckResult.PASS, "Observation"));
    }

    @Test
    @DisplayName("Recording a result for an inspection that is already completed should throw an Exception")
    void recordResult_whenInspectionIsCompleted_shouldThrowException() {

        inspection.getInspectionItems()
                .forEach(item -> inspection.recordResult(item.getId(), CheckResult.PASS, "Observation"));
        inspection.completeInspection();
        UUID inspectionItemId = inspection.getInspectionItems().getFirst().getId();

        assertThrows(InvalidInspectionStateException.class,
                () -> inspection.recordResult(inspectionItemId, CheckResult.PASS, "Observation"));
    }
}
