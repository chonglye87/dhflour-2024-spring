package com.dhflour.dhflourdemo1.core.domain.board;

import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Board")
@Schema(description = "게시판 엔티티, 게시물 정보를 포함")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "1", readOnly = true)
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "게시물 제목", example = "")
    private String title;

    @Column(nullable = false, length = 4000)
    @Schema(description = "게시물 내용", example = "")
    private String content;

    @Column(nullable = false, updatable = false)
    @Schema(description = "생성 시간", example = "2023-10-01T12:00:00", readOnly = true)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Schema(description = "마지막 업데이트 시간", example = "2023-10-02T12:00:00", readOnly = true)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "board_category",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Schema(description = "연결된 카테고리 목록")
    private Set<CategoryEntity> categories;

    // 기본 생성자
    public BoardEntity() {
    }

    // 필드를 포함한 생성자
    public BoardEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter 및 Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryEntity> categories) {
        this.categories = categories;
    }

    // ToString 메소드
    @Override
    public String toString() {
        return "BoardEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
