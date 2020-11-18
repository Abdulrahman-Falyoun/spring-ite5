package com.ite5year.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/sharedMap")
public class SharedParametersController {
    @Resource(name="sharedParametersMap")
    private Map<String, Object> parametersMap;

   @PutMapping("/put-params")
    public @ResponseBody
    Map<?,?> put(String key, String value){
        this.parametersMap.put(key, value);
        return this.parametersMap;
    }

    @GetMapping("/")
    public @ResponseBody Map<String, Object> getMap() {
       return this.parametersMap;
    }
}
