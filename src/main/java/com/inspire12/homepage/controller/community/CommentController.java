package com.inspire12.homepage.controller.community;

import com.inspire12.homepage.model.entity.Comment;
import com.inspire12.homepage.service.board.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    @PostMapping("/comments")
    public ResponseEntity saveComments(@RequestBody Comment comment) {

        commentService.saveComment(comment);
        return ResponseEntity.ok().build();
    }

}

