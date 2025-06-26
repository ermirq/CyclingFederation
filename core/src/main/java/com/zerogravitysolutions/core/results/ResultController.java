package com.zerogravitysolutions.core.results;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
public class ResultController {

    private final ResultService resultService;

    public ResultController(final ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<ResultDTO> getResultById(@PathVariable final String id) {
        final ResultDTO result = resultService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/results/races/{raceId}")
    public ResponseEntity<List<ResultDTO>> getResultsByRaceId(@PathVariable final String raceId) {
        final List<ResultDTO> results = resultService.getResultsByRaceId(raceId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/{resultId}/races/{raceId}")
    public ResponseEntity<ResultDTO> getResultByRaceAndResultId(
            @PathVariable final String resultId,
            @PathVariable final String raceId
    ) {
        final ResultDTO result = resultService.getResultByRaceAndResultId(resultId, raceId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/results/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable final String id) {
        resultService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/results/races/{raceId}")
    public ResponseEntity<ResultDTO> saveRaceResult(
            @PathVariable final String raceId,
            @RequestBody final ResultDTO resultDTO
    ) {
        final ResultDTO result = resultService.saveRaceResult(raceId, resultDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/results/{resultId}/races/{raceId}")
    public ResponseEntity<ResultDTO> updateRaceResult(
            @PathVariable final String resultId,
            @PathVariable final String raceId,
            @RequestBody final ResultDTO resultDTO
    ) {

        final ResultDTO updatedResult =
                resultService.updateRaceResult(resultId, raceId, resultDTO);

        return ResponseEntity.ok(updatedResult);
    }

    @PostMapping("/live/start/races/{raceId}")
    public ResponseEntity<String> setRaceStartTime(
            @PathVariable final String raceId
    ){

            resultService.setRaceStartTime(raceId);
            return ResponseEntity.ok("Start time added successfully");
    }

    @PostMapping("/live/results/{resultId}/checkpoints/{checkpointId}")
    public ResponseEntity<String> setCheckpointTime(
            @PathVariable final String resultId,
            @PathVariable final String checkpointId
    ){
            resultService.setCheckpointTime(resultId,checkpointId);
            return ResponseEntity.ok("Checkpoint time set successfully");
    }

    @PostMapping("/live/results/{resultId}/addBonusPoints")
    public ResponseEntity<String> addBonusPoints(
            @PathVariable final String resultId,
            @RequestBody final Duration bonusPoints
    ){

        resultService.addBonusPoint(resultId, bonusPoints);
        return ResponseEntity.ok("Bonus points added succesfully");
    }

    @PostMapping("/live/results/{resultId}/addPenaltyPoints")
    public ResponseEntity<String> addPenaltyPoints(
            @PathVariable final String resultId,
            @RequestBody final Duration penaltyPoints
    ){

        resultService.addPenaltyPoints(resultId, penaltyPoints);
        return ResponseEntity.ok("Penalty points added succesfully");
    }

}
