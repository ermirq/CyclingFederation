package com.zerogravitysolutions.core.categories;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryDocument, String> {
    List<CategoryDocument> findAll(Sort sort);

    List<CategoryDocument> findAllById(String categoryId);

    Optional<CategoryDocument> findByIdAndDeletedAtIsNull(String id);
}
