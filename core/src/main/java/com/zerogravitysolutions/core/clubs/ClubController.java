package com.zerogravitysolutions.core.clubs;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Validated
public class ClubController {

    private final ClubService clubService;

    public ClubController(final ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping("/clubs")
    public ResponseEntity<ClubDTO> createClub(@Valid @RequestBody final ClubDTO club) {
        final ClubDTO newClub = clubService.saveClub(club);
        return new ResponseEntity<>(newClub, HttpStatus.CREATED);
    }

    @GetMapping("/clubs")
    public ResponseEntity<List<ClubDTO>> getAllClubs() {

        final List<ClubDTO> clubDTOS = clubService.fetchAllClubs();
        return new ResponseEntity<>(clubDTOS, HttpStatus.OK);
    }

    @GetMapping("/clubs/paged")
    public ResponseEntity<Page<ClubDTO>> getAllClubsPaged(
            @PageableDefault(size = 5, sort = {"id", "name"}) final Pageable pageable
    ) {

        final Page<ClubDTO> clubDTOS = clubService.getAll(pageable);
        return ResponseEntity.ok(clubDTOS);
    }


    @GetMapping("/clubs/{id}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable final String id) {

        final ClubDTO foundClub = clubService.getById(id);
        return ResponseEntity.ok(foundClub);
    }

    @GetMapping(path = "/clubs",params = "name")
    public ResponseEntity<Set<ClubDTO>> findByClubName(@RequestParam final String name) {
        final Set<ClubDTO> clubsFound = clubService.findByName(name);
        return ResponseEntity.ok(clubsFound);
    }

    @PutMapping("/clubs/{id}")
    public ResponseEntity<ClubDTO> updateClub(
            @PathVariable final String id,
            @Valid @RequestBody final ClubDTO clubDto
    ) {

        if (id == null || clubDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clubService.update(id, clubDto));
    }

    @DeleteMapping("/clubs/{id}")
    public ResponseEntity<Void> delete(@PathVariable final String id) {
        clubService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/clubs/{clubId}/cyclists/{cyclistId}")
    public ResponseEntity<ClubDTO> addCyclistToRace(
            @PathVariable final String clubId,
            @PathVariable final String cyclistId
    ) {
        clubService.addCyclistToRace(clubId, cyclistId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/clubs/{clubId}/cyclists/{cyclistId}")
    public ResponseEntity<Void> deleteCyclistFromClub(
            @PathVariable final String clubId,
            @PathVariable final String cyclistId
    ){
        clubService.deleteCyclistFromRace(clubId, cyclistId);
        return ResponseEntity.noContent().build();
    }
}
