package SCH_JOIN.join.domain;

import SCH_JOIN.join.exception.NotEnoughCurrentException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class ReserveSports {

    @Id @GeneratedValue
    @Column(name = "reserve_sports_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Sport sport;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private Long recruitmentNum;
    private int currentNum = 1;

    private LocalDateTime nowReserveDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String title;
    private String explanation;

    @Enumerated(EnumType.STRING)
    private ReserveSportsStatus reserveSportsStatus;

    //생성 메서드


    public static ReserveSports createReserve(Member member, Sport sport, Long recruitmentNum,
                                              LocalDateTime startDate, LocalDateTime endDate, String title,String explanation){
        ReserveSports reserveSports = new ReserveSports();
        reserveSports.setMember(member);
        reserveSports.setSport(sport);
        reserveSports.setRecruitmentNum(recruitmentNum);
        reserveSports.setStartDate(startDate);
        reserveSports.setEndDate(endDate);
        reserveSports.setNowReserveDate(LocalDateTime.now());
        reserveSports.setTitle(title);
        reserveSports.setExplanation(explanation);
        reserveSports.setReserveSportsStatus(ReserveSportsStatus.RESERVE);
        return reserveSports;

    }

    public void addCurrentNum(){
        this.currentNum++;
    }

    public void subtractCurrentNum(){

        this.currentNum--;

    }






}























