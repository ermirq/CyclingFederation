package com.zerogravitysolutions.core.members;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<MemberDocument, String> {
    Optional<Object> findByEmail(String email);

    Optional<Object> findByPhoneNumber(String phoneNumber);

    Optional<MemberDocument> findByIdAndDeletedAtIsNull(String id);

    List<MemberDocument> findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCase(
            String firstName, String lastName);
}
