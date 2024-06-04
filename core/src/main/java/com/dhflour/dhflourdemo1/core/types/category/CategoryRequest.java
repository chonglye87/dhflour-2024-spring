//package com.dhflour.dhflourdemo1.core.types.category;
//
//import com.dhflour.dhflourdemo1.jpa.domain.category.CategoryEntity;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//// 등록용은 DTO 를 필수적으로 만들어야함.
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class CategoryRequest {
//
//    @Schema(description = "게시물 이름", required = true, example = "Spring Boot and Swagger Integration")
//    private String name;
//
//    public CategoryEntity toEntity() {
//        CategoryEntity newCategory = new CategoryEntity();
//        newCategory.setName(this.name);
//        return newCategory;
//    }
//}
