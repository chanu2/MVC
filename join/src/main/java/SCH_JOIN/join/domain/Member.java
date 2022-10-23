package SCH_JOIN.join.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private Long schoolNum;


    private String phoneNum;
    private String passWord;
    private String loginId;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<ReserveSports> reserveSports = new ArrayList<>();    //이 컬렉션들 바꾸지 말자

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Participation> participations = new ArrayList<>();


}






