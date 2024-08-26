package com.recipeshare.project.services;

import com.recipeshare.project.models.Ingredient;
import com.recipeshare.project.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
    private IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }
}
