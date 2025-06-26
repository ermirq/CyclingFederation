package com.zerogravitysolutions.core.checkpoints;

import com.zerogravitysolutions.core.commons.BaseDocument;

import java.time.Duration;
import java.time.LocalTime;

public class CheckpointDocument extends BaseDocument {
    private String title;
    private int distanceMark;
    private LocalTime checkpointTime;
    private Duration timeFromStart;
    private Duration timeBehindLeader;

    public CheckpointDocument(final CheckpointDocument original) {
        this.title = original.title;
        this.distanceMark = original.distanceMark;
        this.checkpointTime = original.checkpointTime;
        this.timeFromStart = original.timeFromStart;
        this.timeBehindLeader = original.timeBehindLeader;
    }

    public CheckpointDocument() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public int getDistanceMark() {
        return distanceMark;
    }

    public void setDistanceMark(final int distanceMark) {
        this.distanceMark = distanceMark;
    }

    public LocalTime getCheckpointTime() {
        return checkpointTime;
    }

    public void setCheckpointTime(final LocalTime checkpointTime) {
        this.checkpointTime = checkpointTime;
    }

    public Duration getTimeFromStart() {
        return timeFromStart;
    }

    public void setTimeFromStart(final Duration timeFromStart) {
        this.timeFromStart = timeFromStart;
    }

    public Duration getTimeBehindLeader() {
        return timeBehindLeader;
    }

    public void setTimeBehindLeader(final Duration timeBehindLeader) {
        this.timeBehindLeader = timeBehindLeader;
    }
}
