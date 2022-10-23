package SCH_JOIN.join.service;

import SCH_JOIN.join.domain.Member;
import SCH_JOIN.join.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)//변경하는 것이 아니면 리디온리 true를 해주는 것이 좋다
@RequiredArgsConstructor  // 자동 생성자를 만들어 준다
public class MemberService {

    private final MemberRepository memberRepository;



    // 회원 가입 좀더 추가 해야함
    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    private void DuplicateLoginId(Member member) {
        List<Member> findMembers = memberRepository.findByLoginId(member.getLoginId());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 아이디 입니다");
        }


    }


    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


    public List<Member> findMembers(){
        return memberRepository.findAll();
    }






}
