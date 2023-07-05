package com.mohistmc.ai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/6 2:39:05
 */

@Controller
public class HelloController {

    @GetMapping("/")
    @ResponseBody
    public  String Home() {
        return "Hello";
    }
}
