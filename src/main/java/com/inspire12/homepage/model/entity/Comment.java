package com.inspire12.homepage.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inspire12.homepage.message.CommentMsg;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {
    @Id
    @Column(name = "id")
    int id;
    @Column(name = "article_id")
    @JsonProperty("article_id")
    int articleId;

    @JsonProperty("username")
    String username;
    @Column(name = "no")
    int grpno;
    @Column(name = "grpord")
    int grpord;
    @Column(name = "depth")
    int depth;

    @Column(name = "content")
    @JsonProperty("content")
    String content;
    int like = 0;
    @Column(name = "created_at")
    @CreationTimestamp
    @JsonProperty("created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @JsonProperty("updated_at")
    LocalDateTime updatedAt;


    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private User user = new User();

    public static Comment create(String username, int articleId, String content) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUsername(username);
        comment.setContent(content);
        return comment;
    }
}
