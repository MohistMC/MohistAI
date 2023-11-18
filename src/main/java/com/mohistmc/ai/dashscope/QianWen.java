package com.mohistmc.ai.dashscope;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.mohistmc.ai.MohistConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * @author Mgazul by MohistMC
 * @date 2023/9/15 7:22:16
 */
public class QianWen {

    public static String sendText(String message) {
        try {
            Generation gen = new Generation();
            MessageManager msgManager = new MessageManager(10);
            Message userMsg = Message.builder().role(Role.USER.getValue()).content(message).build();
            msgManager.add(userMsg);
            QwenParam param =
                    QwenParam.builder().model(Generation.Models.QWEN_TURBO).messages(msgManager.get())
                            .resultFormat(QwenParam.ResultFormat.MESSAGE)
                            .topP(0.8)
                            .enableSearch(true)
                            .build();
            GenerationResult result = gen.call(param);
            return result.getOutput().getChoices().getFirst().getMessage().getContent();
        } catch (Exception e) {
            return "妈, 这题好难^~^";
        }
    }

    public static File SyncAudioDataToFile(Long id, String msg) {
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                .model("sambert-zhiwei-v1")
                .text(msg)
                .sampleRate(16000)
                .format(SpeechSynthesisAudioFormat.WAV)
                .apiKey(MohistConfig.dashscope_apikey)
                .build();

        File file = new File(id + ".wav");
        // 调用call方法，传入param参数，获取合成音频
        ByteBuffer audio = synthesizer.call(param);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public static URL basicCall(String message) throws ApiException, NoApiKeyException, MalformedURLException {
        ImageSynthesis is = new ImageSynthesis();
        ImageSynthesisParam param =
                ImageSynthesisParam.builder()
                        .model(ImageSynthesis.Models.WANX_V1)
                        .n(1)
                        .size("1024*1024")
                        .prompt(message)
                        .build();

        ImageSynthesisResult result = is.call(param);
        return URI.create(result.getOutput().getResults().getFirst().get("url")).toURL();
    }
}
