package com.zerogravitysolutions.core.results;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.checkpoints.CheckpointDTO;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import com.zerogravitysolutions.core.checkpoints.commons.CheckpointMapper;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import com.zerogravitysolutions.core.races.RaceDocument;
import com.zerogravitysolutions.core.races.RaceRepository;
import com.zerogravitysolutions.core.races.exceptions.RaceNotFoundException;
import com.zerogravitysolutions.core.results.commons.RaceValidator;
import com.zerogravitysolutions.core.results.commons.ResultMapper;
import com.zerogravitysolutions.core.results.exceptions.ResultNotFoundException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ResultServiceImpl implements ResultService {

    private static final Logger logger = LoggerFactory.getLogger(ResultServiceImpl.class);
    private final ResultRepository resultRepository;
    private final RaceRepository raceRepository;
    private final ResultMapper resultMapper;
    private final RaceValidator validator;
    private final AuditRepository auditRepository;
    private final CheckpointMapper checkpointMapper;

    public ResultServiceImpl(
            final ResultRepository resultRepository,
            final ResultMapper resultMapper,
            final RaceValidator validator,
            final AuditRepository auditRepository,
            final CheckpointMapper checkpointMapper,
            final RaceRepository raceRepository
    ) {
        this.resultRepository = resultRepository;
        this.resultMapper = resultMapper;
        this.validator = validator;
        this.auditRepository = auditRepository;
        this.checkpointMapper = checkpointMapper;
        this.raceRepository = raceRepository;
    }

    @Override
    public ResultDTO findById(final String id) {
        logger.info("Finding result with ID: {}", id);
        final ResultDocument result = resultRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Result with ID: {} not found", id);
                    return new ResultNotFoundException("Result with id " + id + " is not found!");
                }
        );

        logger.info("Result found with ID: {}", result.getId());
        return resultMapper.toResultDTO(result);
    }

    @Override
    public List<ResultDTO> getResultsByRaceId(final String raceId) {
        final ObjectId raceObjectId = new ObjectId(raceId);
        final List<ResultDocument> results = resultRepository.findByRaceReferenceId(raceObjectId);

        return results.stream()
                .map(resultMapper::toResultDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResultDTO getResultByRaceAndResultId(final String resultId, final String raceId) {

        final ResultDocument resultDocument = resultRepository.findById(resultId).orElseThrow(
                () -> new ResultNotFoundException(
                        "Result with this ID " + resultId + " is not found"
                )
        );

        if (!resultDocument.getRace().getId().equals(raceId)) {
            logger.error("Result with ID {} does not belong to race with ID {}", resultId, raceId);
            throw new ResultNotFoundException(
                    "Result with ID " + resultId + " does not belong to race with ID " + raceId
            );
        }

        return resultMapper.toResultDTO(resultDocument);
    }

    @Override
    public void delete(final String id) {
        logger.info("Deleting result with ID: {}", id);
        final ResultDocument resultDocument = resultRepository.findById(id).orElseThrow(
                () -> {
                    logger.error("Result with ID: {} not found for deletion", id);
                    return new ResultNotFoundException("Result with id " + id + " is not found!");
                }
        );

        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(resultDocument);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditDocument.setDeletedAt(LocalDateTime.now());
        auditRepository.save(auditDocument);

        resultRepository.delete(resultDocument);
        logger.info("Result with ID: {} has been permanently deleted", id);
    }

    @Override
    public ResultDTO saveRaceResult(
            final String raceId,
            final ResultDTO resultDTO
    ) {
        logger.info("Trying to save a race result for raceId: {}", raceId);

        final RaceDocument raceDocument = findRaceById(raceId);
        logger.debug("RaceDocument fetched for raceId: {}", raceId);

        validateResult(resultDTO, raceId);
        logger.debug(
                "Validation passed for resultDTO with competitor ID: {}",
                resultDTO.getCompetitor().getId()
        );

        final ResultDocument resultDocument = createResultDocument(resultDTO, raceDocument);
        resultRepository.save(resultDocument);
        logger.info("Result saved for competitor ID: {}", resultDTO.getCompetitor().getId());

        final List<ResultDocument> raceResults =
                resultRepository.findByRaceReferenceId(new ObjectId(raceId));
        logger.debug("Fetched race results for raceId: {}", raceId);

        updateAllCompetitorsTimes(raceResults);
        logger.info("Completed saving race result for raceId: {}", raceId);

        return resultMapper.toResultDTO(resultDocument);
    }

    private ResultDocument createResultDocument(
            final ResultDTO resultDTO,
            final RaceDocument raceDocument
    ) {
        final ResultDocument resultDocument = resultMapper.toResultDocument(resultDTO);
        resultDocument.setRace(raceDocument);

        final List<CheckpointDocument> detailedCheckpoints = mapCheckpoints(
                resultDTO.getCheckpoints(),
                raceDocument
        );
        resultDocument.setCheckpoints(detailedCheckpoints);

        logger.debug("Checkpoints mapped for resultDTO: {}", resultDTO.getCompetitor().getId());
        return resultDocument;
    }

    private void updateAllCompetitorsTimes(
            final List<ResultDocument> raceResults
    ) {
        for (ResultDocument result : raceResults) {
            calculateTotalTime(
                    result,
                    result.getCheckpoints(),
                    raceResults
            );
            logger.debug("Time calculated for competitorId: {}", result.getCompetitor().getId());
        }
        validator.calculateTimeBehindLeader(raceResults);
        raceResults.forEach(resultRepository::save);
    }

    @Override
    public ResultDTO updateRaceResult(
            final String resultId,
            final String raceId,
            final ResultDTO resultDTO
    ) {
        logger.info(
                "Starting to update race result for resultId: {}, raceId: {}",
                resultId, raceId
        );

        final ResultDocument existingResult = getExistingResult(resultId, raceId);
        final AuditDocument auditDocument = auditCurrentResult(resultId, existingResult);

        validateAndUpdateResult(resultDTO, resultId, raceId, existingResult);

        resultRepository.save(existingResult);
        logger.info("Result updated for resultId: {}", resultId);

        auditUpdatedResult(resultId, existingResult, auditDocument);
        logger.info("Updated race result for resultId: {}", resultId);

        return resultMapper.toResultDTO(existingResult);
    }

    private ResultDocument getExistingResult(final String resultId, final String raceId) {
        final ResultDocument existingResult = resultRepository.findById(resultId)
                .orElseThrow(() -> new ResultNotFoundException(
                        "Result with ID :" + resultId + " not found!"
                ));

        if (!existingResult.getRace().getId().equals(raceId)) {
            logger.error("Result with ID {} does not belong to race with ID {}", resultId, raceId);
            throw new ResultNotFoundException(
                    "Result with ID " + resultId + " does not belong to race with ID " + raceId
            );
        }

        return existingResult;
    }

    private void validateAndUpdateResult(
            final ResultDTO resultDTO,
            final String resultId,
            final String raceId,
            final ResultDocument existingResult
    ) {
        final ObjectId raceObjectId = new ObjectId(raceId);
        final List<ResultDocument> existingResultForRace =
                resultRepository.findByRaceReferenceId(raceObjectId);

        validator.validateUniquePosition(resultDTO.getPosition(), existingResultForRace, resultId);
        logger.debug("Unique position validated for resultId: {}", resultId);

        validator.validateCheckpointTimes(resultDTO);
        logger.debug("Checkpoint times validated for resultId: {}", resultId);

        existingResult.setPosition(resultDTO.getPosition());
        existingResult.setPenalties(resultDTO.getPenalties());
        existingResult.setBonuses(resultDTO.getBonuses());

        final RaceDocument raceDocument = findRaceById(raceId);
        logger.debug("RaceDocument fetched for raceId: {}", raceId);

        final List<CheckpointDocument> updatedCheckpoints =
                mapCheckpoints(resultDTO.getCheckpoints(), raceDocument);
        existingResult.setCheckpoints(updatedCheckpoints);
        logger.debug("Checkpoints mapped for resultId: {}", resultId);

        calculateTotalTime(
                existingResult,
                updatedCheckpoints,
                existingResultForRace
        );
    }

    @Override
    @Transactional
    public void setRaceStartTime(final String raceId) {
        logger.info("Setting start time for race ID {}: {}", raceId);

        final RaceDocument race = raceRepository.findById(raceId)
                .orElseThrow(
                        () -> new RaceNotFoundException("Race with ID " + raceId + " not found")
                );
        logger.debug("Race found: {}", race);

        for(RaceDocument.Participants participant : race.getParticipants()){
            final CyclistDocument cyclist = participant.getCyclistId();
            logger.info(
                    "Creating result document for participant: {} {}",
                    cyclist.getFirstName(),
                    cyclist.getLastName()
            );

            final ResultDocument resultDocument = new ResultDocument();
            resultDocument.setRace(race);
            resultDocument.setStartTime(LocalTime.now());
            resultDocument.setCompetitor(cyclist);

            final List<CheckpointDocument> checkpoints = new ArrayList<>(race.getCheckpoints());
            resultDocument.setCheckpoints(checkpoints);

            resultDocument.setPosition(null);
            resultDocument.setFinishTime(null);
            resultDocument.setTimeBehindLeader(null);
            resultDocument.setPenalties(Duration.ZERO);
            resultDocument.setBonuses(Duration.ZERO);

            resultRepository.save(resultDocument);
            logger.info("Result document created for participant {} with ID {}",
                    cyclist.getFirstName() + " " + cyclist.getLastName(), resultDocument.getId());
        }

        logger.info("All result documents created for race ID {}", raceId);
    }

    @Override
    @Transactional
    public void setCheckpointTime(
            final String resultId,
            final String checkpointId
    ) {
        logger.info(
                "Setting checkpoint time for race ID {}, result ID {}, checkpoint ID {}: {}",
                resultId, checkpointId
        );

        final ResultDocument resultDocument = resultRepository.findById(resultId)
                .orElseThrow(
                        ()-> new ResultNotFoundException("Result with ID "+resultId+" is not found")
                );

        final CheckpointDocument checkpoint  = resultDocument.getCheckpoints().stream()
                .filter(cp -> cp.getId().equals(checkpointId))
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Checkpoint with this id is not found"
                        )
                );

        checkpoint.setCheckpointTime(LocalTime.now());
        logger.info("Checkpoint time is set to: {}", checkpoint.getCheckpointTime());

        final List<ResultDocument> raceResults = resultRepository.findByRaceReferenceId(
                new ObjectId(resultDocument.getRace().getId())
        );
        calculateTotalTime(resultDocument, resultDocument.getCheckpoints(), raceResults);

        resultRepository.save(resultDocument);
        logger.info("Checkpoint time is set for result ID {}: checkpoint ID {}",
                resultId, checkpoint
        );

        validator.calculateLiveTimeBehindLeader(resultDocument.getRace().getId());
    }

    @Override
    public void addBonusPoint(
            final String resultId,
            final Duration bonusPoints
    ) {
        logger.info("Adding bonus points for result ID {}: {}", resultId, bonusPoints);

        final ResultDocument resultDocument = resultRepository.findById(resultId)
                .orElseThrow(
                        ()-> new ResultNotFoundException("Result with ID "+resultId+" is not found")
                );

        resultDocument.setBonuses(resultDocument.getBonuses().plus(bonusPoints));

        final List<ResultDocument> raceResults = resultRepository.findByRaceReferenceId(
                new ObjectId(resultDocument.getRace().getId())
        );
        calculateTotalTime(resultDocument, resultDocument.getCheckpoints(), raceResults);

        resultRepository.save(resultDocument);
        logger.info("Added bonus points for result ID {}:", resultId);

        validator.calculateLiveTimeBehindLeader(resultDocument.getRace().getId());
    }

    @Override
    public void addPenaltyPoints(
            final String resultId,
            final Duration penaltyPoints
    ) {
        logger.info("Adding penalty points for result ID {}: {}", resultId, penaltyPoints);

        final ResultDocument resultDocument = resultRepository.findById(resultId)
                .orElseThrow(
                        ()-> new ResultNotFoundException("Result with ID "+resultId+" is not found")
                );

        resultDocument.setPenalties(resultDocument.getPenalties().plus(penaltyPoints));

        final List<ResultDocument> raceResults = resultRepository.findByRaceReferenceId(
                new ObjectId(resultDocument.getRace().getId())
        );
        calculateTotalTime(resultDocument, resultDocument.getCheckpoints(), raceResults);

        resultRepository.save(resultDocument);
        logger.info("Added penalty points for result ID {}:", resultId);

        validator.calculateLiveTimeBehindLeader(resultDocument.getRace().getId());
    }

    private RaceDocument findRaceById(final String raceId) {
        return raceRepository.findById(raceId).orElseThrow(
                () -> new RaceNotFoundException("Race with ID " + raceId + " not found")
        );
    }

    private void validateResult(final ResultDTO resultDTO, final String raceId) {
        final List<ResultDocument> existingResults =
                resultRepository.findByRaceReferenceId(new ObjectId(raceId));
        validator.validateUniquePosition(
                resultDTO.getPosition(),
                existingResults,
                resultDTO.getId()
        );
        validator.validateCheckpointTimes(resultDTO);
    }

    private List<CheckpointDocument> mapCheckpoints(
            final List<CheckpointDTO> checkpointDTOs,
            final RaceDocument raceDocument
    ) {
        return checkpointDTOs.stream()
                .map(checkpointDTO -> {
                    final CheckpointDocument checkpointDocument =
                            checkpointMapper.toCheckpointDocument(checkpointDTO);

                    final CheckpointDocument raceCheckpoint = raceDocument.getCheckpoints().stream()
                            .filter(cp -> cp.getId().equals(checkpointDTO.getId()))
                            .findFirst()
                            .orElseThrow(
                                    () -> new IllegalStateException(
                                            "Checkpoint ID "
                                            + checkpointDTO.getId() +
                                            " not found in race document"
                                    )
                            );

                    checkpointDocument.setDistanceMark(raceCheckpoint.getDistanceMark());
                    checkpointDocument.setTitle(raceCheckpoint.getTitle());

                    return checkpointDocument;
                }).collect(Collectors.toList());
    }

    private void calculateTotalTime(
            final ResultDocument resultDocument,
            final List<CheckpointDocument> checkpoints,
            final List<ResultDocument> raceResults
    ) {

        validateInput(resultDocument, checkpoints, raceResults);

        Duration totalTime = Duration.ZERO;
        LocalTime previousCheckpointTime = resultDocument.getStartTime();

        for (CheckpointDocument checkpoint : checkpoints) {
            if (checkpoint.getCheckpointTime() == null) {
                break;
            }

            final Duration timeFromStart = Duration.between(
                    resultDocument.getStartTime(),
                    checkpoint.getCheckpointTime()
            );
            checkpoint.setTimeFromStart(timeFromStart);

            final Duration leaderTimeFromStart = raceResults.stream()
                    .map(r -> r.getCheckpoints().stream()
                            .filter(c -> c.getId().equals(checkpoint.getId()))
                            .findFirst()
                            .filter(c -> r.getStartTime() != null && c.getCheckpointTime()!=null)
                            .map(c -> Duration.between(r.getStartTime(), c.getCheckpointTime()))
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .min(Comparator.naturalOrder())
                    .orElse(Duration.ZERO);

            Duration timeBehindLeader;
            if(leaderTimeFromStart.isZero() || leaderTimeFromStart.equals(timeFromStart)){
                timeBehindLeader = Duration.ZERO;
            }else {
                timeBehindLeader = timeFromStart.minus(leaderTimeFromStart);
                timeBehindLeader = timeBehindLeader.isNegative() ? Duration.ZERO : timeBehindLeader;
            }

            checkpoint.setTimeBehindLeader(timeBehindLeader);

            final Duration checkpointDuration =
                    Duration.between(
                            previousCheckpointTime,
                            checkpoint.getCheckpointTime()
                    );
            totalTime = totalTime.plus(checkpointDuration);
            previousCheckpointTime = checkpoint.getCheckpointTime();
        }

        totalTime = totalTime
                .plus(resultDocument.getPenalties() != null
                        ? resultDocument.getPenalties()
                        : Duration.ZERO
                )
                .minus(resultDocument.getBonuses() != null
                        ? resultDocument.getBonuses()
                        : Duration.ZERO
                );

        resultDocument.setTotalTime(totalTime);
        resultDocument.setFinishTime(resultDocument.getStartTime().plus(totalTime));
    }

    private void validateInput(
            final ResultDocument resultDocument,
            final List<CheckpointDocument> checkpoints,
            final List<ResultDocument> raceResults
    ) {
        if (resultDocument == null || checkpoints == null || raceResults == null) {
            throw new IllegalArgumentException(
                    "Result document, checkpoints, or race results are null"
            );
        }

        if (resultDocument.getStartTime() == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
    }

    private AuditDocument auditCurrentResult(
            final String resultId,
            final ResultDocument existingResult
    ) {
        final ResultDocument oldResult = existingResult.deepCopy();
        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(oldResult);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditRepository.save(auditDocument);
        logger.debug("Audit record created for old result of resultId: {}", resultId);
        return auditDocument;
    }
    private void auditUpdatedResult(
            final String resultId,
            final ResultDocument existingResult,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(existingResult);
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for resultId: {}", resultId);
    }
}
