package com.zerogravitysolutions.core.clubs;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.clubs.exceptions.ClubNotFoundException;
import com.zerogravitysolutions.core.clubs.exceptions.DuplicateClubException;
import com.zerogravitysolutions.core.clubs.exceptions.InvalidClubDataException;
import com.zerogravitysolutions.core.clubs.mapper.ClubMapper;
import com.zerogravitysolutions.core.cyclists.CyclistDocument;
import com.zerogravitysolutions.core.cyclists.CyclistRepository;
import com.zerogravitysolutions.core.cyclists.exceptions.CyclistDuplicateException;
import com.zerogravitysolutions.core.cyclists.exceptions.CyclistNotFoundException;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClubServiceImpl implements ClubService {

    private static final Logger logger = LoggerFactory.getLogger(ClubServiceImpl.class);
    private final ClubRepository clubRepository;
    private final CyclistRepository cyclistRepository;
    private final ClubMapper clubMapper;
    private final AuditRepository auditRepository;

    public ClubServiceImpl(
            final ClubRepository clubRepository,
            final CyclistRepository cyclistRepository,
            final ClubMapper clubMapper,
            final AuditRepository auditRepository) {
        this.clubRepository = clubRepository;
        this.cyclistRepository = cyclistRepository;
        this.clubMapper = clubMapper;
        this.auditRepository = auditRepository;
    }

    @Override
    public ClubDTO saveClub(final ClubDTO clubDto) {
        logger.info("Saving club: {}", clubDto);
        if (clubRepository.existsByName(clubDto.getName())) {
            logger.error("A club with this name " + clubDto.getName() + " already exists!");
            throw new DuplicateClubException(
                    "A club with the name " + clubDto.getName() + " already exists!"
            );
        }

        final ClubDocument clubDocument = clubMapper.mapDtoToDocument(clubDto);
        clubDocument.setCreatedBy(UserContextHolder.getContext().getUserId());

        clubRepository.save(clubDocument);
        logger.info("Club saved successfully: {}", clubDocument);

        return clubMapper.mapDocumentToDto(clubDocument);
    }

    @Override
    public List<ClubDTO> fetchAllClubs() {
        logger.info("Fetching all clubs");
        final List<ClubDocument> clubDocuments = clubRepository.findAll();
        logger.info("Fetched {} clubs", clubDocuments.size());
        return clubMapper.mapDocumentsToDtos(clubDocuments);
    }

    @Override
    public ClubDTO getById(final String id) {
        logger.info("Fetching club with ID {}", id);
        final ClubDocument clubDocument = findClubById(id);

        logger.info("Fetched club: {}", clubDocument);

        return clubMapper.mapDocumentToDto(clubDocument);
    }

    @Override
    public ClubDTO update(final String id, final ClubDTO clubDto) {
        logger.info("Updating club with ID: {}", id);
        final ClubDocument clubDocument = findClubById(id);

        final AuditDocument auditDocument = auditCurrentClub(id, clubDocument);

        if (clubDto.getName() == null || clubDto.getName().isEmpty()) {
            logger.error("Invalid club data provided for update: {}", clubDto);
            throw new InvalidClubDataException("Club name can not be null or empty");
        }

        clubDocument.setName(clubDto.getName());
        clubDocument.setEmail(clubDto.getEmail());
        clubDocument.setPhone(clubDto.getPhone());
        clubDocument.setClubSize(clubDto.getClubSize());
        clubDocument.setLocation(clubDto.getLocation());
        clubDocument.setRegistrationDate(clubDto.getRegistrationDate());

        clubDocument.setUpdatedBy(UserContextHolder.getContext().getUserId());

        final ClubDocument updated = clubRepository.save(clubDocument);

        auditUpdatedClub(id, clubDocument, auditDocument);
        logger.info("Club updated successfully: {}", updated);

        return clubMapper.mapDocumentToDto(updated);
    }

    @Override
    public void softDelete(final String id) {
        logger.info("Deleting club with ID: {}", id);
        final ClubDocument club = findClubById(id);

        final AuditDocument auditDocument = auditCurrentClub(id, club);

        club.setDeletedBy(UserContextHolder.getContext().getUserId());
        club.setDeletedAt(LocalDateTime.now());

        clubRepository.save(club);

        auditUpdatedClub(id, club, auditDocument);
        logger.info("Club soft deleted successfully: {}", club);
    }

    @Override
    public Page<ClubDTO> getAll(final Pageable pageable) {
        logger.info("Fetching all clubs with pagination");
        final Page<ClubDocument> clubs = clubRepository.findAll(pageable);
        final Page<ClubDTO> clubDTOS = clubs.map(clubMapper::mapDocumentToDto);
        logger.info("Fetched {} clubs with pagination", clubs.getTotalElements());

        return clubDTOS;
    }

    @Override
    public Set<ClubDTO> findByName(final String name) {
        logger.info("Finding club/s by name: {}", name);
        final Set<ClubDocument> clubDocuments = clubRepository.findByNameContainingIgnoreCase(name);
        logger.info("Found {} club/s by name: {}", clubDocuments.size(), name);

        return clubMapper.mapDocumentToDto(clubDocuments);
    }

    @Override
    public void addCyclistToRace(final String clubId, final String cyclistId) {
        logger.info("Attempting to add cyclist to club with id {}", clubId);

        final ClubDocument club = findClubById(clubId);

        final Optional<CyclistDocument> cyclist = cyclistRepository.findById(cyclistId);
        if (cyclist.isPresent()) {
            final CyclistDocument cyclistDocument = cyclist.get();

            if (cyclistDocument.getClubId() != null && !cyclistDocument.getClubId().isEmpty()) {
                throw new CyclistDuplicateException("This cyclist is already part of a club");
            } else {
                cyclistDocument.setClubId(clubId);
                cyclistRepository.save(cyclistDocument);
                club.getCyclists().add(cyclistDocument);
                clubRepository.save(club);

                logger.info("Successfully added cyclist to club with id {}", clubId);
            }
        }
    }


    @Override
    public void deleteCyclistFromRace(final String clubId, final String cyclistId) {
        logger.info("Deleting cyclist with ID: {} from club with ID: {}", cyclistId, clubId);
        final ClubDocument club = findClubById(clubId);

        final List<CyclistDocument> cyclists = club.getCyclists();
        if (cyclists != null) {
            final boolean removed = cyclists.removeIf(cyclist -> cyclist.getId().equals(cyclistId));
            final CyclistDocument cyclistDocument = cyclistRepository.findById(cyclistId)
                    .orElseThrow(
                            () -> new CyclistNotFoundException(
                                    "Cyclist with ID " + cyclistId + " not found"
                            )
                    );
            if (removed) {
                cyclistDocument.setClubId(null);
                cyclistRepository.save(cyclistDocument);
            }
            }else {
            throw  new CyclistNotFoundException(
                    "Cyclist with ID " + cyclistId + " not found in club with ID " + clubId
            );
        }
        clubRepository.save(club);
        logger.info("Deleted cyclist with ID: {} from club with ID: {}", cyclistId, clubId);
    }

    private ClubDocument findClubById(final String id) {
        return clubRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> {
                    logger.error("Club with id {} not found or has been deleted", id);
                    return new ClubNotFoundException(
                            "Club with id " + id + " not found or has been deleted"
                    );
                });
    }

    private AuditDocument auditCurrentClub(final String id, final ClubDocument club) {
        final ClubDocument oldClub = club.deepCopy();
        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(oldClub);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record created for old result of club id: {}", id);
        return auditDocument;
    }

    private void auditUpdatedClub(
            final String id,
            final ClubDocument club,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(club);
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for club id: {}", id);
    }
}
