package com.android.echostrings.utils;
import android.content.Context;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.MFCC;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MFCCExtractor {

    public float[] extractMFCCFromFile(String audioFilePath, Context context) {
        List<float[]> mfccFrames = new ArrayList<>();

        try {
            // 创建音频 dispatcher（假设单声道，16bit PCM）
            File audioFile = new File(audioFilePath);
            int bufferSize = 1024;
            int overlap = 0;
            AudioDispatcher dispatcher = AudioDispatcherFactory.fromPipe(audioFilePath, 44100, bufferSize, overlap);

            // 初始化 MFCC 处理器
            TarsosDSPAudioFormat format = dispatcher.getFormat();
            MFCC mfcc = new MFCC(
                    bufferSize,
                    (float) format.getSampleRate(),
                    13,        // 13 coefficients
                    40,        // 40 filter banks
                    0,         // 最低频率
                    format.getSampleRate() / 2 // 最高频率
            );

            // 添加 MFCC 处理器
            dispatcher.addAudioProcessor(mfcc);

            // 添加帧收集器
            dispatcher.addAudioProcessor(new AudioProcessor() {
                @Override
                public boolean process(AudioEvent audioEvent) {
                    float[] frameMFCC = mfcc.getMFCC();
                    if (frameMFCC != null) {
                        mfccFrames.add(frameMFCC.clone());
                    }
                    return true;
                }

                @Override
                public void processingFinished() {}
            });

            // 开始处理音频
            dispatcher.run();

            // 处理 MFCC 帧到 13x400
            return processFramesTo13x400(mfccFrames);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private float[] processFramesTo13x400(List<float[]> frames) {
        if (frames.isEmpty()) return new float[13 * 400];

        int targetFrames = 400;
        int numCoefficients = 13;
        float[][] interpolated = new float[numCoefficients][targetFrames];

        // 对每个 MFCC 系数进行插值
        for (int coeffIndex = 0; coeffIndex < numCoefficients; coeffIndex++) {
            float[] coeffSeries = new float[frames.size()];
            for (int frameIndex = 0; frameIndex < frames.size(); frameIndex++) {
                coeffSeries[frameIndex] = frames.get(frameIndex)[coeffIndex];
            }
            interpolated[coeffIndex] = interpolate(coeffSeries, targetFrames);
        }

        // 转换为帧优先的一维数组
        float[] result = new float[numCoefficients * targetFrames];
        for (int frame = 0; frame < targetFrames; frame++) {
            for (int coeff = 0; coeff < numCoefficients; coeff++) {
                result[frame * numCoefficients + coeff] = interpolated[coeff][frame];
            }
        }

        return result;
    }

    private float[] interpolate(float[] input, int outputLength) {
        float[] output = new float[outputLength];
        if (input.length == 0) return output;
        if (input.length == 1) {
            Arrays.fill(output, input[0]);
            return output;
        }

        double scale = (double) (input.length - 1) / (outputLength - 1);
        for (int i = 0; i < outputLength; i++) {
            double position = i * scale;
            int left = (int) Math.floor(position);
            int right = Math.min(left + 1, input.length - 1);
            float ratio = (float) (position - left);
            output[i] = (1 - ratio) * input[left] + ratio * input[right];
        }
        return output;
    }
}