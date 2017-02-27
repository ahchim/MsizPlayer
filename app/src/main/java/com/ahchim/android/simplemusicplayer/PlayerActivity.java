package com.ahchim.android.simplemusicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import static com.ahchim.android.simplemusicplayer.App.ACTION_PAUSE;
import static com.ahchim.android.simplemusicplayer.App.ACTION_PLAY;
import static com.ahchim.android.simplemusicplayer.App.ACTION_RESTART;
import static com.ahchim.android.simplemusicplayer.App.PAUSE;
import static com.ahchim.android.simplemusicplayer.App.PLAY;
import static com.ahchim.android.simplemusicplayer.App.STOP;
import static com.ahchim.android.simplemusicplayer.App.playStatus;
import static com.ahchim.android.simplemusicplayer.App.player;
import static com.ahchim.android.simplemusicplayer.App.position;

public class PlayerActivity extends AppCompatActivity {
    ViewPager viewPager;
    ImageButton btnPlay, btnRew, btnFf;

    List<Music> datas;
    PlayerAdapter adapter;

    SeekBar seekBar;
    TextView txtCurrent, txtDuration;

    Intent service;

    // 핸들러 상태 플래그
    public static final int PROGRESS_SET = 101;

    // 핸들러
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case PROGRESS_SET:
                    if(player!=null) {
                        seekBar.setProgress(player.getCurrentPosition());
                        txtCurrent.setText(Util.milliSecToTime(player.getCurrentPosition()));
                    }
                    break;
            }
        }
    };

    // onCreate 안에선 선언 안하는게 좋다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            setContentView(R.layout.activity_player);
        } else return;

        setComponent();
        setData();
        setViewPager();
    }

    private void setComponent(){
        service = new Intent(this, PlayerService.class);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        txtCurrent = (TextView) findViewById(R.id.txtCurrent);
        txtDuration = (TextView) findViewById(R.id.txtDuration);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnRew = (ImageButton) findViewById(R.id.btnRew);
        btnFf = (ImageButton) findViewById(R.id.btnFf);

        btnPlay.setOnClickListener(click);
        btnRew.setOnClickListener(click);
        btnFf.setOnClickListener(click);
    }

    private void setData(){
        // 이 어플에서는 미디어로 볼륨을 조절하겠다.
        // 볼륨 조절 버튼으로 미디어 음량만 조절하기 위한 설정
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // 0. 데이터 가져오기
        /*
        DataLoader loader = new DataLoader(this);
        loader.load();
        List<Music> datas = loader.get();
        */
        // 싱글톤 패턴으로 바꿔보기
        // 이미 한번 세팅되었으면 다시 안 부르게 되었다!
        datas = DataLoader.get(this);
    }

    private void setViewPager(){
        // 1. 뷰페이저 가져오기
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 2. 뷰페이저용 아답터 생성
        adapter = new PlayerAdapter(datas, this);

        // 아답터는 위젯이 달라지면 사용할 수 없다. (리사이클러뷰 상속 아답터 -> 뷰페이저 이런거 안됨.)
        // 3. 뷰페이저 아답터 연결
        viewPager.setAdapter(adapter);

        // 리스너만 연결하고 init에 플레이어 초기화만 매번 해주면 페이저 넘어가면서 노래 바뀐다.
        // 4. 뷰페이저 리스너 연결
        viewPager.addOnPageChangeListener(viewPagerListener);

        viewPager.setPageTransformer(false, pageTransformer);

        // 5. 특정 페이지 호출
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            position = bundle.getInt("position");

            // 음원길이 등의 음악 기본정보를 설정해 준다.
            // 첫페이지일 경우만 init 호출
            // 이유 : 첫페이지가 아닐경우 위의 setCurrentItem에 의해서 ViewPager의 onPageSelected가 호출된다.
            // 0번이 아닌 n페이지일 경우 0페이지 들어갔다가 n페이지가 호출됨.
            // 0페이지에서 0페이지로 갈 경우 리스너가 동작을 안해서 이짓함.
            if(position == 0) init();
                // 실제 페이지 값 계산 처리 - 페이지 이동
            else viewPager.setCurrentItem(position);
        }
    }

    ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        /*
        현재 Page의 위치가 조금이라도 바뀔때마다 호출되는 메소드
        첫번째 파라미터 : 현재 존재하는 View 객체들 중에서 위치가 변경되고 있는 View들
        두번째 파라미터 : 각 View 들의 상대적 위치( 0.0 ~ 1.0 : 화면 하나의 백분율)

                   1.현재 보여지는 Page의 위치가 0.0
                   Page가 왼쪽으로 이동하면 값이 -됨. (완전 왼쪽으로 빠지면 -1.0)
                   Page가 오른쪽으로 이동하면 값이 +됨. (완전 오른쪽으로 빠지면 1.0)

        주의할 것은 현재 Page가 이동하면 동시에 옆에 있는 Page(View)도 이동함.
        첫번째와 마지막 Page 일때는 총 2개의 View가 메모리에 만들어져 있음.
        나머지 Page가 보여질 때는 앞뒤로 2개의 View가 메모리에 만들어져 총 3개의 View가 instance 되어 있음.
        ViewPager 한번에 1장의 Page를 보여준다면 최대 View는 3개까지만 만들어지며
        나머지는 메모리에서 삭제됨.-리소스관리 차원.

        position 값이 왼쪽, 오른쪽 이동방향에 따라 음수와 양수가 나오므로 절대값 Math.abs()으로 계산
        position의 변동폭이 (-2.0 ~ +2.0) 사이이기에 부호 상관없이 (0.0~1.0)으로 변경폭 조절
        주석으로 수학적 연산을 설명하기에는 한계가 있으니 코드를 보고 잘 생각해 보시기 바랍니다.
        */
        @Override
        public void transformPage(View page, float position) {
            float normalizedposition = Math.abs( 1 - Math.abs(position) );

            page.setAlpha(normalizedposition);  //View의 투명도 조절
            page.setScaleX(normalizedposition/2 + 0.5f); //View의 x축 크기조절
            page.setScaleY(normalizedposition/2 + 0.5f); //View의 y축 크기조절
            page.setRotationY(position * 80); //View의 Y축(세로축) 회전 각도
            // setAnimation 등을 할수있겠당.
        }
    };

    View.OnClickListener click = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnPlay:
                    play();
                    break;
                case R.id.btnRew:
                    prev();
                    break;
                case R.id.btnFf:
                    next();
                    break;
            }
        }
    };

    // 씤바seekbar 체인지 리스너
    SeekBar.OnSeekBarChangeListener seekBarChangeListener= new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // User한테 터치받았을 때만 플레이어가 seekBar 움직임 동작을 한다.
            if(player!=null && fromUser) player.seekTo(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { }
    };

    // 뷰페이저 체인지 리스너
    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
        @Override
        public void onPageSelected(int pos) {
            position = pos;
            init();
        }
        @Override
        public void onPageScrollStateChanged(int state) { }
    };

    private void init(){
        // 뷰페이저로 이동할 경우 플레이어에 세팅된 값을 해제한후 로직을 실행한다.
        if(player != null && playStatus != PLAY) {
            // 플레이 상태를 STOP으로 변경
            playStatus = STOP;
            // 아이콘을 플레이 버튼으로 변경
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
            player.release();
        }
        playerInit();
        controllerInit();

        if(playStatus != PLAY){
            play();
        } else {

        }
    }

    private void playerInit(){
        Uri musicUri = datas.get(position).getUri();

        // 플레이어에 음원 세팅
        player = MediaPlayer.create(this, musicUri);          // 미디어플레이어도 싱글톤이당
        player.setLooping(false);  // 반복여부

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });
        // Log.i("전체음악길이", Util.milliSecToTime(player.getDuration()));
    }

    private void controllerInit(){
        // seekBar 길이
        seekBar.setMax(player.getDuration());
        // seekBar 초기화
        seekBar.setProgress(0);
        // 전체 플레이시간 설정
        txtDuration.setText(Util.milliSecToTime(player.getDuration()));
        // 현재 플레이시간을 0으로 설정
        txtCurrent.setText("00:00");
    }

    private void play(){
        switch(playStatus){
            case STOP:
                playStart();
                break;
            case PLAY:
                playPause();
                break;
            case PAUSE:
                playRestart();
                break;
        }
    }

    private void playStart(){
        playStatus = PLAY;

        service.setAction(ACTION_PLAY);
        startService(service);

        btnPlay.setImageResource(android.R.drawable.ic_media_pause);

        // 새로운 쓰레드로 스타트
        Thread thread = new TimerThread();
        thread.start();
    }

    // 플레이중이면 멈춤
    private void playPause(){
        playStatus = PAUSE;
        service.setAction(ACTION_PAUSE);
        startService(service);

        btnPlay.setImageResource(android.R.drawable.ic_media_play);
    }

    // 멈춤상태이면 거기서 부터 재생
    private void playRestart(){
        playStatus = PLAY;

        service.setAction(ACTION_RESTART);
        startService(service);
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void prev(){
        if(position > 0) viewPager.setCurrentItem(position - 1);
    }
    private void next(){
        if(position < datas.size()) viewPager.setCurrentItem(position + 1);
    }


    // 함수 오버라이드해서 player 해제한다.
    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if(player!=null){
//            player.release();  // 사용이 끝나면 해제해야만 한다.
//        }
//
//        // 죽어 재생상태야!!
//        playStatus = STOP;
    }

    // handler로 하는방법
    // sub thread를 생성해서 mediaplayer의 현재 포지션 값으로 seekbar를 변경해준다. 매 밀리세컨마다
    // PLAY 상태 아래에서 해 주자..
    class TimerThread extends Thread {
        @Override
        public void run () {
            while (playStatus < STOP) {
                if (player != null) {
                    // 핸들러!
                    handler.sendEmptyMessage(PROGRESS_SET);
                    // 핸들러어어어
                }
                // 너무 상세하게 체크가 돌아가면 렉을 유발하기 때문에 1초텀줌
                try { Thread.sleep(1); } catch (InterruptedException e) { }
            }
        }
    }

}
