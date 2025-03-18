package com.android.echostrings.network.data;

import java.util.List;

public class MusicSheetResponse {
    private String status;
    private List<MusicSheet> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MusicSheet> getData() {
        return data;
    }

    public void setData(List<MusicSheet> data) {
        this.data = data;
    }
}

