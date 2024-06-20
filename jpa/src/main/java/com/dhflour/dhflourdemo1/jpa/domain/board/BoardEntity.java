package com.dhflour.dhflourdemo1.jpa.domain.board;

import com.dhflour.dhflourdemo1.jpa.domain.AbstractEntity;
import com.dhflour.dhflourdemo1.jpa.domain.category.CategoryEntity;
// import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "Board")
public class BoardEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Schema(description = "id", example = "1", readOnly = true, requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Column(nullable = false, length = 100)
//    @Schema(description = "게시물 제목", example = "", readOnly = true)
    private String title;

    @Column(nullable = false, length = 4000)
//    @Schema(description = "게시물 내용", example = "", readOnly = true)
    private String content;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "board_category",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
//    @Schema(description = "연결된 카테고리 목록", readOnly = true)
    private Set<CategoryEntity> categories;

    // 필드를 포함한 생성자
    public BoardEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void delete() {
        
    }

    @Override
    public void lazy() {

    }
}
