package com.recipeshare.project.repositories;

import com.recipeshare.project.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,  Integer> {
}
