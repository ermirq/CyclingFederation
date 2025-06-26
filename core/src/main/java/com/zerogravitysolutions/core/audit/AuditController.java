package com.zerogravitysolutions.core.audit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuditController {

    private AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/audits/document/{documentType}")
    public ResponseEntity<List<AuditDocument<?>>> getAuditsByDocumentType(@PathVariable String documentType) {
        List<AuditDocument<?>> audits = auditService.getAuditDocumentsByDocumentType(documentType);
        return ResponseEntity.ok(audits);
    }

//    // Get audits by entity type and time range
//    @GetMapping("/audits/document/{documentType}/time-range")
//    public ResponseEntity<List<AuditDocument<?>>> getAuditsByDocumentTypeAndTimeRange(
//            @PathVariable String documentType,
//            @RequestParam("start") String startStr,
//            @RequestParam("end") String endStr) {
//
//        LocalDateTime start = parseDateTime(startStr, true);
//        LocalDateTime end = parseDateTime(endStr, false);
//
//        List<AuditDocument<?>> audits = auditService.getAuditDocumentsByDocumentTypeAndTimeRange(documentType, start, end);
//        return ResponseEntity.ok(audits);
//    }
//
//    private LocalDateTime parseDateTime(String dateTimeStr, boolean isStart) {
//        // If input is null, return a default start or end date
//        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
//            return isStart ? LocalDateTime.of(1970, 1, 1, 0, 0) : LocalDateTime.now();
//        }
//
//        // Try parsing based on different formats
//        try {
//            // YYYY-MM-DD
//            return LocalDate.parse(dateTimeStr).atStartOfDay();
//        } catch (DateTimeParseException e1) {
//            try {
//                // YYYY-MM
//                return YearMonth.parse(dateTimeStr).atDay(1).atStartOfDay();
//            } catch (DateTimeParseException e2) {
//                try {
//                    // YYYY
//                    return Year.parse(dateTimeStr).atDay(1).atStartOfDay();
//                } catch (DateTimeParseException e3) {
//                    throw new IllegalArgumentException("Invalid date format: " + dateTimeStr);
//                }
//            }
//        }
//    }
//
//    // Get audits by author
//    @GetMapping("/audits/author/{author}")
//    public ResponseEntity<List<AuditDocument<?>>> getAuditsByAuthor(@PathVariable String author) {
//        List<AuditDocument<?>> audits = auditService.getAuditDocumentsByAuthor(author);
//        return ResponseEntity.ok(audits);
//    }
//
//    // Get latest audit by entity type
//    @GetMapping("/audits/document/{documentType}/latest")
//    public ResponseEntity<? extends AuditDocument<?>> getLatestAuditByEntityType(@PathVariable String documentType) {
//        Optional<AuditDocument<?>> latestAudit = auditService.getLatestAuditByEntityType(documentType);
//        return latestAudit.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.noContent().build());
//    }
//
//    // Get audits by entity type and author
//    @GetMapping("/audits/document/{documentType}/author/{author}")
//    public ResponseEntity<List<AuditDocument<?>>> getAuditsByDocumentTypeAndAuthor(
//            @PathVariable String documentType,
//            @PathVariable String author) {
//        List<AuditDocument<?>> audits = auditService.getAuditDocumentsByDocumentTypeAndAuthor(documentType, author);
//        return ResponseEntity.ok(audits);
//    }
//
//    // Get count of audits by entity type
//    @GetMapping("/document/{documentType}/count")
//    public ResponseEntity<Long> getCountByDocumentType(@PathVariable String documentType) {
//        long count = auditService.countAuditDocumentsByDocumentType(documentType);
//        return ResponseEntity.ok(count);
//    }
}