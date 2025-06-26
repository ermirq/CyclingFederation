package com.zerogravitysolutions.core.results;

import com.zerogravitysolutions.core.checkpoints.CheckpointDTO;
import com.zerogravitysolutions.core.commons.BaseDTO;
import com.zerogravitysolutions.core.cyclists.CyclistDTO;
import com.zerogravitysolutions.core.races.RaceDTO;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class ResultDTO extends BaseDTO {

    private CyclistDTO competitor;
    private RaceDTO race;
    private Integer position;
    private LocalTime startTime;
    private LocalTime finishTime;
    private Duration totalTime;
    private Duration timeBehindLeader;
    private List<CheckpointDTO> checkpoints;
    private Duration penalties;
    private Duration bonuses;
    private List<LocalTime> lapTimes;

    public CyclistDTO getCompetitor() {
        return competitor;
    }

    public void setCompetitor(final CyclistDTO competitor) {
        this.competitor = competitor;
    }

    public RaceDTO getRace() {
        return race;
    }

    public void setRace(final RaceDTO race) {
        this.race = race;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(final LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(final LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(final Duration totalTime) {
        this.totalTime = totalTime;
    }

    public Duration getTimeBehindLeader() {
        return timeBehindLeader;
    }

    public void setTimeBehindLeader(final Duration timeBehindLeader) {
        this.timeBehindLeader = timeBehindLeader;
    }

    public List<CheckpointDTO> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(final List<CheckpointDTO> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Duration getPenalties() {
        return penalties;
    }

    public void setPenalties(final Duration penalties) {
        this.penalties = penalties;
    }

    public Duration getBonuses() {
        return bonuses;
    }

    public void setBonuses(final Duration bonuses) {
        this.bonuses = bonuses;
    }

    public List<LocalTime> getLapTimes() {
        return lapTimes;
    }

    public void setLapTimes(final List<LocalTime> lapTimes) {
        this.lapTimes = lapTimes;
    }
}
