package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }

    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        return new Comment(commentDto.getId(), commentDto.getText(), item, author, commentDto.getCreated());
    }
}