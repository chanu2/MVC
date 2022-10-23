package SCH_JOIN.join.repository;

import SCH_JOIN.join.domain.Participation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ParticipationRepository {

    private final EntityManager em;


    public void save(Participation participation){
        em.persist(participation);
    }

    public Participation findOne(Long id){
        return em.find(Participation.class,id);
    }

    public void delete(Long id) {
        em.createQuery("select p from Participation p where p.id = :id ",Participation.class).setParameter("id",id).getSingleResult();
    }
}
