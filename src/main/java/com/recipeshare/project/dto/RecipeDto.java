package com.recipeshare.project.dto;

import com.recipeshare.project.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Integer id;
    @NotEmpty(message = "Recipe title should not be empty")
    private String title;
    private MultipartFile imageFile;
    private String imageName;
    @NotEmpty(message = "Cooking method should not be empty")
    private String content;
    private UserEntity createdBy;
    private List<String> ingredients = new ArrayList<>(0);
}
