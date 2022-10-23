package SCH_JOIN.join.service;

import SCH_JOIN.join.domain.Member;
import SCH_JOIN.join.domain.ReserveSports;
import SCH_JOIN.join.domain.Sport;
import SCH_JOIN.join.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = false)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 로컬타임() throws Exception{

        Member member = new Member();
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();

        member.setName("김찬우");
        member1.setName("이훈일");
        member2.setName("이건희");
        member3.setName("조준장");
        member4.setName("왕세준");

        member.setPhoneNum("01082527933");
        member1.setPhoneNum("0108256786");
        member2.setPhoneNum("0108252678");
        member3.setPhoneNum("01025279678");
        member4.setPhoneNum("0108259787");


        em.persist(member);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);


        ReserveSports reserveSports = new ReserveSports();
        ReserveSports reserveSports1 = new ReserveSports();
        ReserveSports reserveSports2 = new ReserveSports();
        ReserveSports reserveSports3 = new ReserveSports();
        ReserveSports reserveSports4 = new ReserveSports();

        reserveSports.setMember(member);
        reserveSports1.setMember(member1);
        reserveSports2.setMember(member2);
        reserveSports3.setMember(member3);
        reserveSports4.setMember(member4);

        reserveSports.setSport(Sport.SOCCER);
        reserveSports1.setSport(Sport.BASKETBALL);
        reserveSports2.setSport(Sport.SOCCER);
        reserveSports3.setSport(Sport.SOCCER);
        reserveSports4.setSport(Sport.BASKETBALL);

        reserveSports.setEndDate(LocalDateTime.of(2000,1,3,17,30));
        reserveSports.setStartDate(LocalDateTime.of(2000,1,3,20,20));

        reserveSports1.setEndDate(LocalDateTime.of(2001,2,3,17,30));
        reserveSports1.setStartDate(LocalDateTime.of(2001,2,3,18,20));

        reserveSports2.setEndDate(LocalDateTime.of(2005,7,3,15,30));
        reserveSports2.setStartDate(LocalDateTime.of(2005,7,3,19,20));

        reserveSports3.setEndDate(LocalDateTime.of(2002,4,3,12,30));
        reserveSports3.setStartDate(LocalDateTime.of(2002,4,3,19,20));

        reserveSports4.setEndDate(LocalDateTime.of(2000,1,3,14,30));
        reserveSports4.setStartDate(LocalDateTime.of(2000,1,3,20,20));

        em.persist(reserveSports);
        em.persist(reserveSports1);
        em.persist(reserveSports2);
        em.persist(reserveSports3);
        em.persist(reserveSports4);

        em.flush();

        em.close();




    }

}