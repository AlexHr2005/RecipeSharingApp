package com.recipeshare.project.repositories;

import com.recipeshare.project.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ingredient WHERE recipe_id=?1", nativeQuery = true)
    void deleteByRecipeId(Integer recipeId);
}
