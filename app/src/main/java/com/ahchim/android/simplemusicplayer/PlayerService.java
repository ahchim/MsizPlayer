package com.ahchim.android.simplemusicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import static com.ahchim.android.simplemusicplayer.App.ACTION_PAUSE;
import static com.ahchim.android.simplemusicplayer.App.ACTION_PLAY;
import static com.ahchim.android.simplemusicplayer.App.ACTION_RESTART;
import static com.ahchim.android.simplemusicplayer.App.PAUSE;
import static com.ahchim.android.simplemusicplayer.App.PLAY;
import static com.ahchim.android.simplemusicplayer.App.playStatus;
import static com.ahchim.android.simplemusicplayer.App.player;

public class PlayerService extends Service {

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            switch(action){
                case ACTION_RESTART:
                case ACTION_PLAY:
                    playStart();
                    break;
                case ACTION_PAUSE:
                    playPause();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void playStart(){
        player.start();

        playStatus = PLAY;
    }

    // 플레이중이면 멈춤
    private void playPause(){
        player.pause();
        playStatus = PAUSE;
    }
}
