package com.spring.blog.entity;

import lombok.*;

@Getter @Setter @ToString @AllArgsConstructor
@NoArgsConstructor @Builder
public class ChatMessage {

    private String sender;
    private String content;

}
