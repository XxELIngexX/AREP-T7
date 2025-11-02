package com.noxvodia.twitteer.model;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts", indexes = {
    @Index(name = "idx_user_created", columnList = "user_id, created_at"),
    @Index(name = "idx_stream_created", columnList = "stream_id, created_at"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"user", "stream"})
@ToString(callSuper = true, exclude = {"user", "stream"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    
    @Column(name = "content", nullable = false, length = 140)
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_user"))
    private User user;
    
    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "stream_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_stream"))
    // private Stream stream;
    
    @Column(name = "likes_count")
    @Builder.Default
    private Integer likesCount = 0;
    
    @Column(name = "retweets_count")
    @Builder.Default
    private Integer retweetsCount = 0;
    
    @Column(name = "replies_count")
    @Builder.Default
    private Integer repliesCount = 0;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;
    
    @Column(name = "is_edited")
    @Builder.Default
    private Boolean isEdited = false;
}