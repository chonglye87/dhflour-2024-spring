package com.dhflour.dhflourdemo1.jpa.domain.category;

import com.dhflour.dhflourdemo1.jpa.domain.board.BoardEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Category")
@Schema(description = "게시판 카테고리 엔티티")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "1", readOnly = true, requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "이름", example = "", readOnly = true)
    private String name;

    @Column(nullable = false, updatable = false)
    @Schema(description = "생성 시간", example = "2023-10-01T12:00:00", readOnly = true)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Schema(description = "마지막 업데이트 시간", example = "2023-10-02T12:00:00", readOnly = true)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany(mappedBy = "categories")
    @Schema(hidden = true)
    @JsonIgnore
    private Set<BoardEntity> boards;

}
