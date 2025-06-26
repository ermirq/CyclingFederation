package com.zerogravitysolutions.core.races;

import com.zerogravitysolutions.core.checkpoints.CheckpointDTO;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import com.zerogravitysolutions.core.volunteers.VolunteersDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RaceController {

    private final RaceService raceService;

    public RaceController(final RaceService raceService) {
        this.raceService = raceService;
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races")
    public ResponseEntity<RaceDTO> createRace(@Valid @RequestBody final RaceDTO raceDto) {
        final RaceDTO newRace = raceService.save(raceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRace);
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/races/{id}")
    public ResponseEntity<RaceDTO> getRaceById(@PathVariable final String id) {
        final RaceDTO raceFound = raceService.getById(id);
        return ResponseEntity.ok(raceFound);
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/races/page")
    public ResponseEntity<Page<RaceDTO>> findAll(final Pageable page) {
        final Page<RaceDTO> races = raceService.getAllRaces(page);
        return ResponseEntity.ok(races);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/races/{id}")
    public ResponseEntity<Void> deleteRacesById(@PathVariable final String id) {
        raceService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @PutMapping(path = "/races/{id}")
    public ResponseEntity<RaceDTO> updateRace(
            @PathVariable final String id,
            @RequestBody final RaceDTO updatedRace
    ) {
        final RaceDTO raceDTO = raceService.updated(id, updatedRace);
        return ResponseEntity.ok(raceDTO);
    }

    @RolesAllowed("Administrator")
    @PatchMapping(path = "/races/{id}")
    public ResponseEntity<RaceDTO> patch(
            @PathVariable final String id,
            @RequestBody final RaceDTO raceDTO
    ) {
        final RaceDTO patched = raceService.patch(id, raceDTO);
        return ResponseEntity.ok(patched);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/races/{raceId}/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategoryFromRace(
            @PathVariable final String raceId,
            @PathVariable final String categoryId
    ) {
        raceService.deleteCategoryFromRace(raceId, categoryId);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/races/{raceId}/disciplines/{disciplineId}")
    public ResponseEntity<Void> deleteDisciplineFromRace(
            @PathVariable final String raceId,
            @PathVariable final String disciplineId
    ) {
        raceService.deleteDisciplineFromRace(raceId, disciplineId);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races/{raceId}/disciplines/{disciplineId}")
    public ResponseEntity<RaceDTO> addDisciplineToRace(
            @PathVariable final String raceId,
            @PathVariable final String disciplineId
    ) {
        raceService.addDisciplineToRace(raceId, disciplineId);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races/{raceId}/categories/{categoryId}")
    public ResponseEntity<RaceDTO> addCategoriesToRace(
            @PathVariable final String raceId,
            @PathVariable final String categoryId
    ) {
        raceService.addCategoriesToRace(raceId, categoryId);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"Administrator", "Cyclist"})
    @PostMapping(path = "/races/{raceId}/participants/{cyclistId}")
    public ResponseEntity<RaceDTO> addParticipantsToRaces(
            @PathVariable final String raceId,
            @PathVariable final String cyclistId,
            @RequestParam final String categoryId
    ){
        raceService.addParticipantsToRaces(raceId, cyclistId,categoryId);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/races/{raceId}/participants/{cyclistId}")
    public ResponseEntity<Void> deleteParticipantsFromRaces(
            @PathVariable final String raceId,
            @PathVariable final String cyclistId
    ) {
        raceService.deleteParticipantsFromRace(raceId, cyclistId);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races/{raceId}/volunteers/{volunteerId}")
    public ResponseEntity<RaceDTO> addVolunteersToRaces (
            @PathVariable final String raceId,
            @PathVariable final String volunteerId
    ){
        raceService.addVolunteersToRaces(raceId, volunteerId);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/races/{raceId}/volunteers/{volunteerId}")
    public ResponseEntity<Void> deleteVolunteersFromRaces(
            @PathVariable final String raceId,
            @PathVariable final String volunteerId
    ) {
        raceService.deleteVolunteersFromRace(raceId, volunteerId);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races/{raceId}/categories/{categoryId}/race-numbers")
    public ResponseEntity<RaceDTO> addRaceNumbers(
            @PathVariable final String raceId,
            @PathVariable final String categoryId,
            @RequestParam final int minNumber,
            @RequestParam final int maxNumber) {
        raceService.addRaceNumbers(raceId, categoryId, minNumber, maxNumber);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed("Administrator")
    @PostMapping (path = "/races/{raceId}/participants/{cyclistId}/categories/{categoryId}")
    public ResponseEntity<Void> reorderParticipants(
            @PathVariable final String raceId,
            @PathVariable final String cyclistId,
            @PathVariable final String categoryId,
            @RequestParam final int newPosition) {
        raceService.reorderParticipantPositionWithShift(raceId, cyclistId, categoryId, newPosition);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/races/{raceId}/checkpoints")
    public ResponseEntity<RaceDTO> addCheckPointToRace(
            @PathVariable final String raceId,
            @RequestBody final CheckpointDTO checkpointDTO
    ){
        final RaceDTO updatedRace = raceService.addCheckPointToRace(raceId, checkpointDTO);
        return ResponseEntity.ok(updatedRace);
    }

    @PutMapping(path = "/races/{raceId}/checkpoints/{checkpointId}")
    public ResponseEntity<RaceDTO> updateCheckpoinInRace(
            @PathVariable final String raceId,
            @PathVariable final String checkpointId,
            @RequestBody final CheckpointDocument updatedCheckpoint
    ){
        final RaceDTO updatedRace = raceService.updateCheckpointInRace(
                raceId,
                checkpointId,
                updatedCheckpoint
        );
        return ResponseEntity.ok(updatedRace);
    }

    @DeleteMapping(path = "/races/{raceId}/checkpoints/{checkpointId}")
    public ResponseEntity<Void> deleteCheckpointFromRace(
            @PathVariable final String raceId,
            @PathVariable final String checkpointId
    ) {
        raceService.deleteCheckpointFromRace(raceId, checkpointId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/races/{raceId}/volunteers")
    public ResponseEntity<VolunteersDTO> createVolunteersFromRace(
            @PathVariable final String raceId,
            @RequestBody final VolunteersDTO volunteersDTO
    ){
        final VolunteersDTO newVolunteer =
                raceService.createVolunteersFromRace(raceId, volunteersDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVolunteer);
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races/{raceId}/sponsors")
    public ResponseEntity<RaceDTO.SponsorDTO> addSponsorsToRace(
            @PathVariable final String raceId,
            @ModelAttribute final RaceDTO.SponsorDTO sponsorDTO,
            @RequestParam("file") final MultipartFile logo
    ) {
        raceService.addSponsorsToRace(raceId, sponsorDTO, logo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/races/{raceId}/sponsors")
    public ResponseEntity<List<RaceDTO.SponsorDTO>> findSponsorsOfRace (
            @PathVariable final String raceId
    ){
        final List<RaceDTO.SponsorDTO> sponsorDTO = raceService.findSponsors(raceId);
        return ResponseEntity.ok(sponsorDTO);
    }

    @RolesAllowed("Administrator")
    @PutMapping(path = "/races/{raceId}/sponsors/{sponsorId}")
    public ResponseEntity<RaceDTO.SponsorDTO> updateSponsor (
            @PathVariable final String raceId,
            @PathVariable final String sponsorId,
            @RequestBody final RaceDTO.SponsorDTO sponsorDTO
    ){
        final RaceDTO.SponsorDTO sponsor = raceService.updateSponsor(raceId, sponsorId, sponsorDTO);
        return ResponseEntity.ok(sponsor);
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races/{raceId}/logistics")
    public ResponseEntity<Void> addLogisticsToRace (
            @PathVariable final String raceId,
            @RequestBody final RaceDTO.LogisticsDTO logisticsDTO
    ){
        raceService.addLogisticsToRace(raceId, logisticsDTO);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/races/{raceId}/logistics")
    public ResponseEntity<List<RaceDTO.LogisticsDTO>> findLogisticsOfRace (
            @PathVariable final String raceId
    ){
        final List<RaceDTO.LogisticsDTO> logisticsDTOS = raceService.findLogistics(raceId);
        return ResponseEntity.ok(logisticsDTOS);
    }

    @RolesAllowed("Administrator")
    @PutMapping(path = "/races/{raceId}/logistics/{logisticsId}")
    public ResponseEntity<RaceDTO.LogisticsDTO> updateLogistics (
            @PathVariable final String raceId,
            @PathVariable final String logisticsId,
            @RequestBody final RaceDTO.LogisticsDTO logisticsDTO
    ){
        final RaceDTO.LogisticsDTO logistics =
                raceService.updateLogistics(raceId, logisticsId, logisticsDTO);
        return ResponseEntity.ok(logistics);
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/races/{raceId}/sponsors/{sponsorId}/logo")
    public ResponseEntity<Void> uploadLogo (
            @PathVariable final String raceId,
            @PathVariable final String sponsorId,
            @RequestParam("file") final MultipartFile logo
    ){
        raceService.addLogoToSponsor(raceId, sponsorId, logo);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/races/{raceId}/sponsors/{sponsorId}/logo")
    public ResponseEntity<ByteArrayResource> getSponsorLogo (
            @PathVariable final String raceId,
            @PathVariable final String sponsorId
    ) {
        final ByteArrayResource sponsorLogo = raceService.readLogo(raceId, sponsorId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + "sponsor- " + sponsorId + " -image.jpeg")
                .body(sponsorLogo);

    }
}
