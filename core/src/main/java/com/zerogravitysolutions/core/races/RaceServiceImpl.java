package com.zerogravitysolutions.core.races;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.categories.CategoryDocument;
import com.zerogravitysolutions.core.categories.CategoryRepository;
import com.zerogravitysolutions.core.categories.exceptions.CategoryAgeException;
import com.zerogravitysolutions.core.categories.exceptions.CategoryDuplicateException;
import com.zerogravitysolutions.core.categories.exceptions.CategoryNotFoundException;
import com.zerogravitysolutions.core.checkpoints.CheckpointDTO;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import com.zerogravitysolutions.core.checkpoints.commons.CheckpointMapper;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import com.zerogravitysolutions.core.cyclists.CyclistRepository;
import com.zerogravitysolutions.core.cyclists.exceptions.CyclistDuplicateException;
import com.zerogravitysolutions.core.cyclists.exceptions.CyclistNotFoundException;
import com.zerogravitysolutions.core.disciplines.DisciplineDocument;
import com.zerogravitysolutions.core.disciplines.DisciplineRepository;
import com.zerogravitysolutions.core.disciplines.exceptions.DisciplineDuplicateException;
import com.zerogravitysolutions.core.disciplines.exceptions.DisciplineNotFoundException;
import com.zerogravitysolutions.core.races.commons.RaceMapper;
import com.zerogravitysolutions.core.races.exceptions.RaceNotFoundException;
import com.zerogravitysolutions.core.results.exceptions.ResultNotFoundException;
import com.zerogravitysolutions.core.races.exceptions.*;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import com.zerogravitysolutions.core.volunteers.VolunteersDTO;
import com.zerogravitysolutions.core.volunteers.VolunteersDocument;
import com.zerogravitysolutions.core.volunteers.VolunteersRepository;
import com.zerogravitysolutions.core.volunteers.commons.VolunteersMapper;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersDuplicateException;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersNotFoundException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RaceServiceImpl implements RaceService {

    private static final Logger logger = LoggerFactory.getLogger(RaceServiceImpl.class);
    private final RaceRepository raceRepository;
    private final CategoryRepository categoryRepository;
    private final DisciplineRepository disciplineRepository;
    private final CyclistRepository cyclistRepository;
    private final VolunteersRepository volunteersRepository;
    private final RaceMapper raceMapper;
    private final VolunteersMapper volunteersMapper;
    private final CheckpointMapper checkpointMapper;
    private final AuditRepository auditRepository;

    public RaceServiceImpl(
            final RaceRepository raceRepository,
            final CategoryRepository categoryRepository,
            final DisciplineRepository disciplineRepository,
            final RaceMapper raceMapper,
            final CyclistRepository cyclistRepository,
            final VolunteersRepository volunteersRepository,
            final VolunteersMapper volunteersMapper,
            final CheckpointMapper checkpointMapper,
            final AuditRepository auditRepository
    ) {
        this.raceRepository = raceRepository;
        this.categoryRepository = categoryRepository;
        this.disciplineRepository = disciplineRepository;
        this.raceMapper = raceMapper;
        this.cyclistRepository = cyclistRepository;
        this.volunteersRepository = volunteersRepository;
        this.volunteersMapper = volunteersMapper;
        this.checkpointMapper = checkpointMapper;
        this.auditRepository = auditRepository;

    }

    @Override
    public RaceDTO save(final RaceDTO raceDTO) {
        logger.info("Attempting to save race");
        RaceDocument raceDocument = new RaceDocument();

        raceMapper.mapDtoToDocument(raceDTO, raceDocument);
        raceDocument.setCreatedBy(UserContextHolder.getContext().getUserId());

        raceDocument = raceRepository.save(raceDocument);

        logger.info("Race saved successfully with ID: {}", raceDocument.getId());
        return raceMapper.mapDocumentToDto(raceDocument);
    }

    @Override
    public RaceDTO getById(final String id) {
        logger.info("Fetching race by ID: {}", id);
        final RaceDocument raceDocument = findRaceById(id);

        logger.info("Race found with ID: {}", id);
        return raceMapper.mapDocumentToDto(raceDocument);
    }

    @Override
    public Page<RaceDTO> getAllRaces(final Pageable page) {
        logger.info("Fetching all races with pagination: {}", page);
        final Page<RaceDocument> documents = raceRepository.findAll(page);
        logger.info("Fetched {} races", documents.getTotalElements());
        return documents.map(raceMapper::mapDocumentToDto);
    }

    @Override
    public RaceDTO patch(final String id, final RaceDTO raceDTO) {
        logger.info("Updating race with ID: {}", id);
        final RaceDocument raceDocument = findRaceById(id);

        raceDocument.setUpdatedBy(UserContextHolder.getContext().getUserId());

        raceMapper.mapDtoToDocument(raceDTO, raceDocument);
        final RaceDocument updated = raceRepository.save(raceDocument);
        logger.info("Race with ID {} has been updated", id);
        return raceMapper.mapDocumentToDto(updated);
    }

    @Override
    public void softDelete(final String id) {
        logger.info("Soft deleting race with ID: {}", id);

        final RaceDocument race = findRaceById(id);

        final AuditDocument auditDocument = auditCurrentRace(id, race);
        race.setDeletedBy(UserContextHolder.getContext().getUserId());
        race.setDeletedAt(LocalDateTime.now());

        raceRepository.save(race);

        auditUpdatedRace(id, race, auditDocument);

        logger.info("Race with ID {} has been soft deleted", id);
    }

    @Override
    public RaceDTO updated(final String id, final RaceDTO updatedRace) {
        logger.info("Updating race with ID: {}", id);
        final RaceDocument existingRace = findRaceById(id);

        final AuditDocument auditDocument = auditCurrentRace(id, existingRace);

        existingRace.setRaceName(updatedRace.getRaceName());
        existingRace.setRaceDate(updatedRace.getRaceDate());
        existingRace.setStartTime(updatedRace.getStartTime());
        existingRace.setEstimatedDuration(updatedRace.getEstimatedDuration());
        existingRace.setElevationGain(updatedRace.getElevationGain());
        existingRace.setDistance(updatedRace.getDistance());
        existingRace.setCheckpoints(updatedRace.getCheckpoints());
        existingRace.setLocation(updatedRace.getLocation());
        existingRace.setMapLink(updatedRace.getMapLink());
        existingRace.setLogistics(raceMapper.mapLogisticsDtoToDocument(updatedRace.getLogistics()));
        existingRace.setSponsors(raceMapper.mapSponsorsDtoToDocument(updatedRace.getSponsors()));

        existingRace.setUpdatedBy(UserContextHolder.getContext().getUserId());
        final RaceDocument merge = raceMapper.mapDtoToDocument(updatedRace);
        final RaceDocument updated = raceRepository.save(merge);
        logger.info("Updated race with ID: {}", id);

        auditUpdatedRace(id, existingRace, auditDocument);

        return raceMapper.mapDocumentToDto(updated);
    }

    @Override
    public void addDisciplineToRace(final String raceId, final String disciplineId){
        logger.info("Attempting to add disciplines to race with id {}", raceId);
        final RaceDocument race = findRaceById(raceId);

        final DisciplineDocument disciplineDocument =
                disciplineRepository.findByIdAndDeletedAtIsNull(disciplineId).orElseThrow(() -> {
                    logger.error("Discipline with id {} not found", disciplineId);
                    return new DisciplineNotFoundException("Discipline was not found");
                });
        if (race.getDiscipline() == null) {
            race.setDiscipline(disciplineDocument);
            raceRepository.save(race);
            logger.info("Successfully added discipline to race with id {}", raceId);
        } else {
            logger.error("This race already has a discipline");
            throw new DisciplineDuplicateException("This race already has a discipline");
        }
    }

    @Override
    public void deleteDisciplineFromRace(final String raceId, final String disciplineId) {
        logger.info("Deleting discipline with ID: {} from race with ID: {}", disciplineId, raceId);
        final RaceDocument raceDocument = findRaceById(raceId);

        final DisciplineDocument discipline = raceDocument.getDiscipline();
        if (discipline != null && discipline.getId().equals(disciplineId)) {
            raceDocument.setDiscipline(null);
        } else {
            throw new DisciplineNotFoundException(
                    "Discipline with ID " + disciplineId + " not found in race with ID " + raceId
            );
        }

        raceRepository.save(raceDocument);

        logger.info("Deleted discipline with ID: {} from race with ID: {}", disciplineId, raceId);
    }

    @Override
    public void addCategoriesToRace(final String raceId, final String categoryId) {
        logger.info("Attempting to add categories to race with id {}", raceId);

        final RaceDocument race = findRaceById(raceId);

        final boolean categoryExists = race.getCategories().stream()
                .anyMatch(category -> category.getCategoryId().getId().equals(categoryId));

        if (categoryExists) {
            logger.error(
                    "Category with ID {} is already associated with race ID {}",
                    categoryId,
                    raceId
            );
            throw new CategoryDuplicateException(
                    "Category with ID " + categoryId
                            + " is already associated with race ID " + raceId
            );
        }

        final CategoryDocument categoryDocument =
                categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category with ID " + categoryId + " not found")
                );

        final RaceDocument.CategoriesDocument newCategory = new RaceDocument.CategoriesDocument();
        newCategory.setCategoryId(categoryDocument);

        race.getCategories().add(newCategory);

        raceRepository.save(race);
        logger.info("Successfully added categories to race with id {}", raceId);
    }

    @Override
    public void deleteCategoryFromRace(final String raceId, final String categoryId) {
        logger.info("Deleting category with ID: {} from race with ID: {}", categoryId, raceId);
        final RaceDocument raceDocument = findRaceById(raceId);

        final List<RaceDocument.CategoriesDocument> categories = raceDocument.getCategories();

        if (categories != null && !categories.isEmpty()) {
            final boolean removed = categories
                    .removeIf(cat -> cat.getCategoryId().getId().equals(categoryId));

            if (!removed) {
                throw new CategoryNotFoundException(
                        "Category with ID " + categoryId + " not found in race with ID " + raceId
                );
            }
        } else {
            throw new CategoryNotFoundException(
                    "Category with ID " + categoryId + " not found in race with ID " + raceId
            );
        }

        raceRepository.save(raceDocument);
        logger.info("Deleted category with ID: {} from race with ID: {}", categoryId, raceId);
    }


    @Override
    public void addParticipantsToRaces(
            final String raceId,
            final String cyclistId,
            final String categoryId) {
        logger.info("Attempting to add participant to race with id {}", raceId);

        final RaceDocument race = findRaceById(raceId);
        final CyclistDocument participant = findCyclistById(cyclistId);

        checkIfAlreadyRegistered(race, cyclistId);

        final boolean isAllowed =
                validateCyclistForCategory(participant.getAge(), race, categoryId);

        if (!isAllowed) {
            throw new CategoryAgeException("Cyclist's age " + participant.getAge() +
                    " is not within the allowed range.");
        }
        final RaceDocument.CategoriesDocument categories = race.getCategories().stream()
                .filter(
                        sn -> sn.getCategoryId().getId() != null &&
                                sn.getCategoryId().getId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Chosen category not found in the race's starting numbers"));

        final int minNumber = categories.getMinNumber();
        final int maxNumber = categories.getMaxNumber();

        final int uniqueRaceNumber =
                generateUniqueRaceNumber(minNumber, maxNumber, race.getParticipants(), categoryId);

        addParticipantToRace(race, participant, categoryId, uniqueRaceNumber);

        logger.info("Successfully added participant to race with id {}", raceId);
    }

    private CyclistDocument findCyclistById(final String cyclistId) {
        return cyclistRepository.findByIdAndDeletedAtIsNull(cyclistId)
                .orElseThrow(() -> new CyclistNotFoundException("Participant not found"));
    }

    private void checkIfAlreadyRegistered(final RaceDocument race, final String cyclistId) {
        final boolean alreadyRegistered = race.getParticipants().stream()
                .anyMatch(p -> p.getCyclistId().getId().equals(cyclistId));
        if (alreadyRegistered) {
            throw new CyclistDuplicateException("Participant with id " + cyclistId +
                    " is already registered for this race.");
        }
    }

    private boolean validateCyclistForCategory(
            final int participantAge,
            final RaceDocument race,
            final String categoryId
    ) {
        for (RaceDocument.CategoriesDocument raceCategory : race.getCategories()) {
            final int categoryMinAge = raceCategory.getCategoryId().getMinAge();
            final int categoryMaxAge = raceCategory.getCategoryId().getMaxAge();

            if (categoryId.equals(raceCategory.getCategoryId().getId())) {
                if (participantAge >= categoryMinAge && participantAge <= categoryMaxAge) {
                    return true;
                } else if (participantAge < categoryMinAge) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addParticipantToRace(
            final RaceDocument race,
            final CyclistDocument participant,
            final String categoryId,
            final int uniqueRaceNumber
    ) {
        final List<RaceDocument.Participants> categoryParticipants = race.getParticipants().stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .toList();

        final int startingPosition = categoryParticipants.size() + 1;

        final RaceDocument.Participants raceParticipant = new RaceDocument.Participants();
        raceParticipant.setCyclistId(participant);
        raceParticipant.setRaceNumber(uniqueRaceNumber);
        raceParticipant.setCategoryId(categoryId);
        raceParticipant.setStartingPosition(startingPosition);
        race.getParticipants().add(raceParticipant);
        raceRepository.save(race);
    }


    private int generateUniqueRaceNumber(
            final int min,
            final int max,
            final List<RaceDocument.Participants> participants,
            final String categoryId
    ) {
        final BitSet existingNumbers = new BitSet(max + 1);

        final List<RaceDocument.Participants> categoryParticipants = participants.stream()
                .filter(p -> p.getCategoryId().equals(categoryId)).toList();

        categoryParticipants.forEach(
                competitor -> existingNumbers.set(competitor.getRaceNumber()));

        final int nextFreeNumber = existingNumbers.nextClearBit(min);

        if (nextFreeNumber > max) {
            throw new IllegalStateException("No available race numbers in the given range");
        }

        return nextFreeNumber;
    }

    @Override
    public void deleteParticipantsFromRace(final String raceId, final String cyclistId) {
        logger.info("Deleting participant with ID: {} from race with ID: {}", cyclistId, raceId);
        final RaceDocument raceDocument = findRaceById(raceId);

        final List<RaceDocument.Participants> participantsList = raceDocument.getParticipants();

        if (participantsList != null && !participantsList.isEmpty()) {
            final boolean removed =
                    participantsList.removeIf(participant -> participant.getCyclistId() != null &&
                    participant.getCyclistId().getId().equals(cyclistId));

            if (removed) {
                raceRepository.save(raceDocument);
                logger.info(
                        "Deleted participant with ID: {} from race with ID: {}",
                        cyclistId,
                        raceId
                );
            } else {
                throw new CyclistNotFoundException(
                        "Participant with ID " + cyclistId +
                                " not found in race with ID " + raceId);
            }
        } else {
            throw new CyclistNotFoundException(
                    "No participants found in the race with ID " + raceId);
        }
    }

    @Override
    public void addVolunteersToRaces(final String raceId, final String volunteerId) {
        logger.info("Attempting to add volunteers to race with id {}", raceId);
        final RaceDocument race = findRaceById(raceId);

        final VolunteersDocument volunteers =
            volunteersRepository.findByIdAndDeletedAtIsNull(volunteerId).orElseThrow(() -> {
            logger.error("Volunteer with id {} not found", volunteerId);
            return new VolunteersNotFoundException("Volunteer not found");
        });

        final boolean alreadyAdded = race.getVolunteers().stream()
                .anyMatch(v -> v.getId().equals(volunteerId));

        if (alreadyAdded) {
            logger.error(
                    "Volunteer with id {} is already registered for the race with id {}",
                    volunteerId,
                    raceId
            );
            throw new VolunteersDuplicateException(
                    "Volunteer with id " + volunteerId + " is already registered for this race."
            );
        }

        race.getVolunteers().add(volunteers);

        raceRepository.save(race);
        logger.info("Successfully added volunteers to race with id {}", raceId);
    }

    @Override
    public void deleteVolunteersFromRace(final String raceId, final String volunteerId) {
        logger.info("Deleting volunteer with ID: {} from race with ID: {}", volunteerId, raceId);
        final RaceDocument raceDocument = findRaceById(raceId);

        final List<VolunteersDocument> volunteers = raceDocument.getVolunteers();
        if (volunteers != null) {
            volunteers.removeIf(volunteer -> volunteer.getId().equals(volunteerId));
        } else {
            throw new VolunteersNotFoundException(
                "Volunteer with ID " + volunteerId + " is not found in the race with Id " + raceId
            );
        }

        raceRepository.save(raceDocument);
        logger.info("Deleted volunteer with ID: {} from race with ID: {}", volunteerId, raceId);
    }

    @Override
    public RaceDTO addCheckPointToRace(final String raceId, final CheckpointDTO checkpointDTO) {
        final RaceDocument race = raceRepository.findById(raceId).orElseThrow(
                () -> new RaceNotFoundException("Race with ID " + raceId + " not found"));

        final CheckpointDocument checkpoint = checkpointMapper.toCheckpointDocument(checkpointDTO);
        if(checkpoint.getId() == null){
            checkpoint.setId(new ObjectId().toString());
        }
        race.getCheckpoints().add(checkpoint);
        raceRepository.save(race);

        return raceMapper.mapDocumentToDto(race);
    }

    @Override
    public RaceDTO updateCheckpointInRace(
            final String raceId,
            final String checkpointId,
            final CheckpointDocument updatedCheckpoint
    ) {
        final RaceDocument race = raceRepository.findById(raceId).orElseThrow(
                () -> new RaceNotFoundException("Race with ID " + raceId + " not found"));

        final CheckpointDocument existingCheckpoint = race.getCheckpoints().stream()
                .filter(c -> c.getId().equals(checkpointId))
                .findFirst()
                .orElseThrow(() -> new ResultNotFoundException("Checkpoint not found"));

        existingCheckpoint.setTitle(updatedCheckpoint.getTitle());
        existingCheckpoint.setDistanceMark(updatedCheckpoint.getDistanceMark());
        existingCheckpoint.setCheckpointTime(updatedCheckpoint.getCheckpointTime());
        existingCheckpoint.setTimeFromStart(updatedCheckpoint.getTimeFromStart());
        existingCheckpoint.setTimeBehindLeader(updatedCheckpoint.getTimeBehindLeader());

        raceRepository.save(race);
        return raceMapper.mapDocumentToDto(race);
    }

    @Override
    public void deleteCheckpointFromRace(final String raceId, final String checkpointId) {
        final RaceDocument race = raceRepository.findById(raceId).orElseThrow(
                () -> new RaceNotFoundException("Race with ID " + raceId + " not found"));

        race.getCheckpoints().removeIf(c -> c.getId().equals(checkpointId));
        raceRepository.save(race);
    }

    public void addRaceNumbers(
            final String raceId,
            final String categoryId,
            final int minNumber,
            final int maxNumber
    ) {
        final RaceDocument race = findRaceById(raceId);

        final CategoryDocument category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category with id " + categoryId + " not found")
                );

        boolean categoryFound = false;

        for (RaceDocument.CategoriesDocument categories : race.getCategories()) {
            if (categories.getCategoryId() != null
                    && categories.getCategoryId().getId().equals(categoryId)) {
                categories.setMinNumber(minNumber);
                categories.setMaxNumber(maxNumber);
                categoryFound = true;
                break;
            }
        }

        if (!categoryFound) {
            final RaceDocument.CategoriesDocument categories =
                    new RaceDocument.CategoriesDocument();
            categories.setCategoryId(category);
            categories.setMinNumber(minNumber);
            categories.setMaxNumber(maxNumber);
            race.getCategories().add(categories);
        }

        raceRepository.save(race);
        raceMapper.mapDocumentToDto(race);
    }

    @Override
    public void reorderParticipantPositionWithShift(
            final String raceId,
            final String cyclistId,
            final String categoryId,
            final int newPosition
    ) {
        logger.info(
                "Reordering participant position for raceId: {}," +
                        " cyclistId: {}, categoryId: {}, newPosition: {}",
                raceId, cyclistId, categoryId, newPosition);

        final RaceDocument race = findRaceById(raceId);
        final List<RaceDocument.Participants> participants = race.getParticipants();

        final Map<Integer, RaceDocument.Participants> positionMap =
                createPositionMap(participants, categoryId);

        final RaceDocument.Participants movingParticipant =
                findParticipantByCyclistId(participants, cyclistId);

        final List<RaceDocument.Participants> updatedParticipants = updateParticipantPositions(
                positionMap, movingParticipant, newPosition);

        updateParticipantsInRace(participants, updatedParticipants);

        raceRepository.save(race);
        logger.info("Successfully saved updated race document with raceId: {}", raceId);
    }

    private Map<Integer, RaceDocument.Participants> createPositionMap(
            final List<RaceDocument.Participants> participants,
            final String categoryId
    ) {
        logger.debug("Creating position map for categoryId: {}", categoryId);
        final Map<Integer, RaceDocument.Participants> positionMap = new HashMap<>();
        for (RaceDocument.Participants p : participants) {
            if (p.getCategoryId().equals(categoryId)) {
                positionMap.put(p.getStartingPosition(), p);
            }
        }
        return positionMap;
    }

    private RaceDocument.Participants findParticipantByCyclistId(
            final List<RaceDocument.Participants> participants,
            final String cyclistId
    ) {
        logger.debug("Looking for moving participant with cyclistId: {}", cyclistId);
        return participants.stream()
                .filter(p -> p.getCyclistId() != null && p.getCyclistId().getId().equals(cyclistId))
                .findFirst()
                .orElseThrow(() -> new CyclistNotFoundException("Cyclist not found"));
    }

    private List<RaceDocument.Participants> updateParticipantPositions(
            final Map<Integer, RaceDocument.Participants> positionMap,
            final RaceDocument.Participants movingParticipant,
            final int newPosition
    ) {
        final List<RaceDocument.Participants> updatedParticipants = new ArrayList<>();

        if (!positionMap.containsKey(newPosition)) {
            logger.info("New position {} is available. Moving participant.", newPosition);
            movingParticipant.setStartingPosition(newPosition);
            updatedParticipants.add(movingParticipant);
        } else {
            logger.info("Shifting participants to accommodate new position {}", newPosition);
            shiftParticipantsPositions(
                    positionMap,
                    updatedParticipants,
                    movingParticipant,
                    newPosition
            );
        }
        return updatedParticipants;
    }

    private void shiftParticipantsPositions(
            final Map<Integer, RaceDocument.Participants> positionMap,
            final List<RaceDocument.Participants> updatedParticipants,
            final RaceDocument.Participants movingParticipant,
            final int newPosition
    ) {
        int currentPosition = newPosition;
        while (positionMap.containsKey(currentPosition)) {
            final RaceDocument.Participants occupant = positionMap.get(currentPosition);
            occupant.setStartingPosition(currentPosition + 1);
            updatedParticipants.add(occupant);
            currentPosition++;
        }
        movingParticipant.setStartingPosition(newPosition);
        updatedParticipants.add(movingParticipant);
    }

    private void updateParticipantsInRace(
            final List<RaceDocument.Participants> participants,
            final List<RaceDocument.Participants> updatedParticipants
    ) {
        for (RaceDocument.Participants updated : updatedParticipants) {
            participants.stream()
                    .filter(p -> p.getCyclistId().equals(updated.getCyclistId()))
                    .findFirst()
                    .ifPresent(p -> p.setStartingPosition(updated.getStartingPosition()));
        }
    }

    @Override
    public VolunteersDTO createVolunteersFromRace(
            final String raceId,
            final VolunteersDTO volunteersDTO
    ) {
        logger.info("Attempting to add volunteers to race with id {}", raceId);

        final RaceDocument race = findRaceById(raceId);

        final VolunteersDocument volunteer = new VolunteersDocument();
        volunteersMapper.mapDtoToDocument(volunteersDTO, volunteer);

        volunteer.setCreatedBy(UserContextHolder.getContext().getUserId());
        final VolunteersDocument savedVolunteer = volunteersRepository.save(volunteer);

        race.getVolunteers().add(savedVolunteer);
        raceRepository.save(race);

        logger.info("Successfully added volunteer with id {} to race with id {}",
                volunteer.getId(), raceId);
        return volunteersDTO;
    }

    @Override
    public void addSponsorsToRace(
            final String raceId,
            final RaceDTO.SponsorDTO sponsorDTO,
            final MultipartFile logo
    ) {
        final RaceDocument race = findRaceById(raceId);

        final RaceDocument.Sponsor sponsor = new RaceDocument.Sponsor();
        raceMapper.mapSponsorDtoToDocument(sponsorDTO, sponsor);

        if (logo != null && !logo.isEmpty()) {
            validateLogoFile(logo);

            addLogo(sponsor, logo);
        }

        if (race.getSponsors() == null) {
            race.setSponsors(new ArrayList<>());
        }
        race.getSponsors().add(sponsor);

        raceRepository.save(race);

        raceMapper.mapSponsorDocumentToDto(sponsor);
    }

    @Override
    public List<RaceDTO.SponsorDTO> findSponsors(final String raceId) {
        final RaceDocument race = findRaceById(raceId);

        logger.info("Fetching all sponsors");
        final List<RaceDocument.Sponsor> sponsors = race.getSponsors();
        final List<RaceDTO.SponsorDTO> dto = raceMapper.mapSponsorsDocumentToDto(sponsors);
        logger.info("Found {} sponsors", dto.size());
        return dto;
    }

    @Override
    public RaceDTO.SponsorDTO updateSponsor(
            final String raceId,
            final String sponsorId,
            final RaceDTO.SponsorDTO sponsorDTO) {
        final RaceDocument race = findRaceById(raceId);

        final Optional<RaceDocument.Sponsor> sponsors = race.getSponsors()
                .stream()
                .filter(sponsor -> sponsor.getId().toString().equals(sponsorId))
                .findFirst();

        if (sponsors.isEmpty()) {
            throw new SponsorNotFoundException("Sponsor not found with ID: " + sponsorId);
        }

        final RaceDocument.Sponsor existingSponsor = sponsors.get();

        existingSponsor.setName(sponsorDTO.getName());
        existingSponsor.setContribution(sponsorDTO.getContribution());
        existingSponsor.setContactInfo(sponsorDTO.getContactInfo());
        existingSponsor.setEmail(sponsorDTO.getEmail());

        raceRepository.save(race);

        raceMapper.mapSponsorDocumentToDto(existingSponsor);
        return sponsorDTO;
    }

    @Override
    public void addLogisticsToRace(
            final String raceId,
            final RaceDTO.LogisticsDTO logisticsDTO
    ) {
        final RaceDocument race = findRaceById(raceId);

        final RaceDocument.Logistics logistics = new RaceDocument.Logistics();
        raceMapper.mapLogisticDtoToDocument(logisticsDTO, logistics);

        if (race.getLogistics() == null) {
            race.setLogistics(new ArrayList<>());
        }
        race.getLogistics().add(logistics);

        raceRepository.save(race);
    }

    @Override
    public List<RaceDTO.LogisticsDTO> findLogistics(final String raceId) {
        final RaceDocument race = findRaceById(raceId);

        logger.info("Fetching all logistics");
        final List<RaceDocument.Logistics> logistics = race.getLogistics();
        final List<RaceDTO.LogisticsDTO> dto = raceMapper.mapLogisticsDocumentToDto(logistics);
        logger.info("Found {} logistics", dto.size());
        return dto;
    }

    @Override
    public RaceDTO.LogisticsDTO updateLogistics(
            final String raceId,
            final String logisticsId,
            final RaceDTO.LogisticsDTO logisticsDTO
    ) {
        final RaceDocument race = findRaceById(raceId);

        final Optional<RaceDocument.Logistics> logistics = race.getLogistics()
                .stream()
                .filter(log -> log.getId().toString().equals(logisticsId))
                .findFirst();

        if (logistics.isEmpty()) {
            throw new LogisticsNotFoundException("Logistics not found with ID: " + logisticsId);
        }

        final RaceDocument.Logistics existingLogistics = logistics.get();

        existingLogistics.setTitle(logisticsDTO.getTitle());
        existingLogistics.setDetails(logisticsDTO.getDetails());

        raceRepository.save(race);

        raceMapper.mapLogisticDocumentToDto(existingLogistics);
        return logisticsDTO;
    }

    @Override
    public void addLogoToSponsor(
            final String raceId,
            final String sponsorId,
            final MultipartFile logo
    ) {
        final RaceDocument race = findRaceById(raceId);

        final RaceDocument.Sponsor sponsor = race.getSponsors().stream()
                .filter(s -> s.getId() != null && s.getId().equals(new ObjectId (sponsorId)))
                .findFirst()
                .orElseThrow(() -> new SponsorNotFoundException("Sponsor not found"));

        validateLogoFile(logo);
        addLogo(sponsor, logo);

        raceRepository.save(race);
    }

    @Override
    public ByteArrayResource readLogo(final String raceId, final String sponsorId) {

        final RaceDocument race = findRaceById(raceId);

        final RaceDocument.Sponsor sponsor = race.getSponsors().stream()
                .filter(s -> s.getId() != null && s.getId().equals(new ObjectId (sponsorId)))
                .findFirst()
                .orElseThrow(() -> new SponsorNotFoundException("Sponsor not found"));

        final byte[] logo = sponsor.getLogo();

        if(logo != null) {
            return new ByteArrayResource(logo);
        }else {
            throw new LogoNotFoundException("Logo not found");
        }
    }

    private RaceDocument findRaceById(final String raceId) {
        return raceRepository.findByIdAndDeletedAtIsNull(raceId)
                .orElseThrow(() -> {
                    logger.error("Race with id {} not found or has been deleted", raceId);
                    return new RaceNotFoundException(
                            "Race with id " + raceId + " not found or has been deleted"
                    );
                });
    }

    private void validateLogoFile(final MultipartFile logo) {
        final String fileType = logo.getContentType();
        if (!(("image/png".equals(fileType)) || ("image/jpeg".equals(fileType)))) {
            throw new InvalidLogoException("Only PNG and JPEG files are allowed.");
        }

        if (logo.getSize() > 2 * 1024 * 1024) { // 2MB limit
            throw new InvalidLogoException("File size cannot exceed 2MB.");
        }
    }

    private void addLogo(final RaceDocument.Sponsor sponsor, final MultipartFile logo){
        try {
            final byte[] logoBytes = logo.getBytes();
            sponsor.setLogo(logoBytes);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store logo", e);
        }
    }

    private AuditDocument auditCurrentRace(
            final String raceId,
            final RaceDocument existingRace
    ) {
        final RaceDocument oldRace = existingRace.deepCopy();
        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(oldRace);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record created for old result of race id: {}", raceId);
        return auditDocument;
    }

    private void auditUpdatedRace(
            final String raceId,
            final RaceDocument existingRace,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(existingRace);
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for race id: {}", raceId);
    }
}