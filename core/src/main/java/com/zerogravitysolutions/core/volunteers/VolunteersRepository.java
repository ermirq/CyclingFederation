package com.zerogravitysolutions.core.volunteers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VolunteersRepository extends MongoRepository <VolunteersDocument, String> {
    Optional<VolunteersDocument> findByIdAndDeletedAtIsNull(String id);

    List<VolunteersDocument> findByNameContainsIgnoreCase(String firstName);

    Optional<Object> findByEmail(String email);
}
