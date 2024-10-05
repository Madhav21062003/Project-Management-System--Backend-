package com.madhav.controller;

import com.madhav.entities.Comment;
import com.madhav.entities.User;
import com.madhav.request.CreateCommentRequest;
import com.madhav.response.MessageResponse;
import com.madhav.service.interfaces.CommentService;
import com.madhav.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody CreateCommentRequest request,
            @RequestHeader("Authorization")String jwt
            )throws Exception{

        User user = userService.findUserProfileByJwt(jwt);
        Comment createdComment = commentService.createComment(request.getIssueId(), user.getId(), request.getContent());

        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization")String jwt)throws Exception{

        User user = userService.findUserProfileByJwt(jwt);

        commentService.deleteComment(commentId, user.getId());

        MessageResponse response = new MessageResponse();
        response.setMessage("comment deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<List<Comment>> getCommentsByIssueId(@PathVariable Long issueId){
        List<Comment> comments = commentService.findCommentByIssueId(issueId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
