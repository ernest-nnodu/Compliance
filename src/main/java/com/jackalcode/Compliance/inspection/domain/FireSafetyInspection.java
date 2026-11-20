package com.jackalcode.Compliance.inspection.domain;

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
    private List<InspectionItem> inspectionItems;

    private FireSafetyInspection(UUID homeId) {
        this.id = UUID.randomUUID();
        this.homeId = Objects.requireNonNull(homeId, "homeId cannot be null");
        this.status = InspectionStatus.IN_PROGRESS;
        this.startedAt = Instant.now();

        //Initialise inspection items
        this.inspectionItems = Arrays.stream(FireSafetyCheckType.values())
                .map(InspectionItem::of)
                .toList();
    }
    public static FireSafetyInspection start(UUID homeId) {
        return new FireSafetyInspection(homeId);
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

    public List<InspectionItem> getInspectionItems() {
        return List.copyOf(inspectionItems);
    }
}
