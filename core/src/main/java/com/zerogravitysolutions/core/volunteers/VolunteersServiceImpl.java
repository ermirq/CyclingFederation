package com.zerogravitysolutions.core.volunteers;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.members.exceptions.MemberNotFoundException;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import com.zerogravitysolutions.core.volunteers.commons.VolunteersMapper;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersDuplicateException;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VolunteersServiceImpl implements VolunteersService {
    private static final Logger logger = LoggerFactory.getLogger(VolunteersServiceImpl.class);
    private final VolunteersRepository volunteersRepository;
    private final VolunteersMapper volunteersMapper;
    private final AuditRepository auditRepository;

    public VolunteersServiceImpl(
            final VolunteersRepository volunteersRepository,
            final VolunteersMapper volunteersMapper,
            final AuditRepository auditRepository) {
        this.volunteersRepository = volunteersRepository;
        this.volunteersMapper = volunteersMapper;
        this.auditRepository = auditRepository;
    }

    @Override
    public VolunteersDTO save(final VolunteersDTO volunteersDTO) {
        logger.info("Saving a new volunteer: {}", volunteersDTO);
        VolunteersDocument volunteersDocument = new VolunteersDocument();
        volunteersMapper.mapDtoToDocument(volunteersDTO, volunteersDocument);

        volunteersDocument.setCreatedBy(UserContextHolder.getContext().getUserId());
        volunteersDocument = volunteersRepository.save(volunteersDocument);
        final VolunteersDTO savedDto = volunteersMapper.mapDocumentToDto(volunteersDocument);
        logger.info("Saved volunteer: {}", savedDto);
        return savedDto;
    }

    @Override
    public List<VolunteersDTO> findAll() {
        logger.info("Fetching all volunteers");
        final List<VolunteersDocument> documents = volunteersRepository.findAll();
        final List<VolunteersDTO> dto = volunteersMapper.mapDocumentsToDtos(documents);
        logger.info("Found {} volunteers", dto.size());
        return dto;
    }

    @Override
    public VolunteersDTO getById(final String id) {
        logger.info("Fetching volunteer with id: {}", id);
        final VolunteersDocument volunteersDocument = findVolunteersById(id);
        final VolunteersDTO dto = volunteersMapper.mapDocumentToDto(volunteersDocument);
        logger.info("Found volunteer: {}", dto);
        return dto;
    }

    @Override
    public void softDelete(final String id) {
        logger.info("Soft deleting volunteer with id: {}", id);
        final VolunteersDocument volunteer = findVolunteersById(id);
        final AuditDocument auditDocument = auditCurrentVolunteer(id, volunteer);

        volunteer.setDeletedBy(UserContextHolder.getContext().getUserId());
        volunteer.setDeletedAt(LocalDateTime.now());
        volunteersRepository.save(volunteer);

        auditUpdatedVolunteer(id, volunteer, auditDocument);
        logger.info("Soft deleted volunteer with id: {}", id);
    }

    @Override
    public VolunteersDTO update(final String id, final VolunteersDTO updatedVolunteer) {
        logger.info("Updating volunteer with id: {}", id);
        final VolunteersDocument existingVolunteer = findVolunteersById(id);

        final AuditDocument auditDocument = auditCurrentVolunteer(id, existingVolunteer);

        existingVolunteer.setName(updatedVolunteer.getName());
        existingVolunteer.setRole(updatedVolunteer.getRole());
        existingVolunteer.setContactInfo(updatedVolunteer.getContactInfo());
        existingVolunteer.setEmail(updatedVolunteer.getEmail());

        existingVolunteer.setUpdatedBy(UserContextHolder.getContext().getUserId());
        final VolunteersDocument updated = volunteersRepository.save(existingVolunteer);
        final VolunteersDTO updatedDto = volunteersMapper.mapDocumentToDto(updated);

        logger.info("Updated volunteer: {}", updatedDto);

        auditUpdatedVolunteer(id, existingVolunteer, auditDocument);
        return updatedDto;
    }

    @Override
    public VolunteersDTO patch(final String id, final VolunteersDTO volunteersDTO) {
        logger.info("Patching volunteer with id: {}", id);
        final VolunteersDocument volunteersDocument = findVolunteersById(id);

        final AuditDocument auditDocument = auditCurrentVolunteer(id,volunteersDocument);

        if (volunteersRepository.findByEmail(volunteersDTO.getEmail()).isPresent()) {
            logger.warn("Duplicate email detected: {}", volunteersDTO.getEmail());
            throw new VolunteersDuplicateException(
                    "This email is already being used. Please use another email!"
            );
        }

        volunteersDocument.setUpdatedBy(UserContextHolder.getContext().getUserId());
        volunteersMapper.mapDtoToDocument(volunteersDTO, volunteersDocument);
        final VolunteersDocument patched = volunteersRepository.save(volunteersDocument);
        final VolunteersDTO patchedDto = volunteersMapper.mapDocumentToDto(patched);

        logger.info("Patched volunteer: {}", patchedDto);

        auditUpdatedVolunteer(id, volunteersDocument, auditDocument);
        return patchedDto;
    }

    @Override
    public List<VolunteersDTO> findByName(final String name) {
        logger.info("Finding volunteer by name: {}", name);
        final List<VolunteersDocument> volunteersDocuments =
                volunteersRepository.findByNameContainsIgnoreCase(name);

        final List<VolunteersDocument> activeVolunteers = volunteersDocuments.stream()
                .filter(volunteer -> volunteer.getDeletedAt() == null)
                .toList();

        if (activeVolunteers.isEmpty()) {
            logger.warn("No active volunteer found with name: {}", name);
            throw new MemberNotFoundException(
                    "Volunteer with name " + name + " not found or has been deleted."
            );
        }

        return volunteersMapper.mapDocumentsToDtos(volunteersDocuments);
    }

    private VolunteersDocument findVolunteersById(final String id) {
        return volunteersRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> {
                    logger.error("Volunteer with id {} not found or has been deleted", id);
                    return new VolunteersNotFoundException(
                            "Volunteer with ID " + id + " not found or has been deleted!");
                });
    }

    private AuditDocument auditCurrentVolunteer(
            final String id,
            final VolunteersDocument volunteer
    ) {
        final VolunteersDocument oldVolunteer = volunteer.deepCopy();
        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(oldVolunteer);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record created for old result of volunteer id: {}", id);
        return auditDocument;
    }

    private void auditUpdatedVolunteer(
            final String id,
            final VolunteersDocument volunteer,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(volunteer);
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for volunteer id: {}", id);
    }
}
