package com.zerogravitysolutions.core.cyclists;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CyclistService {

    CyclistDTO save(CyclistDTO cyclistDto);

    CyclistDTO getById(String id);

    void softDelete(String id);

    CyclistDTO updated(String id, CyclistDTO updatedCyclist);

    CyclistDTO patch(String id, CyclistDTO cyclistDto);

    Page<CyclistDTO> getAllCyclists(Pageable pageable);

    List<CyclistDTO> findByName(String name);

    void addCategoryToCyclist(String cyclistId, String categoryId);

    void deleteCategoryFromCyclist(String cyclistId, String categoryId);
}