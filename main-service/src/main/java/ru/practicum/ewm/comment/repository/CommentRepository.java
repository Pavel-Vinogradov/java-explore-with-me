package ru.practicum.ewm.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEvent_Id(Long eventId, Pageable pageable);

    List<Comment> findByAuthor_Id(Long userId);

    @Query("select c " +
            "from comments as c " +
            "where lower(c.text) like lower(concat('%', ?1, '%') )")
    List<Comment> search(String text, Pageable pageable);
}