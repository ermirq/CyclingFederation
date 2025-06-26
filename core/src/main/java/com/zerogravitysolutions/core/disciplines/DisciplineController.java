package com.zerogravitysolutions.core.disciplines;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DisciplineController {

    private final DisciplineService raceDisciplineService;

    public DisciplineController(final DisciplineService raceDisciplineService) {
        this.raceDisciplineService = raceDisciplineService;
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/disciplines")
    public ResponseEntity<DisciplineDTO> createDiscipline(
            @Valid @RequestBody final DisciplineDTO raceDisciplineDTO
    ) {
        final DisciplineDTO newDiscipline = raceDisciplineService.save(raceDisciplineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDiscipline);
    }

    @RolesAllowed({"Administrator", "Member", "Cyclist"})
    @GetMapping(path = "/disciplines/{id}")
    public ResponseEntity<DisciplineDTO> getDisciplineById(@PathVariable final String id) {
        final DisciplineDTO disciplineFound = raceDisciplineService.getById(id);
        return ResponseEntity.ok(disciplineFound);
    }

    @RolesAllowed("Administrator")
    @GetMapping(path = "/disciplines")
    public ResponseEntity<List<DisciplineDTO>> findAll() {
        final List<DisciplineDTO> disciplines = raceDisciplineService.getAllDisciplines();
        return ResponseEntity.ok(disciplines);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/disciplines/{id}")
    public ResponseEntity<Void> deleteDisciplineById(@PathVariable final String id) {
        raceDisciplineService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @PutMapping("/disciplines/{id}")
    public ResponseEntity<DisciplineDTO> updateDiscipline(
            @PathVariable final String id,
            @RequestBody final DisciplineDTO updatedDiscipline
    ) {
        final DisciplineDTO raceDisciplineDTO =
                raceDisciplineService.updated(id, updatedDiscipline);
        return ResponseEntity.ok(raceDisciplineDTO);
    }

    @RolesAllowed("Administrator")
    @PatchMapping("/disciplines/{id}")
    public ResponseEntity<DisciplineDTO> patch(
            @PathVariable final String id,
            @RequestBody final DisciplineDTO raceDisciplineDTO
    ) {
        final DisciplineDTO patched = raceDisciplineService.patch(id, raceDisciplineDTO);
        return ResponseEntity.ok(patched);
    }
}
