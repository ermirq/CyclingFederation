package com.zerogravitysolutions.core.cyclists;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CyclistRepository extends MongoRepository<CyclistDocument, String> {

    Optional<CyclistDocument> findByEmail(String email);

    Page<CyclistDocument> findAll(Pageable page);

    List<CyclistDocument> findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(
            String firstName, String lastName);

    Optional<CyclistDocument> findById(String id);

    Optional<CyclistDocument> findByIdAndDeletedAtIsNull(String id);

}
