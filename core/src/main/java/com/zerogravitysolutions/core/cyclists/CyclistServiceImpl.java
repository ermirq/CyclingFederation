package com.zerogravitysolutions.core.cyclists;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.categories.CategoryDocument;
import com.zerogravitysolutions.core.categories.CategoryRepository;
import com.zerogravitysolutions.core.categories.exceptions.CategoryDuplicateException;
import com.zerogravitysolutions.core.categories.exceptions.CategoryNotFoundException;
import com.zerogravitysolutions.core.cyclists.commons.CyclistMapper;
import com.zerogravitysolutions.core.cyclists.events.CyclistEvent;
import com.zerogravitysolutions.core.cyclists.exceptions.CyclistAgeRestrictionException;
import com.zerogravitysolutions.core.cyclists.exceptions.CyclistDuplicateException;
import com.zerogravitysolutions.core.cyclists.exceptions.CyclistNotFoundException;
import com.zerogravitysolutions.core.events.EventType;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CyclistServiceImpl implements CyclistService {

    private static final Logger logger = LoggerFactory.getLogger(CyclistServiceImpl.class);
    private final CyclistRepository cyclistRepository;
    private final CyclistMapper cyclistMapper;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuditRepository auditRepository;

    public CyclistServiceImpl(
            final CyclistRepository cyclistRepository,
            final CyclistMapper cyclistMapper,
            final CategoryRepository categoryRepository,
            final ApplicationEventPublisher applicationEventPublisher,
            final AuditRepository auditRepository) {
        this.cyclistRepository = cyclistRepository;
        this.cyclistMapper = cyclistMapper;
        this.categoryRepository = categoryRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.auditRepository = auditRepository;
    }

    @Override
    public CyclistDTO save(final CyclistDTO cyclistDto) {
        logger.info("Attempting to save cyclist with email: {}", cyclistDto.getEmail());

        if (cyclistRepository.findByEmail(cyclistDto.getEmail()).isPresent()) {
            logger.warn("Duplicate email found: {}", cyclistDto.getEmail());
            throw new CyclistDuplicateException(
                    "This email is already being used. Please use another email!"
            );
        } else if (cyclistDto.getAge() < 15) {
            logger.warn("Invalid age provided: {}", cyclistDto.getAge());
            throw new CyclistAgeRestrictionException("The age must be above or equals to 15.");
        } else {
            CyclistDocument cyclistDocument = new CyclistDocument();
            cyclistMapper.mapDtoToDocument(cyclistDto, cyclistDocument);
            cyclistDocument.setCreatedBy(UserContextHolder.getContext().getUserId());

            cyclistDocument = cyclistRepository.save(cyclistDocument);
            logger.info("Saved cyclist with ID: {}", cyclistDocument.getId());

            applicationEventPublisher.publishEvent(
                    new CyclistEvent(EventType.CYCLIST_CREATED,
                            cyclistDto)
            );

            return cyclistMapper.mapDocumentToDto(cyclistDocument);

        }
    }

    @Override
    public CyclistDTO patch(final String id, final CyclistDTO cyclistDto) {
        logger.info("Patching cyclist with ID: {}", id);
        final CyclistDocument cyclistDocument = findCyclistById(id);
        final AuditDocument auditDocument = auditCurrentCyclist(id, cyclistDocument);

        if (cyclistDocument.getAge() != null && cyclistDocument.getAge() <= 18) {
            logger.warn("Age restriction violated for cyclist with ID: {}", id);
            throw new CyclistAgeRestrictionException(
                    "Age violation. The age you gave is under 18 years old!"
            );
        }

        if (cyclistDto.getEmail() != null &&
                cyclistRepository.findByEmail(cyclistDto.getEmail()).isPresent()) {
            logger.warn("Duplicate email detected: {}", cyclistDto.getEmail());
            throw new CyclistDuplicateException(
                    "This email is already being used. Please use another email!"
            );
        }

        cyclistDocument.setUpdatedBy(UserContextHolder.getContext().getUserId());

        cyclistMapper.mapDtoToDocument(cyclistDto, cyclistDocument);
        final CyclistDocument patched = cyclistRepository.save(cyclistDocument);
        logger.info("Patched cyclist with ID: {}", patched.getId());

        auditUpdatedCyclist(id, cyclistDocument, auditDocument);

        return cyclistMapper.mapDocumentToDto(patched);
    }

    @Override
    public Page<CyclistDTO> getAllCyclists(final Pageable pageable) {
        logger.info("Fetching all cyclists with pagination");
        final Page<CyclistDocument> cyclistDocuments = cyclistRepository.findAll(pageable);
        final Page<CyclistDTO> cyclistDTOS = cyclistDocuments.map(cyclistMapper::mapDocumentToDto);
        logger.info("Fetched {} clubs with pagination", cyclistDocuments.getTotalElements());
        return cyclistDTOS;
    }

    @Override
    public List<CyclistDTO> findByName(final String name) {
        logger.info("Finding cyclists by name: {}", name);
        final List<CyclistDocument> cyclistDocuments =
                cyclistRepository.
                        findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(name, name);

        final List<CyclistDocument> activeCyclists = cyclistDocuments.stream()
                .filter(cyclist -> cyclist.getDeletedAt() == null)
                .toList();

        if (activeCyclists.isEmpty()) {
            logger.warn("No active cyclist found with name: {}", name);
            throw new CyclistNotFoundException(
                    "Cyclist with name " + name + " not found or has been deleted."
            );
        }

        return cyclistMapper.mapDocumentToDto(cyclistDocuments);
    }

    @Override
    public CyclistDTO getById(final String id) {
        logger.info("Fetching cyclist with ID: {}", id);
        final CyclistDocument cyclistDocument = findCyclistById(id);
        return cyclistMapper.mapDocumentToDto(cyclistDocument);
    }

    @Override
    public void softDelete(final String id) {
        logger.info("Deleting Cyclist with ID: {}", id);
        final CyclistDocument cyclist = findCyclistById(id);

        final AuditDocument auditDocument = auditCurrentCyclist(id, cyclist);

        cyclist.setDeletedBy(UserContextHolder.getContext().getUserId());
        cyclist.setDeletedAt(LocalDateTime.now());

        cyclistRepository.save(cyclist);
        auditUpdatedCyclist(id, cyclist, auditDocument);
        logger.info("Cyclist soft deleted successfully: {}", cyclist);
    }

    @Override
    public CyclistDTO updated(final String id, final CyclistDTO updatedCyclist) {
        logger.info("Updating cyclist with ID: {}", id);
        final CyclistDocument existingCyclist = findCyclistById(id);

        final AuditDocument auditDocument = auditCurrentCyclist(id, existingCyclist);

        existingCyclist.setFirstName(updatedCyclist.getFirstName());
        existingCyclist.setLastName(updatedCyclist.getLastName());
        existingCyclist.setEmail(updatedCyclist.getEmail());

        if (updatedCyclist.getAge() != null && updatedCyclist.getAge() <= 18) {
            logger.warn("Age restriction violated for cyclist with ID: {}", id);
            throw new CyclistAgeRestrictionException(
                    "This person is too young to be part of the club!"
            );
        }
        existingCyclist.setAge(updatedCyclist.getAge());
        existingCyclist.setGender(updatedCyclist.getGender());
        existingCyclist.setNationality(updatedCyclist.getNationality());

        existingCyclist.setUpdatedBy(UserContextHolder.getContext().getUserId());

        final CyclistDocument updated = cyclistRepository.save(existingCyclist);
        logger.info("Updated cyclist with ID: {}", updated.getId());

        auditUpdatedCyclist(id, existingCyclist, auditDocument);

        return cyclistMapper.mapDocumentToDto(updated);
    }

    @Override
    public void addCategoryToCyclist(final String cyclistId, final String categoryId) {
        logger.info("Attempting to add category to cyclist with id {}", cyclistId);

        final CyclistDocument cyclist = findCyclistById(cyclistId);

        final CategoryDocument categoryDocument =
                categoryRepository.findById(categoryId).orElseThrow(() -> {
            logger.error("Category with id {} not found", categoryId);
            return new CategoryNotFoundException("Category was not found");
        });

        if (cyclist.getCategory() == null) {
            cyclist.setCategory(categoryDocument);
            cyclistRepository.save(cyclist);
            logger.info("Successfully added category to cyclist with id {}", categoryId);
        } else {
            logger.error("This cyclist already has a category");
            throw new CategoryDuplicateException("This cyclist already has a category");
        }
        cyclist.setCategory(categoryDocument);
        cyclistRepository.save(cyclist);
        logger.info("Successfully added category to cyclist with id {}", cyclistId);
    }


    @Override
    public void deleteCategoryFromCyclist(final String cyclistId, final String categoryId) {
        logger.info("Deleting category with ID: {} from race with ID: {}", categoryId, cyclistId);
        final CyclistDocument cyclistDocument = findCyclistById(cyclistId);

        final CategoryDocument categoryDocument = cyclistDocument.getCategory();
        if (categoryDocument != null && categoryDocument.getId().equals(categoryId)) {
            cyclistDocument.setCategory(null);
        } else {
            throw new CategoryNotFoundException(
                    "Category with ID " + categoryId + " not found in cyclist with ID " + cyclistId
            );
        }
        cyclistRepository.save(cyclistDocument);
        logger.info("Deleted category with ID: {} from cyclist with ID: {}", categoryId, cyclistId);
    }

    private CyclistDocument findCyclistById (final String id){
        return cyclistRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> {
                    logger.error("Cyclist with id {} not found or has been deleted", id);
                    return new CyclistNotFoundException(
                            "Cyclist with id " + id + " not found or has been deleted"
                    );
                });
    }

    private AuditDocument<CyclistDocument> auditCurrentCyclist(final String id, final CyclistDocument cyclist) {
        if (cyclist == null) {
            throw new IllegalArgumentException("Cyclist cannot be null");
        }

        final CyclistDocument oldCyclist = cyclist.deepCopy();
        final LocalDateTime timestamp = LocalDateTime.now();
        final String author = UserContextHolder.getContext().getUserId();

        // Create an AuditDocument using the existing constructor
        final AuditDocument<CyclistDocument> auditDocument = new AuditDocument<>(
                oldCyclist,             // oldDocument
                null,                   // newDocument (since this is for old state)
                CyclistDocument.class,  // documentType
                timestamp,              // timestamp
                author                  // author
        );

        try {
            auditRepository.save(auditDocument);
            logger.debug("Audit record created for old result of cyclist id: {}", id);
        } catch (Exception e) {
            logger.error("Error saving audit document for cyclist id: {}", id, e);
            // Handle exception (rethrow, log, etc.)
        }

        return auditDocument;
    }


    private void auditUpdatedCyclist(
            final String id,
            final CyclistDocument existingCyclist,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(existingCyclist);
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for cyclist id: {}", id);
    }
}
