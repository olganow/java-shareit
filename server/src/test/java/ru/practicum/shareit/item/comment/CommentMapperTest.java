package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentMapperTest {
    private Item item;
    private User user;
    private Comment comment;
    private final LocalDateTime start = LocalDateTime.now();

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "User_name", "user@test.testz");
        item = new Item(1L, "Item_name", "Item_Description", true, user,
                null);
        comment = new Comment(1L, "Comment_Text", item, user, start);
    }

    @Test
    void commentToCommentDtoTest() {
        CommentDto commentDto = CommentMapper.commentToCommentDto(comment);

        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getItemId()).isEqualTo(comment.getItem().getId());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentDto.getCreated()).isEqualTo(comment.getCreated());
    }

}
