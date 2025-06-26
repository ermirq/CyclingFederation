package com.zerogravitysolutions.core.cyclists;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CyclistController {

    private final CyclistService cyclistService;

    public CyclistController(final CyclistService cyclistService) {
        this.cyclistService = cyclistService;
    }

    @RolesAllowed({"Administrator", "Cyclist"})
    @PostMapping("/cyclists")
    public ResponseEntity<CyclistDTO> createCyclist (
            @Valid @RequestBody final CyclistDTO cyclistDto
    ) {
        final CyclistDTO newCyclist = cyclistService.save(cyclistDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCyclist);
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/cyclists/page")
    public ResponseEntity<Page<CyclistDTO>> findAll(final Pageable pageable){
        final Page<CyclistDTO> cyclists = cyclistService.getAllCyclists(pageable);
        return ResponseEntity.ok(cyclists);
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping (path = "/cyclists/{id}")
    public ResponseEntity<CyclistDTO> getById(@PathVariable final String id){
        final CyclistDTO cyclistFound = cyclistService.getById(id);
        return ResponseEntity.ok(cyclistFound);
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/cyclists", params = {"name"})
    public ResponseEntity<List<CyclistDTO>> findByName(
            @RequestParam(name = "name") final String name
    ) {
        final List<CyclistDTO> studentsFound = cyclistService.findByName(name);
        return ResponseEntity.ok(studentsFound);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/cyclists/{id}")
    public ResponseEntity<Void> deleteCyclistById (@PathVariable final String id){
        cyclistService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

//    @RolesAllowed({"Administrator", "Cyclist"})
    @PutMapping(path = "/cyclists/{id}")
    public ResponseEntity<CyclistDTO> updateCyclist(
            @PathVariable final String id,
            @RequestBody final CyclistDTO updatedCyclist
    ){
        final CyclistDTO cyclistDto = cyclistService.updated(id, updatedCyclist);
        return ResponseEntity.ok(cyclistDto);
    }

    @RolesAllowed({"Administrator", "Cyclist"})
    @PatchMapping(path = "/cyclists/{id}")
    public ResponseEntity<CyclistDTO> patch(
            @PathVariable final String id,
            @RequestBody final CyclistDTO cyclistDto
    ){
        final CyclistDTO patched = cyclistService.patch(id, cyclistDto);
        return ResponseEntity.ok(patched);
    }

    @RolesAllowed({"Administrator", "Cyclist"})
    @PostMapping(path = "/cyclists/{cyclistId}/categories/{categoryId}")
    public ResponseEntity<CyclistDTO> addCategoryToCyclist(
            @PathVariable final String cyclistId,
            @PathVariable final String categoryId
    ){
        cyclistService.addCategoryToCyclist (cyclistId, categoryId);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"Administrator", "Cyclist"})
    @DeleteMapping(path = "cyclists/{cyclistId}/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategoryFromCyclist (
            @PathVariable final String cyclistId,
            @PathVariable final String categoryId
    ){
        cyclistService.deleteCategoryFromCyclist(cyclistId, categoryId);
        return ResponseEntity.noContent().build();
    }
}