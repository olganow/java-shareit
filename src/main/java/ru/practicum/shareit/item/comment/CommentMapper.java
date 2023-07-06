package ru.practicum.shareit.item.comment;

public class CommentMapper {

    public static CommentDto commentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .itemId(comment.getItem().getId())
                .author(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}