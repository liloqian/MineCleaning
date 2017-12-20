package com.leo.minecleaning.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.leo.minecleaning.R;
import com.leo.minecleaning.util.Constans;
import com.leo.minecleaning.util.SharePreferenceUtil;

/**
 *  @author leo
 * */
public class ConfigActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ConfigActivity";

    private Toolbar mToolbar;
    private Button mBtnGridSize;
    private Button mBtnMineNumbers;
    private SharePreferenceUtil sp ;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,ConfigActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mToolbar = (Toolbar) findViewById(R.id.myToolbar);
        mBtnGridSize = (Button) findViewById(R.id.btn_set_gridNumbers);
        mBtnMineNumbers = (Button) findViewById(R.id.btn_set_mineNumbers);
        Button mBtnOk = (Button) findViewById(R.id.btn_setOk);
        Button mBtnCancel = (Button) findViewById(R.id.btn_setCancel );

        initToolBar();

        sp = SharePreferenceUtil.getInstance();
        setButtonText();

        mBtnCancel.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
        mBtnGridSize.setOnClickListener(this);
        mBtnMineNumbers.setOnClickListener(this);
    }

    private void setButtonText() {
        String gridSize = Constans.GRID_SIZE[sp.getInt(SharePreferenceUtil.SP_GRID_NUMBERS,0)];
        String minePercent = Constans.MINE_PERCENT[sp.getInt(SharePreferenceUtil.SP_MINE_NUMBERS,0)];
        mBtnGridSize.setText(gridSize);
        mBtnMineNumbers.setText(minePercent);
    }

    private void initToolBar() {
        mToolbar.setTitle("配置参数");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder;
        switch (v.getId()){
            case R.id.btn_setCancel:
                this.finish();
                break;
            case R.id.btn_setOk:
                saveConfig();
                this.finish();
                break;
            case R.id.btn_set_gridNumbers:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("选择网格大小");
                builder.setItems(Constans.GRID_SIZE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBtnGridSize.setText(Constans.GRID_SIZE[which]);
                    }
                });
                builder.create().show();
                break;
            case R.id.btn_set_mineNumbers:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("选择游戏难度");
                builder.setItems(Constans.MINE_PERCENT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBtnMineNumbers.setText(Constans.MINE_PERCENT[which]);
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }
    }

    /**
     * 设置全部保存到sp中
     * */
    private void saveConfig() {
        String gridSize = (String) mBtnGridSize.getText();
        for(int i = 0 ; i < Constans.GRID_SIZE.length ;i++){
            if(gridSize.equals(Constans.GRID_SIZE[i])){
                sp.putInt(SharePreferenceUtil.SP_GRID_NUMBERS,i);
                break;
            }
        }
        String minePercent = (String) mBtnMineNumbers.getText();
        for(int i = 0 ; i < Constans.MINE_PERCENT.length ;i++){
            if(minePercent.equals(Constans.MINE_PERCENT[i])){
                sp.putInt(SharePreferenceUtil.SP_MINE_NUMBERS,i);
            }
        }
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
    }
}
