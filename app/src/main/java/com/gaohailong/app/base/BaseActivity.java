package com.gaohailong.app.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by gaohailong on 2018/3/2.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(getLayoutId());

        initView();

        initEvent();
    }

    //加载布局前，设置状态栏属性
    protected void setStatusBar() {}

    //布局
    protected abstract int getLayoutId();

    //初始化View
    protected abstract void initView();

    //添加事件
    protected void initEvent() {}

}
