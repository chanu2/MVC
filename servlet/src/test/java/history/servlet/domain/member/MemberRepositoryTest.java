package history.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void save(){
        // given
        Member member = new Member("hello", 20);

        //when
        Member saveMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(saveMember.getId());
        Assertions.assertThat(findMember).isEqualTo(saveMember);
    }



}