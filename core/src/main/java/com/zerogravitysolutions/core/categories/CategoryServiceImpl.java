package com.zerogravitysolutions.core.categories;

import com.zerogravitysolutions.core.audit.AuditDocument;
import com.zerogravitysolutions.core.audit.AuditRepository;
import com.zerogravitysolutions.core.categories.commons.CategoryMapper;
import com.zerogravitysolutions.core.categories.exceptions.CategoryNotFoundException;
import com.zerogravitysolutions.core.utilities.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuditRepository auditRepository;

    public CategoryServiceImpl(
            final CategoryRepository categoryRepository,
            final CategoryMapper categoryMapper,
            final AuditRepository auditRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.auditRepository = auditRepository;
    }

    @Override
    public CategoryDTO save(final CategoryDTO dto) {
        logger.info("Saving new Race Category: {}", dto);
        CategoryDocument categoryDocument = new CategoryDocument();
        categoryMapper.mapDtoToDocument(dto, categoryDocument);

        categoryDocument.setCreatedBy(UserContextHolder.getContext().getUserId());

        categoryDocument = categoryRepository.save(categoryDocument);
        final CategoryDTO savedDto = categoryMapper.mapDocumentToDto(categoryDocument);
        logger.info("Saved Race Category with ID: {}", savedDto.getId());
        return savedDto;
    }

    @Override
    public CategoryDTO getById(final String id) {
        logger.info("Fetching Race Category by ID: {}", id);
        final CategoryDocument categoryDocument = findCategoryById(id);
        final CategoryDTO dto = categoryMapper.mapDocumentToDto(categoryDocument);
        logger.info("Fetched Race Category: {}", dto);
        return dto;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        logger.info("Fetching all Race Categories");
        final List<CategoryDocument> documents =categoryRepository.findAll();
        final List<CategoryDTO> dtos = categoryMapper.mapDocumentsToDtos(documents);
        logger.info("Fetched {} Race Categories", dtos.size());
        return dtos;
    }

    @Override
    public void softDelete(final String id) {
        logger.info("Soft deleting Race Category with ID: {}", id);
        final CategoryDocument document = findCategoryById(id);

        final AuditDocument auditDocument = auditCurrentCategory(id, document);

        document.setDeletedBy(UserContextHolder.getContext().getUserId());
        document.setDeletedAt(LocalDateTime.now());
        final CategoryDocument updated = categoryRepository.save(document);
        auditUpdatedCategory(id, document, auditDocument);
        logger.info("Soft deleted Race Category with ID: {}", id);
    }

    @Override
    public CategoryDTO updated(final String id, final CategoryDTO updatedCategory) {
        logger.info("Updating Race Category with ID: {}", id);
        final CategoryDocument existingCategory = findCategoryById(id);

        final AuditDocument auditDocument = auditCurrentCategory(id, existingCategory);


        existingCategory.setCategoryName(updatedCategory.getCategoryName());
        existingCategory.setDescription(updatedCategory.getDescription());
        existingCategory.setColor(updatedCategory.getColor());
        existingCategory.setMinAge(updatedCategory.getMinAge());
        existingCategory.setMaxAge(updatedCategory.getMaxAge());

        existingCategory.setUpdatedBy(UserContextHolder.getContext().getUserId());

        final CategoryDocument updated = categoryRepository.save(existingCategory);
        final CategoryDTO updatedDto = categoryMapper.mapDocumentToDto(updated);
        logger.info("Updated Race Category with ID: {}", id);

        auditUpdatedCategory(id, existingCategory, auditDocument);

        return updatedDto;
    }

    @Override
    public CategoryDTO patch(final String id, final CategoryDTO categoryDTO) {
        logger.info("Patching Race Category with ID: {}", id);
        final CategoryDocument categoryDocument = findCategoryById(id);

        final AuditDocument auditDocument = auditCurrentCategory(id, categoryDocument);


        categoryMapper.mapDtoToDocument(categoryDTO, categoryDocument);
        categoryDocument.setUpdatedBy(UserContextHolder.getContext().getUserId());

        final CategoryDocument patched = categoryRepository.save(categoryDocument);
        final CategoryDTO patchedDto = categoryMapper.mapDocumentToDto(patched);
        logger.info("Patched Race Category with ID: {}", id);

        auditUpdatedCategory(id, categoryDocument, auditDocument);

        return patchedDto;
    }

    private CategoryDocument findCategoryById (final String id) {
        return categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> {
                    logger.error("Category with id {} not found or has been deleted", id);
                    return new CategoryNotFoundException(
                            "Category with id " + id + " not found or has been deleted"
                    );
                });
    }

    private AuditDocument auditCurrentCategory(
            final String id,
            final CategoryDocument existingCategory
    ) {
        final CategoryDocument oldCategory = existingCategory.deepCopy();
        final AuditDocument auditDocument = new AuditDocument();
        auditDocument.setOldDocument(oldCategory);
        auditDocument.setTimestamp(LocalDateTime.now());
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record created for old result of category id: {}", id);
        return auditDocument;
    }

    private void auditUpdatedCategory(
            final String id,
            final CategoryDocument existingCategory,
            final AuditDocument auditDocument
    ) {
        auditDocument.setNewDocument(existingCategory);
        auditDocument.setAuthor(UserContextHolder.getContext().getUserId());
        auditRepository.save(auditDocument);
        logger.debug("Audit record updated for category id: {}", id);
    }
}
