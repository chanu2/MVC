package farmconnect.farmconnectbackend.repository.deal;

import farmconnect.farmconnectbackend.dto.CropItemDto;
import farmconnect.farmconnectbackend.entity.CropItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CropItemRepository extends JpaRepository<CropItem, Long> {
    CropItem save(CropItem cropItem);
    List<CropItem> findAll();
    boolean existsByCropItem(String cropItem);
    CropItem findByCropItem(String cropItem);

    @Transactional
    void deleteByCropItem(String cropItem);

}
