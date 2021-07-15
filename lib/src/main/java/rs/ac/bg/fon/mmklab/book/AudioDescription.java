package rs.ac.bg.fon.mmklab.book;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

@JsonSerialize
public class AudioDescription {
    private AudioInputStream audioInputStream;
    private AudioFormat audioFormat;
    private long lengthInFrames;
    private int frameSizeInBytes;

    public AudioDescription() {
    }

    public AudioDescription(AudioInputStream audioInputStream, AudioFormat audioFormat, long lengthInFrames, int frameSizeInBytes) {
        this.audioInputStream = audioInputStream;
        this.audioFormat = audioFormat;
        this.lengthInFrames = lengthInFrames;
        this.frameSizeInBytes = frameSizeInBytes;
    }


    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public long getLengthInFrames() {
        return lengthInFrames;
    }

    public int getFrameSizeInBytes() {
        return frameSizeInBytes;
    }


    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
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
                "audioInputStream=" + audioInputStream +
                ", audioFormat=" + audioFormat +
                ", lengthInFrames=" + lengthInFrames +
                ", frameSizeInBytes=" + frameSizeInBytes +
                '}';
    }
}
