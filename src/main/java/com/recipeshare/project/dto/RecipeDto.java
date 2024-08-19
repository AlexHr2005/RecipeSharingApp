package com.recipeshare.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Integer id;
    @NotEmpty(message = "Recipe title should not be empty")
    private String title;
    @NotEmpty(message = "Photo URL should not be empty")
    private String photoUrl;
    @NotEmpty(message = "Cooking method should not be empty")
    private String content;
    //private List<Ingredient> ingredients;
}
