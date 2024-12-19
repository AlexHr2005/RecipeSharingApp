package com.recipeshare.project.services;

import com.recipeshare.project.dto.RecipeDto;
import com.recipeshare.project.models.Ingredient;
import com.recipeshare.project.models.Recipe;
import com.recipeshare.project.models.UserEntity;
import com.recipeshare.project.repositories.RecipeRepository;
import com.recipeshare.project.security.SecurityUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private RecipeRepository recipeRepository;
    private IngredientService ingredientService;
    private UserService userService;
    private String uploadDir;

    public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.ingredientService = ingredientService;
        this.userService = userService;
        this.uploadDir = System.getenv("RECIPES_IMAGES");
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
                createdBy(recipe.getCreatedBy()).
                imageName(Path.of(recipe.getImagePath()).getFileName().toString()).
                build();
        recipeDto.setIngredients(recipe.getIngredients().stream().map(Ingredient::getDescription).collect(Collectors.toList()));
        return recipeDto;
    }

    private Recipe mapToRecipe(RecipeDto recipeDto) {
        return Recipe.builder().
                id(recipeDto.getId()).
                title(recipeDto.getTitle()).
                content(recipeDto.getContent()).
                createdBy(recipeDto.getCreatedBy()).
                build();
    }

    private Recipe mapToExistingRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(recipeDto.getId()).get();
        recipe.setTitle(recipeDto.getTitle());
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

    public Recipe saveRecipe(RecipeDto recipeDto) throws IOException {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        Recipe recipe = mapToRecipe(recipeDto);
        recipe.setCreatedBy(user);
        recipe = recipeRepository.save(recipe);

        MultipartFile image = recipeDto.getImageFile();
        Path imagePath = saveImage(image, recipe);

        recipe.setImagePath(imagePath.toString());

        for(String ingredientDescription : recipeDto.getIngredients()) {
            Ingredient newIngredient = Ingredient.builder().
                    description(ingredientDescription).
                    recipe(recipe).
                    build();
            ingredientService.saveIngredient(newIngredient);
        }

        recipeRepository.save(recipe);
        return recipe;
    }

    private void deleteImage(Path path) throws IOException {
        Files.delete(path);
        Files.delete(path.getParent());
    }

    private Path saveImage(MultipartFile image, Recipe recipe) throws IOException {
        Path path = Paths.get(uploadDir);
        Path recipePath = path.resolve("" + recipe.getId());
        recipePath.toFile().mkdir();

        Path filePath = recipePath.resolve(image.getOriginalFilename());
        Files.write(filePath, image.getBytes());

        return filePath;
    }

    public void updateRecipe(RecipeDto recipeDto) throws IOException {
        Recipe recipe = mapToExistingRecipe(recipeDto);

        deleteImage(Path.of(recipe.getImagePath()));
        MultipartFile image = recipeDto.getImageFile();
        Path imagePath = saveImage(image, recipe);

        recipe.setImagePath(imagePath.toString());

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        recipe.setCreatedBy(user);
        recipeRepository.save(recipe);
    }

    public void delete(Integer recipeId) throws IOException {
        Recipe recipeToDelete = recipeRepository.findById(recipeId).get();
        deleteImage(Path.of(recipeToDelete.getImagePath()));
        recipeRepository.deleteById(recipeId);
    }
}
