package com.recipeshare.project.services;

import com.recipeshare.project.dto.RecipeDto;
import com.recipeshare.project.models.Ingredient;
import com.recipeshare.project.models.Recipe;
import com.recipeshare.project.models.UserEntity;
import com.recipeshare.project.repositories.RecipeRepository;
import com.recipeshare.project.security.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private IngredientService ingredientService;
    private UserService userService;

    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.userService = userService;
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
                createdBy(recipe.getCreatedBy()).
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
                createdBy(recipeDto.getCreatedBy()).
                build();
    }

    private Recipe mapToExistingRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(recipeDto.getId()).get();
        recipe.setTitle(recipeDto.getTitle());
        recipe.setPhotoUrl(recipeDto.getPhotoUrl());
        recipe.setContent(recipeDto.getContent());
        recipe.setCreatedBy(recipeDto.getCreatedBy());

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
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        Recipe recipe = mapToRecipe(recipeDto);
        recipe.setCreatedBy(user);
        recipe = recipeRepository.save(recipe);
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
        Recipe recipe = mapToExistingRecipe(recipeDto);
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        recipe.setCreatedBy(user);
        recipeRepository.save(recipe);
    }

    public void delete(Integer recipeId) {
        recipeRepository.deleteById(recipeId);
    }
}
