package com.zerogravitysolutions.core.races;

import com.zerogravitysolutions.core.checkpoints.CheckpointDTO;
import com.zerogravitysolutions.core.checkpoints.CheckpointDocument;
import com.zerogravitysolutions.core.volunteers.VolunteersDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RaceService {
    RaceDTO save(RaceDTO raceDto);

    RaceDTO getById(String id);

    Page<RaceDTO> getAllRaces(Pageable page);

    void softDelete(String id);

    RaceDTO updated(String id, RaceDTO updatedRace);

    RaceDTO patch(String id, RaceDTO raceDTO);

    void deleteCategoryFromRace(String raceId, String categoryId);

    void deleteDisciplineFromRace(String raceId, String disciplineId);

    void addDisciplineToRace(String raceId, String disciplineId);

    void addCategoriesToRace(String raceId, String categoryId);

    void addParticipantsToRaces(String raceId, String cyclistId, String categoryId);

    void deleteParticipantsFromRace(String raceId, String cyclistId);

    void addVolunteersToRaces(String raceId, String volunteerId);

    void deleteVolunteersFromRace(String raceId, String volunteerId);

    RaceDTO addCheckPointToRace(String raceId, CheckpointDTO checkpointDTO);

    RaceDTO updateCheckpointInRace(
            String raceId,
            String checkpointId,
            CheckpointDocument updatedCheckpoint
    );

    void deleteCheckpointFromRace(String raceId, String checkpointId);

    void addRaceNumbers(String raceId, String categoryId, int minNumber, int maxNumber);

    void reorderParticipantPositionWithShift(
            final String raceId,
            final String cyclistId,
            final String categoryId,
            final int newPosition);

    VolunteersDTO createVolunteersFromRace(String raceId, VolunteersDTO volunteersDTO);

    void addSponsorsToRace(String raceId, RaceDTO.SponsorDTO sponsorDTO, MultipartFile logo);

    List<RaceDTO.SponsorDTO> findSponsors(String raceId);

    void addLogisticsToRace(String raceId, RaceDTO.LogisticsDTO logisticsDTO);

    List<RaceDTO.LogisticsDTO> findLogistics(String raceId);

    RaceDTO.SponsorDTO updateSponsor(
            String raceId, String sponsorId, RaceDTO.SponsorDTO sponsorDTO);

    RaceDTO.LogisticsDTO updateLogistics(
            String raceId, String logisticsId, RaceDTO.LogisticsDTO logisticsDTO);

    void addLogoToSponsor(String raceId, String sponsorId, MultipartFile logo);

    ByteArrayResource readLogo(String raceId, String sponsorId);
}
