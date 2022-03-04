package com.example.hiwin.teacher_version_bob.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.fragment.*;
import org.json.JSONException;

public class PaperScissorStoneFragment extends StaticFragment {
    private Context context;
    private View root;
    private CommandListener commandListener;
    private Handler handler;
    private ImageView gesture_img;
    private GestureHandler gestureHandler;

    public interface CommandListener {
        void onCommand(String cmd);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_paper_scissor_stone, container, false);
        handler = new Handler();
        gesture_img = root.findViewById(R.id.paper_scissor_stone_image);

        root.findViewById(R.id.paper_scissor_stone_btn_paper).setOnClickListener(paper_scissor_stone_listener);
        root.findViewById(R.id.paper_scissor_stone_btn_scissor).setOnClickListener(paper_scissor_stone_listener);
        root.findViewById(R.id.paper_scissor_stone_btn_stone).setOnClickListener(paper_scissor_stone_listener);

        showGame();
        return root;
    }


    @Override
    protected View[] getViews() {
        return new View[0];
    }

    public void initialize(Context context) {
        this.context = context;
    }

    private void showGame() {
        gestureHandler = new GestureHandler(new GestureListener() {
            @Override
            public void onWin() {
                MediaPlayer.create(context, R.raw.sound_correct_2).start();
                new Thread(() -> {

                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    end();
                }).start();
            }

            @Override
            public void onLoss() {
                MediaPlayer.create(context, R.raw.sound_try_again).start();
                new Thread(() -> {
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gestureHandler.restart();
                }).start();
            }

            @Override
            public void onPeace() {
                MediaPlayer.create(context, R.raw.sound_try_again).start();
                new Thread(() -> {
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gestureHandler.restart();
                }).start();
            }

            @Override
            public void onGestureChange(Gesture gesture) {
                handler.post(() -> {
                    gesture_img.setImageDrawable(context.getDrawable(gesture.getDrawableId()));
                });
            }
        });

        new Thread(gestureHandler).start();
    }

    private final View.OnClickListener paper_scissor_stone_listener = v -> {
        Gesture gesture;
        if (v.getId() == R.id.paper_scissor_stone_btn_paper) {
            gesture = Gesture.paper;
        } else if (v.getId() == R.id.paper_scissor_stone_btn_scissor) {
            gesture = Gesture.scissor;
        } else if (v.getId() == R.id.paper_scissor_stone_btn_stone) {
            gesture = Gesture.stone;
        } else
            throw new RuntimeException();

        gestureHandler.stop();

        int result = gesture.compare(gestureHandler.getValue());

        if (commandListener != null)
            if (gestureHandler.getValue() == Gesture.paper) {
                commandListener.onCommand("DO_ACTION gesture_paper.csv");
            } else if (gestureHandler.getValue() == Gesture.scissor) {
                commandListener.onCommand("DO_ACTION gesture_scissor.csv");
            } else if (gestureHandler.getValue() == Gesture.stone) {
                commandListener.onCommand("DO_ACTION gesture_stone.csv");
            }
        gestureHandler.doResult(result);

    };


    public enum Gesture {
        paper(R.drawable.gesture_paper), scissor(R.drawable.gesture_scissor), stone(R.drawable.gesture_stone);

        private final int drawableId;

        Gesture(int drawableId) {
            this.drawableId = drawableId;
        }

        public int getDrawableId() {
            return drawableId;
        }

        public int compare(Gesture gesture2) {
            if (this == paper) if (gesture2 == stone) return 1;
            else if (gesture2 == scissor) return -1;
            else if (gesture2 == paper) return 0;
            if (this == scissor) if (gesture2 == paper) return 1;
            else if (gesture2 == stone) return -1;
            else if (gesture2 == scissor) return 0;

            if (this == stone) if (gesture2 == scissor) return 1;
            else if (gesture2 == paper) return -1;
            else if (gesture2 == stone) return 0;

            throw new RuntimeException();
        }
    }


    private interface GestureListener {
        void onWin();

        void onLoss();

        void onPeace();

        void onGestureChange(Gesture gesture);
    }

    private static class GestureHandler implements Runnable {
        private final GestureListener listener;
        private boolean run = true;
        private Gesture value;

        GestureHandler(GestureListener listener) {
            this.listener = listener;
        }

        @Override
        public void run() {
            run = true;
            while (run) {
                int v = (int) (Math.random() * 2.) + 1;
                value = Gesture.values()[v];
                listener.onGestureChange(value);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void restart() {
            run();
        }

        public void stop() {
            run = false;
        }

        public Gesture getValue() {
            return value;
        }

        public void doResult(int result) {
            if (result == 1) {
                listener.onWin();
            } else if (result == -1) {
                listener.onPeace();
            } else if (result == 0) {
                listener.onLoss();
            }
        }
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }
}
