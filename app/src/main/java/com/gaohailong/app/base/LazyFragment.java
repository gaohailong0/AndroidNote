package com.gaohailong.app.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by gaohailong on 2018/3/2.
 */

public abstract class LazyFragment extends Fragment {

    private boolean isVisibleToUser;
    private boolean isPrepareView;
    private boolean isInitData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        return view;
    }


    /**
     * 当fragment由可见变为不可见和不可见变为可见时回调
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser保存页面对用户的可见状态
        this.isVisibleToUser = isVisibleToUser;

        lazyLoadData();
    }

    /**
     * View创建完成后调用
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepareView = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lazyLoadData();
    }

    private void lazyLoadData(){
        if (isVisibleToUser && isPrepareView && !isInitData){
            isInitData = true;
            initData();
        }
    }

    //布局
    protected abstract int getLayoutId();

    //初始化View
    protected abstract void initView(View view);

    //由子类实现，在这里操作数据加载的逻辑
    public abstract void initData();
}
