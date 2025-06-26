package com.zerogravitysolutions.core.audit;

import java.util.List;

public interface AuditService {
    List<AuditDocument<?>> getAuditDocumentsByDocumentType(String documentType);
}
