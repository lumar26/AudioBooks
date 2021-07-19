package rs.ac.bg.fon.mmklab.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.Serializable;
import java.util.Objects;

@JsonSerialize
public class AudioDescription implements Serializable {
    private CustomAudioFormat audioFormat;
    private long lengthInFrames;
    private int frameSizeInBytes;

    public AudioDescription() {
    }

    public AudioDescription(CustomAudioFormat audioFormat, long lengthInFrames, int frameSizeInBytes) {
        this.audioFormat = audioFormat;
        this.lengthInFrames = lengthInFrames;
        this.frameSizeInBytes = frameSizeInBytes;
    }



    public CustomAudioFormat getAudioFormat() {
        return audioFormat;
    }

    public long getLengthInFrames() {
        return lengthInFrames;
    }

    public int getFrameSizeInBytes() {
        return frameSizeInBytes;
    }


    public void setAudioFormat(CustomAudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    public void setLengthInFrames(long lengthInFrames) {
        this.lengthInFrames = lengthInFrames;
    }

    public void setFrameSizeInBytes(int frameSizeInBytes) {
        this.frameSizeInBytes = frameSizeInBytes;
    }

    @Override
    public String toString() {
        return "\nAudioDescription{" +
                ", audioFormat=" + audioFormat +
                ", lengthInFrames=" + lengthInFrames +
                ", frameSizeInBytes=" + frameSizeInBytes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AudioDescription)) return false;
        AudioDescription that = (AudioDescription) o;
        return getLengthInFrames() == that.getLengthInFrames() && getFrameSizeInBytes() == that.getFrameSizeInBytes() && Objects.equals(getAudioFormat(), that.getAudioFormat());
    }

}
