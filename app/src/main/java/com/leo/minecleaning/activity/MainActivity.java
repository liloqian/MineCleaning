package com.leo.minecleaning.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.leo.minecleaning.R;
import com.leo.minecleaning.ui.GameView;
import com.leo.minecleaning.util.Constans;

/**
 * Created by leo on 2017/12/15.
 * @author leo
 * */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Toolbar mToolbar;
    private TextView mTvMineAll;
    private static TextView mTvScore;
    private FrameLayout flMain;
    private GameView mGameView;
    private static int mScore = 0;

    public static Handler sHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mScore = 0;
                    mTvScore.setText("得分: "+mScore);
                    break;
                default:
                    mTvScore.setText("得分: "+(++mScore));
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.myToolbar);
        Button mBtnOptions = (Button) findViewById(R.id.btn_options);
        Button mBtnRefresh = (Button) findViewById(R.id.btn_refresh);
        mTvMineAll = (TextView) findViewById(R.id.tv_mine_all);
        mTvScore = (TextView) findViewById(R.id.tv_score);
        flMain = (FrameLayout) findViewById(R.id.fl_main);

        mBtnRefresh.setOnClickListener(this);
        mBtnOptions.setOnClickListener(this);

        initToolbar();

        mGameView = new GameView(this);
        flMain.addView(mGameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //获取游戏界面到手机顶端的距离
        int[] location = new int[2];
        flMain.getLocationOnScreen(location);
        Constans.MAIN_VIEW_DISTACE_PHONETOP = location[1];
    }

    private void initToolbar() {
        mToolbar.setTitle("MineCleaning");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_options:
                ConfigActivity.startActivity(this);
                break;
            case R.id.btn_refresh:
                initView();
                break;
            default:
                break;
        }
    }

    private void initView(){
        mGameView.initView();
        mTvMineAll.setText("类的总数: "+mGameView.getmMineNumbers());
        mScore = 0;
    }

}
