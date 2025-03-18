package com.android.echostrings.network.data;

public class ChordRecognitionResponse {
    private String status;
    private String chord;
    private String filename;

    // Getters and Setters...

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getChord() { return chord; }
    public void setChord(String chord) { this.chord = chord; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
}