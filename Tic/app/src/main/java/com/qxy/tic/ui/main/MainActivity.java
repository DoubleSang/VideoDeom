package com.qxy.tic.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.qxy.tic.Util.CusVideoView;
import com.qxy.tic.Util.CustomLayoutManager;
import com.qxy.tic.Util.JsonUti;
import com.qxy.tic.R;
import com.qxy.tic.Util.OnPageSlideListener;
import com.qxy.tic.adapter.VideoAdapter;
import com.qxy.tic.bean.Bean;

import org.json.JSONArray;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class MainActivity extends AppCompatActivity {

    private List<Bean.ItemListBean> datas;
    private CustomLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private VideoAdapter mAdapter;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        initView();
        initListener();
    }

    //初始化监听
    private void initListener() {
        mLayoutManager.setOnPageSlideListener(new OnPageSlideListener() {
            @Override
            public void onPageRelease(boolean isNext, int position) {
                int index;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isNext) {
                playVideo();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    private void playVideo() {
        View itemView = recyclerView.getChildAt(0);
        final CusVideoView mVideoView = itemView.findViewById(R.id.mVideoView);
        mVideoView.startPreloading(); //开始预加载，加载完等待播放
        mVideoView.startVideoAfterPreloading(); //如果预加载完会开始播放，如果未加载则开始加载
        mVideoView.startVideo();

    }

    private void releaseVideo(int index) {
        View itemView = recyclerView.getChildAt(index);
        final CusVideoView mVideoView = itemView.findViewById(R.id.mVideoView);
        final ImageView mThumb = itemView.findViewById(R.id.mThumb);
        JzvdStd.goOnPlayOnPause();
        mThumb.animate().alpha(1).start();
    }

    //初始化View
    private void initView() {
        recyclerView = findViewById(R.id.RecyclerView);
        mLayoutManager = new CustomLayoutManager(this, OrientationHelper.VERTICAL, false);
        Fix();
        Log.e("TAG",datas.size()+"");
        mAdapter = new VideoAdapter(this, datas);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }


    private void Fix() {
        String json = JsonUti.getJson(this,"test.json");
        Log.e("test",json);
        Bean bean=new Gson().fromJson(json,Bean.class);
        datas = bean.getItemList();
    }

}