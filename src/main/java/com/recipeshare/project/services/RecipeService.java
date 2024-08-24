package com.recipeshare.project.services;

import com.recipeshare.project.dto.RecipeDto;
import com.recipeshare.project.models.Recipe;
import com.recipeshare.project.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeDto> findAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map((recipe) -> mapToDto(recipe)).collect(Collectors.toList());
    }

    public RecipeDto findRecipeById(Integer id) {
        Recipe recipe = recipeRepository.findById(id).get();
        return mapToDto(recipe);
    }

    private RecipeDto mapToDto(Recipe recipe) {
        return RecipeDto.builder().
                id(recipe.getId()).
                title(recipe.getTitle()).
                content(recipe.getContent()).
                photoUrl(recipe.getPhotoUrl()).
                build();
    }

    private Recipe mapToRecipe(RecipeDto recipeDto) {
        return Recipe.builder().
                id(recipeDto.getId()).
                title(recipeDto.getTitle()).
                content(recipeDto.getContent()).
                photoUrl(recipeDto.getPhotoUrl()).
                build();
    }

    private Recipe mapToExistingRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(recipeDto.getId()).get();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setPhotoUrl(recipeDto.getPhotoUrl());
        recipe.setContent(recipeDto.getContent());
        return recipe;
    }

    public Recipe saveRecipe(RecipeDto recipeDto) {
        return recipeRepository.save(mapToRecipe(recipeDto));
    }

    public void updateRecipe(RecipeDto recipeDto) {
        recipeRepository.save(mapToExistingRecipe(recipeDto));
    }

    public void delete(Integer recipeId) {
        recipeRepository.deleteById(recipeId);
    }
}
