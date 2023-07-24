package com.spring.blog.dto;

import com.spring.blog.entity.Reply;
import lombok.*;

@AllArgsConstructor @Getter @Setter
@Builder @ToString @NoArgsConstructor
public class ReplyUpdateDTO {

    private long replyId;
    private String replyWriter;
    private String replyContent;
//    private LocalDateTime updatedAt;

    public ReplyUpdateDTO(Reply reply){
        this.replyId = reply.getReplyId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
    }
}
