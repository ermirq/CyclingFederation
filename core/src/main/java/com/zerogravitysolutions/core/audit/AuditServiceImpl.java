package com.zerogravitysolutions.core.audit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService{

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public List<AuditDocument<?>> getAuditDocumentsByDocumentType(String documentType) {
        if (documentType.equals("CyclistDocument")) {
            documentType = "com.zerogravitysolutions.core.cyclists.CyclistDocument";
        }

        List<AuditDocument<?>> auditDocuments = auditRepository.findByDocumentType(documentType);
        return auditDocuments;
    }
}
