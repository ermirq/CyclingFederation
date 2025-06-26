package com.zerogravitysolutions.core.volunteers;

import java.util.List;

public interface VolunteersService {
    VolunteersDTO save(VolunteersDTO volunteersDTO);

    List<VolunteersDTO> findAll();

    VolunteersDTO getById(String id);

    void softDelete(String id);

    VolunteersDTO update(String id, VolunteersDTO updatedVolunteer);

    VolunteersDTO patch(String id, VolunteersDTO volunteersDTO);

    List<VolunteersDTO> findByName(String name);
}
