package com.zerogravitysolutions.core.results.commons;

import com.zerogravitysolutions.core.checkpoints.CheckpointDTO;
import com.zerogravitysolutions.core.results.ResultDTO;
import com.zerogravitysolutions.core.results.ResultDocument;
import com.zerogravitysolutions.core.results.ResultRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Component
public class RaceValidator {

    private final ResultRepository resultRepository;

    public RaceValidator(final ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void validateUniquePosition(
            final int position,
            final List<ResultDocument> existingResults,
            final String currentId
    ) {
        for (ResultDocument result : existingResults) {
            if (result.getPosition() == position
                    && (currentId == null || !result.getId().equals(currentId))
            ) {
                throw new IllegalArgumentException(
                        "Position must be unique within each race category!"
                );
            }
        }
    }

    public void validateCheckpointTimes(final ResultDTO resultDTO) {
        final List<CheckpointDTO> checkpoints = resultDTO.getCheckpoints();

        if (checkpoints == null) {
            throw new IllegalArgumentException("Checkpoints list cannot be null.");
        }

        for (CheckpointDTO checkpoint : checkpoints) {
            if (checkpoint.getCheckpointTime() == null) {
                throw new IllegalArgumentException(
                        "Checkpoint times cannot be null for checkpoint: " + checkpoint.getTitle()
                );
            }
        }
    }

    public void calculateTimeBehindLeader(final List<ResultDocument> raceResults) {
        final ResultDocument leader = raceResults.stream()
                .min(Comparator.comparing(ResultDocument::getTotalTime))
                .orElseThrow(()-> new IllegalStateException("No results found!"));

        for (ResultDocument result : raceResults) {
            if (!result.equals(leader)) {
                final Duration timeBehindLeader =
                        result.getTotalTime().minus(leader.getTotalTime());
                result.setTimeBehindLeader(
                        timeBehindLeader.isNegative() ? Duration.ZERO : timeBehindLeader
                );
            } else {
                result.setTimeBehindLeader(Duration.ZERO);
            }
        }
    }

    public void calculateLiveTimeBehindLeader(final String raceId) {

        final List<ResultDocument> raceResults =
                resultRepository.findByRaceReferenceId(new ObjectId(raceId));

        final ResultDocument leader = raceResults.stream()
                .filter(result -> result.getTotalTime() != null)
                .min(Comparator.comparing(ResultDocument::getTotalTime))
                .orElseThrow(() -> new IllegalStateException("No results found!"));

        for (ResultDocument result : raceResults) {

            if (result.getTotalTime() != null) {

                final Duration timeBehindLeader =
                        result.getTotalTime().minus(leader.getTotalTime());
                result.setTimeBehindLeader(
                        timeBehindLeader.isNegative() ? Duration.ZERO : timeBehindLeader
                );

            } else {
                result.setTimeBehindLeader(Duration.ZERO);
            }
        }

        resultRepository.saveAll(raceResults);
    }
}
