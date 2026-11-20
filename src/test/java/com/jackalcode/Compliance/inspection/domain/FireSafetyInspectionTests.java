package com.jackalcode.Compliance.inspection.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FireSafetyInspectionTests {

    @Test
    @DisplayName("Starting a FireSafetyInspection with a valid homeId should create an inspection with IN_PROGRESS status")
    void start_whenHomeIdProvided_shouldCreateInspectionWithInProgressStatus() {

        UUID homeId = UUID.randomUUID();
        FireSafetyInspection inspection = FireSafetyInspection.start(homeId);
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

        UUID homeId = UUID.randomUUID();
        FireSafetyInspection inspection = FireSafetyInspection.start(homeId);

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
}
