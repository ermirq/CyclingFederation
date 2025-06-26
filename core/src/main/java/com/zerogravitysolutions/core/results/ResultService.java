package com.zerogravitysolutions.core.results;

import java.time.Duration;
import java.util.List;

public interface ResultService {
    ResultDTO findById(String id);

    List<ResultDTO> getResultsByRaceId(String raceId);

    ResultDTO getResultByRaceAndResultId(String resultId, String raceId);

    void delete(String id);

    ResultDTO saveRaceResult(String raceId, ResultDTO resultDTO);

    ResultDTO updateRaceResult(
            String resultId,
            String raceId,
            ResultDTO resultDTO
    );

    void setRaceStartTime(String raceId);

    void setCheckpointTime(
            String resultId,
            String checkpointId
    );

    void addBonusPoint(String resultId, Duration bonusPoints);

    void addPenaltyPoints(String resultId, Duration penaltyPoints);
}
