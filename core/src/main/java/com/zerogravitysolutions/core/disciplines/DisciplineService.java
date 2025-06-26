package com.zerogravitysolutions.core.disciplines;

import java.util.List;

public interface DisciplineService {
    DisciplineDTO save(DisciplineDTO raceDisciplineDTO);

    DisciplineDTO getById(String id);

    void softDelete(String id);

    DisciplineDTO updated(String id, DisciplineDTO updatedDiscipline);

    DisciplineDTO patch(String id, DisciplineDTO raceDisciplineDTO);

    List<DisciplineDTO> getAllDisciplines();
}
