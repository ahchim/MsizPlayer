package com.ahchim.android.simplemusicplayer;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.ahchim.android.simplemusicplayer.App.PLAY;
import static com.ahchim.android.simplemusicplayer.App.playStatus;
import static com.ahchim.android.simplemusicplayer.App.position;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends Fragment {
    // Player 액티비티와 adapter를 공유하기 위해 static으로 선언했다.
    // 이게 최선인지 일단 고민해보자..
    public static MusicAdapter adapter;
    private final int REQ_PERMISSION = 100; // 권한 요청코드

    private View view;

    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 만든 뷰가 있으면 다시 뷰를 돌려줌.
        // savedInstanceState != null && view != null
        if(view != null){
            return view;
        } else {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_three, container, false);

            // 앱을 껐다가 플레이어가 실행중이면 일단 PlayerActivity로 이동한다.
            if(playStatus == PLAY){
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("position", position);

                startActivity(intent);

                //finish();
            } else {

                // 버전체크해서 마시멜로보다 낮으면 런타임권한 체크를 하지 않는다.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(PermissionControl.checkPermission(getActivity(), REQ_PERMISSION)){
                        init();
                    }
                } else {
                    init();
                }
            }

            return view;
        }
    }

    // 데이터를 로드할 함수
    private void init(){
        MessageUtil.toastShort(getContext(), "프로그램을 실행합니다.");

        listInit();
    }

    private void listInit(){
        // 리사이클러뷰 세팅
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new MusicAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
