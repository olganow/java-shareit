package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {

    @Autowired
    private JacksonTester<CommentDto> json;
    private static final String TIME_PATTERN_TEST = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN_TEST);

    @Test
    void commentDtoTest() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .itemId(1L)
                .authorName("Author_Name")
                .text("Text_comment")
                .created(now.truncatedTo(ChronoUnit.SECONDS))
                .build();
        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(now.truncatedTo(ChronoUnit.SECONDS).format(formatter));
    }
}
