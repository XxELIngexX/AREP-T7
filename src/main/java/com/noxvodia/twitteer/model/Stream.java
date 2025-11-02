// package com.noxvodia.twitteer.model;

// import jakarta.persistence.*;
// import lombok.*;

// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Table(name = "streams", indexes = {
//         @Index(name = "idx_stream_name", columnList = "name")
// })
// @Data
// @EqualsAndHashCode(callSuper = true, exclude = { "posts" })
// @ToString(callSuper = true, exclude = { "posts" })
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Stream extends BaseEntity {

//     @Column(name = "name", unique = true, nullable = false, length = 100)
//     private String name;

//     @Column(name = "description", length = 500)
//     private String description;

//     @Column(name = "is_public")
//     @Builder.Default
//     private Boolean isPublic = true;

//     @Column(name = "posts_count")
//     @Builder.Default
//     private Integer postsCount = 0;

//     @OneToMany(mappedBy = "stream", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//     @Builder.Default
//     private List<Post> posts = new ArrayList<>();
//     @OneToOne
//     @JoinColumn(name = "user_id", nullable = false, unique = true)
//     private User user;

// }