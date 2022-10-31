package history.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {


    /**
     *
     * @param parmMap
     * @param model
     * @return viewName
     */
    String process(Map<String,String> parmMap ,Map<String,Object> model);
}
