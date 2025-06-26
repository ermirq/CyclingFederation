package com.zerogravitysolutions.core.disciplines;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.disciplines.commons.DisciplineMapper;
import com.zerogravitysolutions.core.disciplines.exceptions.DisciplineNotFoundException;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisciplineServiceImpl implements DisciplineService {

    private static final Logger logger = LoggerFactory.getLogger(DisciplineServiceImpl.class);
    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;
    private final AuditRepository auditRepository;

    public DisciplineServiceImpl(
            final DisciplineRepository disciplineRepository,
            final DisciplineMapper disciplineMapper,
            final AuditRepository auditRepository) {
        this.disciplineRepository = disciplineRepository;
        this.disciplineMapper = disciplineMapper;
        this.auditRepository = auditRepository;
    }

    @Override
    public DisciplineDTO save(final DisciplineDTO dto) {
        logger.info("Saving new RaceDisciplineDTO: {}", dto);
        DisciplineDocument disciplineDocument = new DisciplineDocument();
        disciplineMapper.mapDtoToDocument(dto, disciplineDocument);
        disciplineDocument = disciplineRepository.save(disciplineDocument);

        disciplineDocument.setCreatedBy(UserContextHolder.getContext().getUserId());

        final DisciplineDTO savedDto = disciplineMapper.mapDocumentToDto(disciplineDocument);
        logger.info("Saved RaceDisciplineDTO with ID: {}", savedDto.getId());
        return savedDto;
    }

    @Override
    public DisciplineDTO getById(final String id) {
        logger.info("Fetching RaceDisciplineDTO by ID: {}", id);
        final DisciplineDocument disciplineDocument = findDisciplineById(id);

        final DisciplineDTO dto = disciplineMapper.mapDocumentToDto(disciplineDocument);
        logger.info("Fetched RaceDisciplineDTO: {}", dto);
        return dto;
    }

    @Override
    public void softDelete(final String id) {
        logger.info("Soft deleting RaceDisciplineDTO with ID: {}", id);
        final DisciplineDocument document = findDisciplineById(id);

        final AuditDocument auditDocument = auditCurrentDiscipline(id, document);

        document.setDeletedBy(UserContextHolder.getContext().getUserId());
        document.setDeletedAt(LocalDateTime.now());
        disciplineRepository.save(document);

        auditUpdatedDiscipline(id, document, auditDocument);
        logger.info("Soft deleted RaceDisciplineDTO with ID: {}", id);
    }

    @Override
    public DisciplineDTO updated(final String id, final DisciplineDTO updatedDiscipline) {
        logger.info("Updating RaceDisciplineDTO with ID: {}", id);
        final DisciplineDocument existingDiscipline = findDisciplineById(id);

        final AuditDocument auditDocument = auditCurrentDiscipline(id, existingDiscipline);

        existingDiscipline.setTitle(updatedDiscipline.getTitle());
        existingDiscipline.setDescription(updatedDiscipline.getDescription());
        existingDiscipline.setValidation(updatedDiscipline.getValidation());

        existingDiscipline.setUpdatedBy(UserContextHolder.getContext().getUserId());

        final DisciplineDocument updated = disciplineRepository.save(existingDiscipline);
        final DisciplineDTO updatedDto = disciplineMapper.mapDocumentToDto(updated);
        logger.info("Updated RaceDisciplineDTO: {}", updatedDto);
        auditUpdatedDiscipline(id, existingDiscipline, auditDocument);
        return updatedDto;
    }

    @Override
    public DisciplineDTO patch(final String id, final DisciplineDTO disciplineDTO) {
        logger.info("Patching RaceDisciplineDTO with ID: {}", id);
        final DisciplineDocument disciplineDocument = findDisciplineById(id);

        final AuditDocument auditDocument = auditCurrentDiscipline(id, disciplineDocument);

        disciplineMapper.mapDtoToDocument(disciplineDTO, disciplineDocument);

        disciplineDocument.setUpdatedBy(UserContextHolder.getContext().getUserId());

        final DisciplineDocument patched = disciplineRepository.save(disciplineDocument);
        final DisciplineDTO patchedDto = disciplineMapper.mapDocumentToDto(patched);
        logger.info("Patched RaceDisciplineDTO: {}", patchedDto);

        auditUpdatedDiscipline(id, disciplineDocument, auditDocument);
        return patchedDto;
    }

    @Override
    public List<DisciplineDTO> getAllDisciplines() {
        logger.info("Fetching all RaceDisciplineDTOs");
        final List<DisciplineDocument> documents = disciplineRepository.findAll();
        final List<DisciplineDTO> dtos = disciplineMapper.mapDocumentsToDtos(documents);
        logger.info("Fetched {} RaceDisciplineDTOs", dtos.size());
        return dtos;
    }

    private DisciplineDocument findDisciplineById (final String id) {
        return disciplineRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> {
                    logger.error("Discipline with id {} not found or has been deleted", id);
                    return new DisciplineNotFoundException(
                                    "Discipline with Id " + id + " not found or has been deleted"
                    );
                });
    }

    private AuditDocument auditCurrentDiscipline(
            final String id,
            final DisciplineDocument existingDiscipline
    ) {
        final DisciplineDocument oldDiscipline = existingDiscipline.deepCopy();
        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(oldDiscipline);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record created for old result of discipline id: {}", id);
        return auditDocument;
    }

    private void auditUpdatedDiscipline(
            final String id,
            final DisciplineDocument existingDiscipline,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(existingDiscipline);
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for discipline id: {}", id);
    }
}
