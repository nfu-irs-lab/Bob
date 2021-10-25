package com.example.hiwin.teacher_version_bob.data.data;

import com.example.hiwin.teacher_version_bob.R;

public enum Face {
    sad(R.raw.face_cry), happy(R.raw.face_happy),love_eyes(R.raw.face_love_eyes),surprise(R.raw.face_surprise);
    private final int gif_id;

    Face(int gif_id) {
        this.gif_id = gif_id;
    }

    public int getGifId() {
        return gif_id;
    }
}
