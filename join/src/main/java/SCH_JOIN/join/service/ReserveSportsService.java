package SCH_JOIN.join.service;


import SCH_JOIN.join.domain.Member;
import SCH_JOIN.join.domain.ReserveSports;
import SCH_JOIN.join.domain.Sport;
import SCH_JOIN.join.repository.MemberRepository;
import SCH_JOIN.join.repository.ParticipationRepository;
import SCH_JOIN.join.repository.ReserveSportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReserveSportsService {

    private final ReserveSportsRepository reserveSportsRepository;
    private final MemberRepository memberRepository;



    // 구장 예약
    @Transactional
    public Long reserveSports(Long memberId, Sport sport, Long recruitmentNum,
                              LocalDateTime startDate, LocalDateTime endDate, String title, String explanation){

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);

        //구장 생성

        ReserveSports reserveSports = ReserveSports.createReserve(member, sport, recruitmentNum,
                 startDate,endDate, title,explanation);

        // 구장 저장
        reserveSportsRepository.save(reserveSports);
        return reserveSports.getId();

    }

    // 구장 예약 취소
    @Transactional
    public void cancelReserve(Long reserveSportsId){

        // 구장 예약 엔티티 조회


    }








}
