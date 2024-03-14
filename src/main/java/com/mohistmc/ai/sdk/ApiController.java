package com.mohistmc.ai.sdk;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @SneakyThrows
    @PostMapping("/qq")
    public ResponseEntity<MessageRequest> qq(@RequestBody MessageRequest request) {
        if (request.getMessage_type().equals("group")) {
            System.out.printf("[群消息] 群号<%s> 发言者<%s> [%s]: %s%n",
                    request.getGroup_id(),
                    request.getUser_id(),
                    request.getMessage().getFirst().type(),
                    request.getMessage().getFirst().data().text());
        }
        return ResponseEntity.ok(request);
    }
}
