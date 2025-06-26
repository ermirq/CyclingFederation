package com.zerogravitysolutions.core.categories;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService raceCategoryService;

    public CategoryController(final CategoryService raceCategoryService) {
        this.raceCategoryService = raceCategoryService;
    }

    @RolesAllowed("Administrator")
    @PostMapping(path = "/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody final CategoryDTO dto) {
        final CategoryDTO newCategory = raceCategoryService.save(dto);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @RolesAllowed({"Administrator", "Member", "Cyclist"})
    @GetMapping(path = "/categories/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable final String id) {
        final CategoryDTO categoryDTO = raceCategoryService.getById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @RolesAllowed({"Administrator", "Member", "Cyclist"})
    @GetMapping(path = "/categories")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        final List<CategoryDTO> categoryDTO = raceCategoryService.getAllCategories();
        return ResponseEntity.ok(categoryDTO);
    }

    @RolesAllowed("Administrator")
    @DeleteMapping(path = "/categories/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable final String id) {
        raceCategoryService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("Administrator")
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable final String id,
            @RequestBody final CategoryDTO updatedCategory
    ) {
        final CategoryDTO categoryDTO = raceCategoryService.updated(id, updatedCategory);
        return ResponseEntity.ok(categoryDTO);
    }

    @RolesAllowed("Administrator")
    @PatchMapping("/categories/{id}")
    public ResponseEntity<CategoryDTO> patch(
            @PathVariable final String id,
            @RequestBody final CategoryDTO categoryDTO
    ) {
        final CategoryDTO patched = raceCategoryService.patch(id, categoryDTO);
        return ResponseEntity.ok(patched);
    }
}
