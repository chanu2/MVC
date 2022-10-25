package history.servlet.web.frontcontroller.v2.controller;

import history.servlet.domain.member.Member;
import history.servlet.domain.member.MemberRepository;
import history.servlet.web.frontcontroller.MyView;
import history.servlet.web.frontcontroller.v2.ControllerV2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberSaveControllerV2 implements ControllerV2 {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username,age);
        memberRepository.save(member);

        //Model에 데이터를 보관한다
        request.setAttribute("member",member);


        return new MyView("/WEB-INF/views/save-result.jsp");


    }
}
