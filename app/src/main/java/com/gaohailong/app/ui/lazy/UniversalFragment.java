package com.gaohailong.app.ui.lazy;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.gaohailong.app.R;
import com.gaohailong.app.base.LazyFragment;

/**
 * Created by gaohailong on 2018/3/2.
 */

public class UniversalFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private TextView title;
    private String text;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_universal_layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = getArguments().getString("title");
    }

    @Override
    protected void initView(View view) {
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        title = view.findViewById(R.id.title);

        refreshLayout.setOnRefreshListener(this);
    }

    public static UniversalFragment getInstance(String title){
        UniversalFragment fragment = new UniversalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        refreshLayout.setRefreshing(true);
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                title.setText(text);
            }
        }, 1500);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
