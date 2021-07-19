package rs.ac.bg.fon.mmklab.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.sound.sampled.AudioFormat;
import java.io.Serializable;
import java.util.Objects;

@JsonSerialize
public class CustomAudioFormat implements Serializable {
    private boolean bigEndian;
    //    Indicates whether the audio data is stored in big-endian or little-endian order.
    private int channels;
    //    The number of audio channels in this format (1 for mono, 2 for stereo).
    private String encoding;
    //    The audio encoding technique used by this format. PCM_UNSIGNED
    private float frameRate;
    //    The number of frames played or recorded per second, for sounds that have this format.
    private int frameSize;
    //    The number of bytes in each frame of a sound that has this format.
    private float sampleRate;
    //    The number of samples played or recorded per second, for sounds that have this format.
    private int sampleSizeInBits;
//    The number of bits in each sample of a sound that has this format.


    public CustomAudioFormat() {
    }

    public CustomAudioFormat(boolean bigEndian, int channels, String encoding, float frameRate, int frameSize, float sampleRate, int sampleSizeInBits) {
        this.bigEndian = bigEndian;
        this.channels = channels;
        this.encoding = encoding;
        this.frameRate = frameRate;
        this.frameSize = frameSize;
        this.sampleRate = sampleRate;
        this.sampleSizeInBits = sampleSizeInBits;
    }

    public boolean isBigEndian() {
        return bigEndian;
    }

    public int getChannels() {
        return channels;
    }

    public String getEncoding() {
        return encoding;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public float getSampleRate() {
        return sampleRate;
    }

    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public static AudioFormat toStandard(CustomAudioFormat custom){
        return new AudioFormat(
                new AudioFormat.Encoding(custom.getEncoding()),
                custom.getSampleRate(),custom.getSampleSizeInBits(),
                custom.getChannels(),
                custom.getFrameSize(), custom.getFrameRate(),
                custom.isBigEndian());
    }

    public static CustomAudioFormat toCustom(AudioFormat std){
        return new CustomAudioFormat(std.isBigEndian(), std.getChannels(), std.getEncoding().toString(),
                std.getFrameRate(), std.getFrameSize(), std.getSampleRate(), std.getSampleSizeInBits());
    }

    @Override
    public String toString() {
        return "CustomAudioFormat{" +
                "bigEndian=" + bigEndian +
                ", channels=" + channels +
                ", encoding='" + encoding + '\'' +
                ", frameRate=" + frameRate +
                ", frameSize=" + frameSize +
                ", sampleRate=" + sampleRate +
                ", sampleSizeInBits=" + sampleSizeInBits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomAudioFormat)) return false;
        CustomAudioFormat that = (CustomAudioFormat) o;
        return isBigEndian() == that.isBigEndian() && getChannels() == that.getChannels() && Float.compare(that.getFrameRate(), getFrameRate()) == 0 && getFrameSize() == that.getFrameSize() && Float.compare(that.getSampleRate(), getSampleRate()) == 0 && getSampleSizeInBits() == that.getSampleSizeInBits() && Objects.equals(getEncoding(), that.getEncoding());
    }
}
