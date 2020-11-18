package com.ite5year.controllers;


import com.ite5year.models.SharedParameters;
import com.ite5year.repositories.ApplicationUserRepository;
import com.ite5year.repositories.SharedParametersRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ite5year.utils.GlobalConstants.BASE_URL;

@Controller
@RequestMapping(BASE_URL + "/sharedMap")
public class SharedParametersController {
    private final SharedParametersRepository sharedParametersRepository;

    public SharedParametersController(SharedParametersRepository sharedParametersRepository) {
        this.sharedParametersRepository = sharedParametersRepository;
    }

    @Resource(name="sharedParametersMap")
    private Map<String, Object> parametersMap;

   @PutMapping("/put-param")
    public @ResponseBody
    Map<String,Object> putParam(String key, String value) {
       if(key != null && value != null) {
           if(this.parametersMap.containsKey(key)) {
               return this.parametersMap;
           }
           sharedParametersRepository.save(new SharedParameters(key, value));
           this.parametersMap.put(key, value);
       }
        return this.parametersMap;
    }


    @GetMapping("/get-value")
    public @ResponseBody Object getValueByKey(String key) {
       if(this.parametersMap.containsKey(key)) {
           Object value = this.parametersMap.get(key);
           return new HashMap<String, Object>() {{
               put(key, value);
           }};
       }
       return new HashMap<String, Object>();
    }

    @GetMapping
    public @ResponseBody Map<String, Object> getMap() {
       return this.parametersMap;
    }
}
