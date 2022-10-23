package SCH_JOIN.join.service;


import SCH_JOIN.join.domain.Member;
import SCH_JOIN.join.domain.Participation;
import SCH_JOIN.join.domain.ParticipationSports;
import SCH_JOIN.join.domain.ReserveSports;
import SCH_JOIN.join.repository.MemberRepository;
import SCH_JOIN.join.repository.ParticipationRepository;
import SCH_JOIN.join.repository.ParticipationSportsRepository;
import SCH_JOIN.join.repository.ReserveSportsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationService {

    private final MemberRepository memberRepository;
    private final ParticipationRepository participationRepository;
    private final ReserveSportsRepository reserveSportsRepository;

    private final ParticipationSportsRepository participationSportsRepository;


    // 참여
    @Transactional
    public Long participation(Long memberId , Long reserveSportsId){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        ReserveSports reserveSports = reserveSportsRepository.findOne(reserveSportsId);

        // 참여 할 경기 생성

        ParticipationSports participationSports = ParticipationSports.createParticipationSports(reserveSports);

        // 참여 생성
        Participation participation = Participation.createParticipation(member,participationSports);

        // 참여 저장
        participationRepository.save(participation);
        return participation.getId();

    }

    // 참여 취소

    @Transactional
    public void cancelParticipation(Long participationId){

        // 참여 엔티티 조회
        Participation participation = participationRepository.findOne(participationId);

        // 참여 취소
        participation.cancel();


    }

    @Transactional
    public void deleteParticipation(Long participationId){

        //참여 엔티티 조회
        Participation participation = participationRepository.findOne(participationId);
        ParticipationSports participationSports = participationSportsRepository.find

        participationRepository.delete(participation.getId());
        participationSportsRepository.delete()




    }



}
