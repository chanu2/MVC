package farmconnect.farmconnectbackend.repository.deal;

import farmconnect.farmconnectbackend.dto.CropItemDto;
import farmconnect.farmconnectbackend.entity.CropItem;
import farmconnect.farmconnectbackend.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


public interface DealRepository extends JpaRepository<Deal, Long> {
    Deal save(Deal deal);
    Optional<Deal> findById(Long id);
    List<Deal> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE Deal d SET d.isEnd = true WHERE d.id = :id")
    Integer updateIsEnd(@Param("id") Long id);

    List<Deal> findAllByCrop(CropItem cropItem);

    @Transactional
    void deleteAllByCrop(CropItem cropItem);
}
