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
@Subselect("SELECT a.id AS deal_id, IFNULL(b.new_comment_cnt,0) AS new_comment_cnt\n" +
        "        FROM deal AS a\n" +
        "        LEFT JOIN (\n" +
        "        SELECT origin_deal_id, COUNT(origin_deal_id) AS new_comment_cnt\n" +
        "        FROM new_comment\n" +
        "        GROUP BY origin_deal_id) AS b\n" +
        "        ON a.id = b.origin_deal_id")
@Synchronize("NewComment")
public class NewCommentSubQuery {
    @Id
    private Long dealId;
    private Integer newCommentCnt;
}