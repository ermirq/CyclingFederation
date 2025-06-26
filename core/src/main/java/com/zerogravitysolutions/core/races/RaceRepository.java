package com.zerogravitysolutions.core.races;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RaceRepository extends MongoRepository<RaceDocument, String> {
    Optional<RaceDocument> findByIdAndDeletedAtIsNull(String id);
}