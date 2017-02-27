package com.ahchim.android.simplemusicplayer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ahchim.android.simplemusicplayer.util.fragment.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int TAB_COUNT = 5;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OneFragment one;
    private TwoFragment two;
    private ThreeFragment three;
    private FourFragment four;
    private FiveFragment five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 화면의 툴바 가져오기
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 플로팅 버튼 설정
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 리스트 섞기
                setShuffle();
            }
        });

        // 네비 드로워 설정
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ////

        // 컨텐트 영역 설정
        // 1. 탭 레이아웃
        // 탭 Layout 정의
        tabLayout = (TabLayout) findViewById(R.id.tab);
        if(TAB_COUNT > 5){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);  // 가로축 스크롤하기
        } else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        // 탭 생성 및 타이틀 입력
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_album)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_artist)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_folder)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_genre)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_music)));

        // 2. 뷰페이저
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 아답터 설정 필요
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        // 프래그먼트 초기화
        one = new OneFragment();
        two = new TwoFragment();
        three = new ThreeFragment();
        four = new FourFragment();
        five = new FiveFragment();  // 미리 정해진 그리드 가로축 개수
        adapter.add(one);
        adapter.add(two);
        adapter.add(three);
        adapter.add(four);
        adapter.add(five);

        viewPager.setAdapter(adapter);

        // 1. 페이저 리스너: 페이저가 변경되었을 때 탭을 바꿔주는 리스너 (add라 리스너 추가가능함)
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // 2. 탭이 변경되었을 때 페이지를 바꿔주는 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    // 리스트 섞기
    public void setShuffle(){
        // TODO : 구현합시다
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings:
                Toast.makeText(this, "설정이 선택되었습니다!!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_mylist:
                Toast.makeText(this, "나의 플레이이리스트가 선택되었습니다!!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_search:
                Toast.makeText(this, "검색이 선택되었습니다!!", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 네비게이션 드로어 메뉴가 onClick 되면 호출
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_album:
                viewPager.setCurrentItem(0);
                break;
            case R.id.nav_artist:
                viewPager.setCurrentItem(1);
                break;
            case R.id.nav_folder:
                viewPager.setCurrentItem(2);
                break;
            case R.id.nav_genre:
                viewPager.setCurrentItem(3);
                break;
            case R.id.nav_music:
                viewPager.setCurrentItem(4);
                break;
            case R.id.action_mylist:
                break;
            case R.id.action_search:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
