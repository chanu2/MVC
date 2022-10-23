package farmconnect.farmconnectbackend.repository.comment;

import farmconnect.farmconnectbackend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository <Comment, Long> {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findAllByDealId(Long dealId);

    @Transactional
    void deleteById(Long aLong);

    @Transactional
    void deleteAllByDealId(Long dealId);

    @Transactional
    void deleteAllByUserId(Long userId);
}


