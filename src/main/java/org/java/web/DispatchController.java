package org.java.web;

import org.java.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DispatchController {


    @Autowired
    private DispatchService dispatchService;


    @RequestMapping("taskAutoDispatch/{processInstId}")
    @ResponseBody
    public List<String> taskAutoDispatch(@PathVariable("processInstId") String processInstId){
        List<String> list = dispatchService.submitAndAssign(processInstId);
        return list;
    }
}
