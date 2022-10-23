package farmconnect.farmconnectbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Subselect("SELECT a.id AS deal_id, IFNULL(b.comment_cnt,0) AS comment_cnt\n" +
        "FROM deal AS a \n" +
        "LEFT JOIN (\n" +
        "\tSELECT deal_id, COUNT(deal_id) AS comment_cnt\n" +
        "\tFROM comment\n" +
        "\tGROUP BY deal_id) AS b\n" +
        "ON a.id = b.deal_id")
@Synchronize("Comment")
public class SubQuery {
    @Id
    private Long dealId;
    private Integer commentCnt;
}
