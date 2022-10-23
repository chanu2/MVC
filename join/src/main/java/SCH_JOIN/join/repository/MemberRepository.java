package SCH_JOIN.join.repository;

import SCH_JOIN.join.domain.Member;
import SCH_JOIN.join.domain.ReserveSports;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;


    //멤버 저장
    public void save(Member member){
        em.persist(member);
    }

    //한명 찾기
    public Member findOne(Long id){
        return em.find(Member.class,id);
    }


    //멤버 모두 조회
    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class).getResultList();
    }


    public List<Member> findByLoginId(String loginId) {
        return em.createQuery("select m from Member m where m.loginId = :loginId ",Member.class) .setParameter("loginId",loginId).getResultList();
    }


}
