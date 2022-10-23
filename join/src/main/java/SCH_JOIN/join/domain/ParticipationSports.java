package SCH_JOIN.join.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ParticipationSports {

    @Id @GeneratedValue
    @Column(name = "participation_sports_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id")
    private Participation participation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_sports_id")
    private ReserveSports reserveSports;




    // == 생성 메서드 ==//

    public static ParticipationSports createParticipationSports(ReserveSports reserveSports){
        ParticipationSports participationSports = new ParticipationSports();
        reserveSports.addCurrentNum();
        participationSports.setReserveSports(reserveSports);


        return participationSports;
    }



    //==  비지니스 로직 ==//
    public void cancel() {
        getReserveSports().subtractCurrentNum();
    }
}
