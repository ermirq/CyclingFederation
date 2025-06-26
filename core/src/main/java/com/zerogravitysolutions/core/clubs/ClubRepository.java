package com.zerogravitysolutions.core.clubs;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ClubRepository extends MongoRepository<ClubDocument, String> {

    Set<ClubDocument> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);

    Optional<ClubDocument> findByIdAndDeletedAtIsNull(String id);
}
