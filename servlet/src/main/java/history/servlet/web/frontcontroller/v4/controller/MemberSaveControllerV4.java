package history.servlet.web.frontcontroller.v4.controller;

import history.servlet.domain.member.Member;
import history.servlet.domain.member.MemberRepository;
import history.servlet.web.frontcontroller.ModelView;
import history.servlet.web.frontcontroller.v3.ControllerV3;
import history.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt((paramMap.get("age")));

        Member member = new Member(username, age);

        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member",member);
        return mv;
    }
}
