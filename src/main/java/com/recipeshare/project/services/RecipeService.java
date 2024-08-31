package com.recipeshare.project.services;

import com.recipeshare.project.dto.RecipeDto;
import com.recipeshare.project.models.Ingredient;
import com.recipeshare.project.models.Recipe;
import com.recipeshare.project.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private IngredientService ingredientService;

    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
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
        RecipeDto recipeDto = RecipeDto.builder().
                id(recipe.getId()).
                title(recipe.getTitle()).
                content(recipe.getContent()).
                photoUrl(recipe.getPhotoUrl()).
                build();
        recipeDto.setIngredients(recipe.getIngredients().stream().map(Ingredient::getDescription).collect(Collectors.toList()));
        return recipeDto;
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

        recipe.clearIngredients();
        ingredientService.deleteIngredientsByRecipeId(recipe.getId());
        for(String ingredientDescription : recipeDto.getIngredients()) {
            Ingredient ingredient = Ingredient.builder().
                    description(ingredientDescription).
                    recipe(recipe).
                    build();
            ingredientService.saveIngredient(ingredient);
        }
        return recipe;
    }

    public Recipe saveRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.save(mapToRecipe(recipeDto));
        for(String ingredientDescription : recipeDto.getIngredients()) {
            Ingredient newIngredient = Ingredient.builder().
                    description(ingredientDescription).
                    recipe(recipe).
                    build();
            ingredientService.saveIngredient(newIngredient);
        }
        return recipe;
    }

    public void updateRecipe(RecipeDto recipeDto) {
        recipeRepository.save(mapToExistingRecipe(recipeDto));
    }

    public void delete(Integer recipeId) {
        recipeRepository.deleteById(recipeId);
    }
}
