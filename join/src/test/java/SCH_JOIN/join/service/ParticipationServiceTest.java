package SCH_JOIN.join.service;

import SCH_JOIN.join.domain.Member;
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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = false)
public class ParticipationServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    ParticipationService participationService;
    @Autowired
    ReserveSportsService reserveSportsService;

    @Autowired
    EntityManager em;


    @Test
    public void 경기참여() throws Exception{

        Member member = new Member();
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        member.setName("김찬우");
        member1.setName("이훈일");
        member2.setName("기범");
        member3.setName("왕세");
        member.setPhoneNum("01082527933");
        member1.setPhoneNum("0108256786");
        member2.setPhoneNum("0101245512");
        member3.setPhoneNum("01064201507");

        em.persist(member);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);




        Long reserve = reserveSportsService.reserveSports(member.getId(), Sport.SOCCER, 12L, LocalDateTime.of(2000, 1, 3, 17, 30),
                LocalDateTime.of(2000, 1, 3, 18, 30), "빨리와", "나와");

        Long reserve1 = reserveSportsService.reserveSports(member1.getId(), Sport.BASKETBALL, 12L, LocalDateTime.of(2000, 2, 3, 12, 30),
                LocalDateTime.of(2000, 2, 3, 14, 30), "농구하자", "왕세 이기는 사람 나와라 시발");

        participationService.participation(member2.getId(),reserve);
        participationService.participation(member3.getId(),reserve1);

        em.flush();
        em.clear();

    }
    @Test
    public void 참여취소() throws Exception{

        Member member = new Member();
        Member member1 = new Member();
        member.setName("김찬우");
        member1.setName("이훈일");
        member.setPhoneNum("01082527933");
        member1.setPhoneNum("0108256786");
        em.persist(member);
        em.persist(member1);

        Long reserve = reserveSportsService.reserveSports(member.getId(), Sport.SOCCER, 12L, LocalDateTime.of(2000, 1, 3, 17, 30),
                LocalDateTime.of(2000, 1, 3, 18, 30), "빨리와", "나와");

        Long participation = participationService.participation(member1.getId(), reserve);


        participationService.cancelParticipation(participation);

    }


}