package com.recipeshare.project.controllers;

import com.recipeshare.project.dto.RecipeDto;
import com.recipeshare.project.models.Recipe;
import com.recipeshare.project.services.RecipeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RecipeController {
    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public String listClubs(Model model) {
        List<RecipeDto> recipes = recipeService.findAllRecipes();
        model.addAttribute("recipes", recipes);
        return "list_of_recipes";
    }

    @GetMapping("/recipes/create")
    public String createRecipeForm(Model model) {
        RecipeDto recipe = new RecipeDto();
        model.addAttribute("recipe", recipe);
        return "recipes_create";
    }

    @PostMapping("/recipes/create")
    public String saveRecipe(@Valid @ModelAttribute("recipe") RecipeDto recipe,
                             BindingResult bindingResult,
                             Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipe);
            return "recipes_create";
        }
        recipeService.saveRecipe(recipe);
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/edit/{recipeId}")
    public String editRecipeForm(@PathVariable("recipeId") Integer id, Model model) {
        RecipeDto recipeDto = recipeService.findRecipeById(id);
        model.addAttribute("recipe", recipeDto);
        return "recipes_edit";
    }

    @PostMapping("/recipes/edit/{recipeId}")
    public String editRecipeForm(@PathVariable("recipeId") Integer id,
                                 @Valid @ModelAttribute("recipe") RecipeDto recipeDto,
                                 BindingResult bindingResult,
                                 Model model) {
        if(bindingResult.hasErrors()) {
            recipeDto.setId(id);
            model.addAttribute("recipe", recipeDto);
            return "recipes_edit";
        }
        recipeDto.setId(id);
        recipeService.updateRecipe(recipeDto);
        return "redirect:/recipes";
    }
}
