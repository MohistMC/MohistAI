package com.mohistmc.ai.minecraft.mohistchain;

import com.mohistmc.ai.network.ContentType;
import com.mohistmc.ai.network.RequestPath;
import com.mohistmc.ai.network.event.HttpGetEvent;
import com.mohistmc.mjson.Json;
import java.io.File;
import java.net.URI;
import lombok.SneakyThrows;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/10 23:45:55
 *
 *  http://127.0.0.1:2024/api/1.16.5/latest
 *  http://s1.devicloud.cn:32023/api/1.16.5/latest/download
 */

public class ChinaAPIListener{

    @SneakyThrows
    public static void onEvent(HttpGetEvent event) {
        RequestPath requestPath = event.getRequestPath();
        String[] mapping = event.getO().split("/");
        if (requestPath.isUnknown() && mapping[1].equals("api")) {
            if (mapping.length == 4) {
                String variable = mapping[2];
                if (mapping[3].equals("latest")) {
                    if (variable.equals("1.12.2")) {
                        Json json = Json.object("number", 343, "md5", "d5256adbddc6fea7837ee2fdf88ae0f0", "url", "http://s1.devicloud.cn:32023/api/" + variable + "/latest/download");
                        event.setBytes(json.asBytes());
                    }
                    String v2 = "https://mohistmc.com/api/v2/projects/mohist/%s/builds/latest";
                    Json mohist = Json.read(URI.create(v2.formatted(variable)).toURL()).at("build");
                    Json json = Json.object(
                            "number", mohist.asInteger("number"),
                            "md5", mohist.asString("fileMd5"),
                            "url", "http://s1.devicloud.cn:32023/api/" + variable+ "/latest/download");
                    event.setBytes(json.asBytes());
                }
            }
            if (mapping.length == 5) {
                String variable = mapping[2];
                if (mapping[3].equals("latest") && mapping[4].equals("download")) {
                    String fileName = MohistChinaAPI.dataMap.get(variable);
                    if (fileName != null) {
                        File file = new File("mohistchinaapi/" + variable, fileName);
                        if (file.exists()) {
                            event.setContenttype(ContentType.FILE);
                            event.setFile(file);
                        }
                    }
                }
            }
        }
    }

    /*
    @GetMapping("/{variable}/latest/download")
    public ResponseEntity<Resource> download(@PathVariable String variable) {

        try {
            File folder = new File("mohist/" + variable, MohistChinaAPI.dataMap.get(variable));
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();

                if (files != null) {
                    for (File file : files) {
                        folder = file;
                    }
                } else {
                    return ResponseEntity.notFound().build();
                }
            }

            Resource resource = new UrlResource(folder.toPath().toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
     */
}
