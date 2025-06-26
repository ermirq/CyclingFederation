package com.zerogravitysolutions.core.audit;

import com.zerogravitysolutions.core.audit.exceptions.InvalidAuditDocumentException;
import com.zerogravitysolutions.core.commons.BaseDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "audits")
public class AuditDocument<T> extends BaseDocument {

    private T oldDocument;
    private T newDocument;
    private String documentType;
    private LocalDateTime timestamp;
    private String author;

    public AuditDocument() {
    }

    public AuditDocument(T oldDocument, T newDocument, Class<?> documentType, LocalDateTime timestamp, String author) {
        if (oldDocument == null && newDocument == null) {
            throw new InvalidAuditDocumentException("Both oldDocument and newDocument cannot be null simultaneously");
        }
        this.oldDocument = oldDocument;
        this.newDocument = newDocument;
        this.documentType = documentType.getSimpleName();
        this.timestamp = timestamp;
        this.author = author;
    }

    public T getOldDocument() {
        return oldDocument;
    }

    public void setOldDocument(T oldDocument) {
        this.oldDocument = oldDocument;
    }

    public T getNewDocument() {
        return newDocument;
    }

    public void setNewDocument(T newDocument) {
        this.newDocument = newDocument;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}