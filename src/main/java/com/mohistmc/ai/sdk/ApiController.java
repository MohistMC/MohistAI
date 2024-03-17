package com.mohistmc.ai.sdk;

import com.mohistmc.ai.MohistAI;
import com.mohistmc.tools.XMLUtil;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @PostMapping("/qq")
    public ResponseEntity<MessageRequest> qq(@RequestBody MessageRequest request) {
        String t = request.getMessage_type();
        if (t == null) {
            System.out.print("==================================================");
            System.out.printf(request.toString());
            System.out.print("==================================================");
        }
        if (t != null && t.equals("group")) {
            MohistAI.LOGGER.info("[群消息] 群号<%s> 发言者<%s>: %s".formatted(
                    request.getGroup_id(),
                    request.getUser_id(),
                    XMLUtil.unescapeXML(request.getRaw_message())));
        }
        return ResponseEntity.ok(request);
    }

    /**
     * 用于DEBUG调试
     */
    @SneakyThrows
    @PostMapping("/qq_string")
    public ResponseEntity<String> qq_string(@RequestBody String request) {
        System.out.printf(request);
        return ResponseEntity.ok(request);
    }
}
