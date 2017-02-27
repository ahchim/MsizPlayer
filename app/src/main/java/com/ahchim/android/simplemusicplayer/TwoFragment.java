package com.ahchim.android.simplemusicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment implements View.OnClickListener {

    private View view;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 만든 뷰가 있으면 다시 뷰를 돌려줌.
        // savedInstanceState != null && view != null
        if(view != null){
            // preview.setText("0");
            return view;
        } else {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_two, container, false);


            return view;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
