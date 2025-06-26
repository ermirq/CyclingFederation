package com.zerogravitysolutions.core.volunteers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VolunteersController {
    private final VolunteersService volunteersService;

    public VolunteersController(final VolunteersService volunteersService) {
        this.volunteersService = volunteersService;
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/volunteers")
    public ResponseEntity<VolunteersDTO> createVolunteer (
            @Valid @RequestBody final VolunteersDTO volunteersDTO
    ) {
        final VolunteersDTO newVolunteer = volunteersService.save(volunteersDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVolunteer);
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping(path = "/volunteers")
    public ResponseEntity<List<VolunteersDTO>> findAll(){
        final List<VolunteersDTO> volunteersDTO = volunteersService.findAll();
        return ResponseEntity.ok(volunteersDTO);
    }

    @RolesAllowed({"Administrator", "Cyclist", "Member"})
    @GetMapping (path = "/volunteers/{id}")
    public ResponseEntity<VolunteersDTO> getById(@PathVariable final String id){
        final VolunteersDTO volunteersDTO = volunteersService.getById(id);
        return ResponseEntity.ok(volunteersDTO);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/volunteers/{id}")
    public ResponseEntity<Void> deleteVolunteerById (@PathVariable final String id){
        volunteersService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @PutMapping(path = "/volunteers/{id}")
    public ResponseEntity<VolunteersDTO> updateVolunteer(
            @PathVariable final String id,
            @RequestBody final VolunteersDTO updatedVolunteer
    ) {
        final VolunteersDTO volunteersDto = volunteersService.update(id, updatedVolunteer);
        return ResponseEntity.ok(volunteersDto);
    }

    @RolesAllowed("Administrator")
    @PatchMapping(path = "/volunteers/{id}")
    public ResponseEntity<VolunteersDTO> patch(
            @PathVariable final String id,
            @RequestBody final VolunteersDTO volunteersDTO
    ) {
        final VolunteersDTO patched = volunteersService.patch(id, volunteersDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(patched);
    }

    @GetMapping(path = "/volunteers/search")
    public ResponseEntity<List<VolunteersDTO>> findVolunteerByName(
            @RequestParam(name = "name", required = false) final String name
    ) {
        final List<VolunteersDTO> volunteers = volunteersService.findByName(name);

        return ResponseEntity.ok(volunteers);
    }
}
