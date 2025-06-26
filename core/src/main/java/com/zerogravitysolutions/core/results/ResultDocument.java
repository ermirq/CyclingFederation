package com.zerogravitysolutions.core.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import com.zerogravitysolutions.core.commons.BaseDocument;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import com.zerogravitysolutions.core.races.RaceDocument;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "race_results")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDocument extends BaseDocument {

    private CyclistDocument competitor;
    @DBRef
    private RaceDocument race;
    private Integer position;
    private LocalTime startTime;
    private LocalTime finishTime;
    private Duration totalTime;
    private Duration timeBehindLeader;
    private List<CheckpointDocument> checkpoints;
    private Duration penalties;
    private Duration bonuses;
    private List<LocalTime> lapTimes;

    public CyclistDocument getCompetitor() {
        return competitor;
    }

    public void setCompetitor(final CyclistDocument competitor) {
        this.competitor = competitor;
    }

    public RaceDocument getRace() {
        return race;
    }

    public void setRace(final RaceDocument race) {
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

    public List<CheckpointDocument> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(final List<CheckpointDocument> checkpoints) {
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

    public ResultDocument deepCopy(){
        final ResultDocument copy = new ResultDocument();

        copy.setId(this.getId());
        copy.setCompetitor(this.getCompetitor());
        copy.setRace(this.getRace());

        copy.setPosition(this.getPosition());
        copy.setStartTime(this.getStartTime());
        copy.setFinishTime(this.getFinishTime());
        copy.setTotalTime(this.getTotalTime());
        copy.setTimeBehindLeader(this.getTimeBehindLeader());
        copy.setPenalties(this.getPenalties());
        copy.setBonuses(this.getBonuses());

        if (this.getCheckpoints() != null) {
            copy.setCheckpoints(this.getCheckpoints().stream()
                    .map(CheckpointDocument::new)
                    .collect(Collectors.toList()));
        }

        if (this.getLapTimes() != null) {
            copy.setLapTimes(new ArrayList<>(this.getLapTimes()));
        }

        return copy;
    }
}
