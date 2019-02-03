package com.appzone.dukkan.models;

import java.io.Serializable;

public class TypingModel implements Serializable {

    private int room_id;
    private int user_id;
    private int status;

    public TypingModel(int room_id, int user_id, int status) {
        this.room_id = room_id;
        this.user_id = user_id;
        this.status = status;
    }

    public int getRoom_id() {
        return room_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getStatus() {
        return status;
    }
}
