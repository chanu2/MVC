package SCH_JOIN.join.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ParticipationSportsRepository {

    private final EntityManager em;
}
