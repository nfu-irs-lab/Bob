package com.example.hiwin.teacher_version_bob;

public class Constants {
    public static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect";

    public static int getObjectDrawableId(String name) {
        switch (name) {
            case "car":
                return R.drawable.object_car;
            case "knife":
                return R.drawable.object_knife;
            case "cake":
                return R.drawable.object_cake;
            case "bird":
                return R.drawable.object_bird;
            case "bowl":
                return R.drawable.object_bowl;
            case "human":
                return R.drawable.object_person;
            case "cat":
                return R.drawable.object_cat;
            case "bottle":
                return R.drawable.object_bottle;
        }
        throw new RuntimeException("Drawable not found");
    }

    public static int getFaceDrawableId(String name) {
        switch (name) {
            case "smile":
                return R.drawable.face_smile;
            case "sad":
                return R.drawable.face_sad;
        }
        throw new RuntimeException("Drawable not found");
    }


}
