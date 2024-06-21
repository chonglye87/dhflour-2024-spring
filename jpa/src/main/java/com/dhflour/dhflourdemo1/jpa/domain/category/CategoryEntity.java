package com.dhflour.dhflourdemo1.jpa.domain.category;

import com.dhflour.dhflourdemo1.jpa.domain.AbstractEntity;
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
public class CategoryEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "1", readOnly = true, requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "이름", example = "", readOnly = true)
    private String name;


    @ManyToMany(mappedBy = "categories")
    @Schema(hidden = true)
    @JsonIgnore
    private Set<BoardEntity> boards;

    @Override
    public void delete() {

    }

    @Override
    public void lazy() {

    }
}
