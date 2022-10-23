package farmconnect.farmconnectbackend.repository.comment;

import farmconnect.farmconnectbackend.entity.NewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface NewCommentRepository extends JpaRepository<NewComment, Long> {
    NewComment save(NewComment newComment);
    Optional<NewComment> findById(Long aLong);

    @Override
    List<NewComment> findAll();

    @Override
    boolean existsById(Long aLong);

    @Transactional
    void deleteById(Long aLong);

    @Transactional
    void deleteAllByOriginDealId(Long aLong);

    @Transactional
    void deleteAllByCommentId(Long aLong);
}
