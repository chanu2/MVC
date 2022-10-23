package SCH_JOIN.join.repository;


import SCH_JOIN.join.domain.ReserveSports;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReserveSportsRepository {
    private final EntityManager em;

    public void save(ReserveSports reserveSports){
        em.persist(reserveSports);
    }

    public ReserveSports findOne(Long id){
        return em.find(ReserveSports.class,id);
    }

    public List<ReserveSports> findAll(){
        return em.createQuery("select r from ReserveSports r").getResultList();
    }

    // 날짜별
    // 축구 경기 가져오기
    public List<ReserveSports> findSoccerDate(){
        return em.createQuery("select r from ReserveSports r ORDER BY r.startDate").getResultList();
    }


    //날짜별
    // 농구 경기 가져오기


    //날짜별
    // 런닝 경기 가져오기





}
