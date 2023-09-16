package com.mohistmc.ai.dashscope;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/16 18:14:36
 */
public class BaiChuan {

    public static String sendText(String message) {
        try {
            Generation gen = new Generation();
            GenerationParam param = GenerationParam
                    .builder()
                    .model("baichuan-7b-v1")
                    .prompt(message)
                    .build();
            GenerationResult result = gen.call(param);
            return result.getOutput().getText();
        }catch (Exception e) {
            return null;
        }
    }
}
