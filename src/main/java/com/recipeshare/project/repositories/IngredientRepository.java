package com.recipeshare.project.repositories;

import com.recipeshare.project.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {}
