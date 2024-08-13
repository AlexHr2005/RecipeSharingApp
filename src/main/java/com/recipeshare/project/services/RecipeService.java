package com.recipeshare.project.services;

import com.recipeshare.project.dto.RecipeDto;
import com.recipeshare.project.models.Recipe;
import com.recipeshare.project.repositories.RecipeRepository;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeService {
    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeDto> findAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map((recipe) -> mapToDto(recipe)).collect(Collectors.toList());
    }

    private RecipeDto mapToDto(Recipe recipe) {
        return RecipeDto.builder().
                id(recipe.getId()).
                title(recipe.getTitle()).
                content(recipe.getContent()).
                photoUrl(recipe.getPhotoUrl()).
                timeCreated(recipe.getTimeCreated()).
                timeUpdated(recipe.getTimeUpdated()).
                build();
    }
}
