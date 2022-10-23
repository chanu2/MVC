package SCH_JOIN.join.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Participation {

    @Id @GeneratedValue
    @Column(name = "participation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus participationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy ="participation",cascade = CascadeType.ALL)
    private List<ParticipationSports> participationSportsList = new ArrayList<>();


    //==연관 관계 편의 메서드==//

    public void setMember(Member member){
        this.member = member;
        member.getParticipations().add(this);
    }

    public void addParticipationSports(ParticipationSports participationSports){
        participationSportsList.add(participationSports);
        participationSports.setParticipation(this);
    }


    //== 생성 메서드 ==/

    public static Participation createParticipation(Member member,ParticipationSports...participationSportsList){
        Participation participation = new Participation();
        participation.setMember(member);

        for (ParticipationSports participationSports : participationSportsList) {
            participation.addParticipationSports(participationSports);

        }
        participation.setParticipationStatus(ParticipationStatus.ATTEND);
        return participation;

    }

    //== 비지니스 로직 ==//

    /**
     * 주문 취소
     */

    public void cancel(){
        this.setParticipationStatus(ParticipationStatus.CANCEL);
        for(ParticipationSports participationSports : participationSportsList){
            participationSports.cancel();
        }
    }



}
