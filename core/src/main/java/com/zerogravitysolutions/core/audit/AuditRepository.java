package com.zerogravitysolutions.core.audit;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuditRepository extends MongoRepository<AuditDocument<?>, String> {

    // Derived query to find audit documents by entityType
    List<AuditDocument<?>> findByDocumentType(String documentType);

    // Find audit documents by the author who made the change
    List<AuditDocument<?>> findByAuthor(String author);

    // Find audit documents by entityType within a specific time range
    List<AuditDocument<?>> findByDocumentTypeAndTimestampBetween(Class<?> documentType, LocalDateTime timestamp, LocalDateTime timestamp2);

    // Find the latest audit document for a specific entityType
    Optional<AuditDocument<?>> findFirstByDocumentTypeOrderByTimestampDesc(Class<?> documentType);

    // Find audit documents by entityType and author
    List<AuditDocument<?>> findByDocumentTypeAndAuthor(Class<?> documentType, String author);

    // Count the number of audit documents for a given entityType
    long countByDocumentType(Class<?> documentType);
}
