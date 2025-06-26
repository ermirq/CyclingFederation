package com.zerogravitysolutions.core.disciplines;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplineRepository extends MongoRepository <DisciplineDocument, String> {
    Optional<DisciplineDocument> findByIdAndDeletedAtIsNull(String id);

    Optional<DisciplineDocument> findByCode(String code);
}
