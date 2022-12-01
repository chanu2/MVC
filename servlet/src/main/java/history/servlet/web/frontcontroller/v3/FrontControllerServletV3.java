package history.servlet.web.frontcontroller.v3;

import history.servlet.web.frontcontroller.ModelView;
import history.servlet.web.frontcontroller.MyView;
import history.servlet.web.frontcontroller.v2.ControllerV2;
import history.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import history.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import history.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import history.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import history.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import history.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FrontControllerServletV3",urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap

        Map<String, String> paramMap =  createParamMap(request);
        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName();//논리 이름만 얻을 수 있다 new-form

        MyView view = viewResolver(viewName);

        view.render(mv.getModel(),request,response);


    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private static Map<String, String>  createParamMap(HttpServletRequest request) {  // request에서 파라미터 모든 네이을 가져온다. 가져온 것들을 키 값으로 설정하고 paramMap에 저장을 해준다
        Map<String,String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
