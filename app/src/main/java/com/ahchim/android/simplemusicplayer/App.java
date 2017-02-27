package com.ahchim.android.simplemusicplayer;

import android.media.MediaPlayer;

/**
 * Created by Ahchim on 2017-02-24.
 */

public class App {
    public static MediaPlayer player = null;

    // 액션 플래그
    public static final String ACTION_PLAY = "com.ahchim.android.musicplayerservice.Action.Play";
    public static final String ACTION_PAUSE = "com.ahchim.android.musicplayerservice.Action.Pause";
    public static final String ACTION_RESTART = "com.ahchim.android.musicplayerservice.Action.Restart";

    // 플레이어 상태 플래그
    public static final int PLAY = 0, PAUSE = 1, STOP = 2;

    // 현재 플레이어 상태
    public static int playStatus = STOP;

    public static int position = 0;  // 현재 음악 위치
}


