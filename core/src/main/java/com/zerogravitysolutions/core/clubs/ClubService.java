package com.zerogravitysolutions.core.clubs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ClubService {
    ClubDTO saveClub(ClubDTO club);

    List<ClubDTO> fetchAllClubs();

    ClubDTO getById(String id);

    ClubDTO update(String id, ClubDTO clubDto);

    void softDelete(String id);

    Page<ClubDTO> getAll(Pageable pageable);

    Set<ClubDTO> findByName(String name);

    void addCyclistToRace(String clubId, String cyclistId);

    void deleteCyclistFromRace(String clubId, String cyclistId);
}
