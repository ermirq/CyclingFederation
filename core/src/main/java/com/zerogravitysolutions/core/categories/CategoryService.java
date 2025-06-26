package com.zerogravitysolutions.core.categories;

import java.util.List;

public interface CategoryService {
    CategoryDTO save(CategoryDTO dto);

    CategoryDTO getById(String id);

    List<CategoryDTO> getAllCategories();

    void softDelete(String id);

    CategoryDTO updated(String id, CategoryDTO updatedCategory);

    CategoryDTO patch(String id, CategoryDTO categoryDTO);
}
