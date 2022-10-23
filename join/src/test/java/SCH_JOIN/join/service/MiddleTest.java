package SCH_JOIN.join.service;


import SCH_JOIN.join.domain.Member;
import SCH_JOIN.join.domain.Sport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = false)
public class MiddleTest {


    @Autowired MemberService memberService;

    @Autowired ParticipationService participationService;

    @Autowired ReserveSportsService reserveSportsService;

    @Autowired EntityManager em;

    @Test
    public void 경기생성_및_경기참여() throws Exception{

        //given

        Member member = new Member();
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();
        Member member5 = new Member();
        Member member6 = new Member();
        Member member7 = new Member();

        member.setName("김찬우");
        member1.setName("이훈일");
        member2.setName("이건희");
        member3.setName("조준장");
        member4.setName("왕세준");
        member5.setName("이기범");
        member6.setName("하재은");
        member7.setName("은혜");


        member.setPhoneNum("01082527933");
        member1.setPhoneNum("01082567865");
        member2.setPhoneNum("0108252678");
        member3.setPhoneNum("01025279678");
        member4.setPhoneNum("0108259787");
        member5.setPhoneNum("01082597871");
        member6.setPhoneNum("01082597873");
        member7.setPhoneNum("01082597875");

        member.setSchoolNum(1234L);
        member1.setSchoolNum(12345L);
        member2.setSchoolNum(123456l);
        member3.setSchoolNum(124123l);
        member4.setSchoolNum(123412l);
        member5.setSchoolNum(13412l);
        member6.setSchoolNum(12412l);
        member7.setSchoolNum(12312l);

        em.persist(member);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);
        em.persist(member7);

        //when
        // 김찬우님 축구 방 생성
        Long reserve = reserveSportsService.reserveSports(member.getId(), Sport.SOCCER, 12L, LocalDateTime.of(2000, 1, 3, 17, 30),
                LocalDateTime.of(2000, 1, 3, 18, 30), "빨리와", "나와");

        //이훈일님 농구 방 생성
        Long reserve1 = reserveSportsService.reserveSports(member1.getId(), Sport.BASKETBALL, 12L, LocalDateTime.of(2000, 2, 3, 12, 30),
                LocalDateTime.of(2000, 2, 3, 14, 30), "농구하자", "왕세 이기는 사람 나와라 시발");



        //then
        //축구참여
        participationService.participation(member2.getId(),reserve);
        participationService.participation(member3.getId(),reserve);
        participationService.participation(member4.getId(),reserve);
        participationService.participation(member5.getId(),reserve);



        //농구참여
        participationService.participation(member6.getId(),reserve1);
        participationService.participation(member7.getId(),reserve1);

        em.flush();
        em.clear();



    }
    @Test
    public void 경기참여_취소() throws Exception{
        /**
         * 준장님 건희님 재은님의 경기 참여 취소 상황
         */

        //given

        Member member = new Member();
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();
        Member member4 = new Member();
        Member member5 = new Member();
        Member member6 = new Member();
        Member member7 = new Member();

        member.setName("김찬우");
        member1.setName("이훈일");
        member2.setName("이건희");
        member3.setName("조준장");
        member4.setName("왕세준");
        member5.setName("이기범");
        member6.setName("하재은");
        member7.setName("은혜");


        member.setPhoneNum("01082527933");
        member1.setPhoneNum("01082567865");
        member2.setPhoneNum("0108252678");
        member3.setPhoneNum("01025279678");
        member4.setPhoneNum("0108259787");
        member5.setPhoneNum("01082597871");
        member6.setPhoneNum("01082597873");
        member7.setPhoneNum("01082597875");

        member.setSchoolNum(1234L);
        member1.setSchoolNum(12345L);
        member2.setSchoolNum(123456l);
        member3.setSchoolNum(124123l);
        member4.setSchoolNum(123412l);
        member5.setSchoolNum(13412l);
        member6.setSchoolNum(12412l);
        member7.setSchoolNum(12312l);

        em.persist(member);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        em.persist(member6);
        em.persist(member7);

        //when
        // 김찬우님 축구 방 생성
        Long reserve = reserveSportsService.reserveSports(member.getId(), Sport.SOCCER, 12L, LocalDateTime.of(2000, 1, 3, 17, 30),
                LocalDateTime.of(2000, 1, 3, 18, 30), "빨리와", "나와");

        //이훈일님 농구 방 생성
        Long reserve1 = reserveSportsService.reserveSports(member1.getId(), Sport.BASKETBALL, 12L, LocalDateTime.of(2000, 2, 3, 12, 30),
                LocalDateTime.of(2000, 2, 3, 14, 30), "농구하자", "왕세 이기는 사람 나와라 시발");



        //then
        Long participation = participationService.participation(member2.getId(), reserve);
        Long participation1 = participationService.participation(member3.getId(), reserve);
        Long participation2 = participationService.participation(member4.getId(), reserve);
        Long participation3 = participationService.participation(member5.getId(), reserve);
        Long participation4 = participationService.participation(member6.getId(), reserve1);
        Long participation5 = participationService.participation(member7.getId(), reserve1);

        // 참여 취소

        participationService.cancelParticipation(participation2);
        participationService.cancelParticipation(participation3);
        participationService.cancelParticipation(participation5);


        em.flush();
        em.clear();



    }
}
