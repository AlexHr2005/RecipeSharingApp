package com.recipeshare.project.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RecipeDto {
    private Integer id;
    private String title;
    private String photoUrl;
    private String content;
    //private List<Ingredient> ingredients;
    private LocalDateTime timeCreated;
    private LocalDateTime timeUpdated;
}
