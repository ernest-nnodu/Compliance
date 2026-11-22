package com.jackalcode.Compliance.inspection.domain;

import com.jackalcode.Compliance.inspection.exception.IncompleteInspectionException;
import com.jackalcode.Compliance.inspection.exception.InspectionItemNotFoundException;
import com.jackalcode.Compliance.inspection.exception.InvalidInspectionStateException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FireSafetyInspection {

    private UUID id;
    private UUID homeId;
    private InspectionStatus status;
    private Instant startedAt;
    private Instant completedAt;
    private List<InspectionItem> checkItems;

    private FireSafetyInspection(UUID homeId) {
        this.id = UUID.randomUUID();
        this.homeId = Objects.requireNonNull(homeId, "homeId cannot be null");
        this.status = InspectionStatus.IN_PROGRESS;
        this.startedAt = Instant.now();

        //Initialise inspection items
        this.checkItems = Arrays.stream(FireSafetyCheckType.values())
                .map(InspectionItem::of)
                .toList();
    }

    public static FireSafetyInspection start(UUID homeId) {
        return new FireSafetyInspection(homeId);
    }

    public void recordResult(UUID inspectionItemId, CheckResult result, String observation) {

        //Inspection must be in progress to record a result
        ensureInProgress();
        InspectionItem inspectionItemToUpdate = findCheckItem(inspectionItemId);
        inspectionItemToUpdate.recordResult(result, observation);
    }

    public void complete() {

        //Inspection must be in progress to complete it
        ensureInProgress();

        //All inspection items must have a result to complete the inspection
        if (!allCheckItemsHaveResult()) {
            throw new IncompleteInspectionException("Cannot complete inspection: Not all inspection items have a result.");
        }

        this.status = InspectionStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public InspectionStatus getStatus() {
        return status;
    }

    public UUID getHomeId() {
        return homeId;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public List<InspectionItem> getCheckItems() {
        return List.copyOf(checkItems);
    }

    private void ensureInProgress() {
        if (status != InspectionStatus.IN_PROGRESS) {
            throw new InvalidInspectionStateException("Cannot record result: Inspection is not in progress.");
        }
    }

    private InspectionItem findCheckItem(UUID inspectionItemId) {

        return checkItems.stream()
                .filter(item -> item.getId().equals(inspectionItemId))
                .findFirst()
                .orElseThrow(() -> new InspectionItemNotFoundException(inspectionItemId));
    }

    private boolean allCheckItemsHaveResult() {

        return checkItems.stream()
                .allMatch(InspectionItem::hasResult);
    }
}
