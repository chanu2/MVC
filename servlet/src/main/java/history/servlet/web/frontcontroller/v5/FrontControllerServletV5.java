package history.servlet.web.frontcontroller.v5;

import history.servlet.web.frontcontroller.ModelView;
import history.servlet.web.frontcontroller.MyView;
import history.servlet.web.frontcontroller.v3.ControllerV3;
import history.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import history.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import history.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import history.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import history.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import history.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import history.servlet.web.frontcontroller.v5.adapter.ControllerHandleV3Adapter;
import history.servlet.web.frontcontroller.v5.adapter.ControllerHandleV4Adapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String , Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();

    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form",new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save",new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members",new MemberListControllerV3());

        //v4
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form",new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save",new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v/4/members",new MemberListControllerV4());
    }
    private void initHandlerAdapters() {

        handlerAdapters.add(new ControllerHandleV3Adapter());
        handlerAdapters.add(new ControllerHandleV4Adapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object handler = getHandler(request);  // handler ????????????    //MemberFormCONTROLLERv3 ??????
                                                 // controller4 ?????? ??? ??? MemberFormControllerV4??????

        if(handler == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //handle ????????? ???????????? ????????????
        MyHandlerAdapter adapter = getHandlerAdapter(handler);  //MemberFormControllerV3??????


        //ControllerHandleV3Adapter??????
        ModelView mv = adapter.handle(request, response, handler);
        //adapter = ControllerHandleV4Adapter

        String viewName = mv.getViewName();//?????? ?????? new-form

        MyView view = viewResolver(viewName);

        view.render(mv.getModel(),request,response);

    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        // handler = MemberFormControllerV3
        // handler = MemberFormControllerV4
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if(adapter.supports(handler)){
                return adapter; //ControllerHandleV3Adapter ?????? ????????? 1??? ???
                                //adapter = ControllerHandleV4Adapter
            }
        }
        throw new IllegalArgumentException("handler adapter??? ?????? ??? ???????????? handler=" + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

}
