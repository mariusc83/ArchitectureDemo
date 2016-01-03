package org.mariusconstantin.dashlanetest.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mariusconstantin.dashlanetest.R;

public abstract class AbstractRecycleListFragment extends Fragment implements ViewStub.OnInflateListener {

    protected Button mGoShoppingButton;

    protected View container;
    protected ProgressBar mProgressBar;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mAdapter;
    protected TextView mEmptyViewTitle;
    protected TextView mEmptyViewSubtitle;
    protected RecyclerView.LayoutManager mLayoutManager;
    private ViewStub mEmptyViewStub;
    private View mEmptyView;


    // life-cycle events
    protected AbstractRecycleListFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Activity activity = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = view.findViewById(R.id.main_container);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mEmptyViewStub = (ViewStub) view.findViewById(R.id.empty_view_stub);
        mLayoutManager = getLayoutManager();
        mRecyclerView.setHasFixedSize(hasFixedSize());
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(dataSetObserver);
            mRecyclerView.setAdapter(mAdapter);
        }

        if (mEmptyViewStub != null) {
            mEmptyViewStub.setOnInflateListener(this);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(dataSetObserver);
        }
        mRecyclerView.setAdapter(null);
        container = null;
        mProgressBar = null;
        mRecyclerView = null;
        mEmptyViewTitle = null;
        mEmptyViewSubtitle = null;
        mGoShoppingButton = null;
        mEmptyViewStub = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NonNull
    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract boolean hasFixedSize();

    protected void setListShown(boolean shown, boolean animate) {
        if (mProgressBar == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        if (shown) {
            if (animate) {
                mProgressBar.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                container.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                mProgressBar.clearAnimation();
                container.clearAnimation();
            }
            mProgressBar.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressBar.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                container.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                mProgressBar.clearAnimation();
                container.clearAnimation();
            }
            mProgressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
        }
    }

    // 	actions
    protected void setEmptyShown(boolean isShown) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(isShown ? View.VISIBLE : View.GONE);
        }
    }

    protected void setEmptyView(final int viewId) {
        if (mEmptyViewStub != null) {
            mEmptyViewStub.setLayoutResource(viewId);
            mEmptyViewStub.inflate();
        }
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @VisibleForTesting
    public TextView getEmptyViewTitle() {
        return mEmptyViewTitle;
    }

    @VisibleForTesting
    public TextView getEmptyViewSubtitle() {
        return mEmptyViewSubtitle;
    }

    public View getContainer() {
        return container;
    }

    public ProgressBar getmProgressBar() {
        return mProgressBar;
    }

    //	handlers
    @Override
    public void onInflate(ViewStub stub, View inflated) {
        if (inflated != null) {
            mEmptyView = inflated;
            mGoShoppingButton = (Button) inflated.findViewById(android.R.id.button1);
        }

    }

    private final RecyclerView.AdapterDataObserver dataSetObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (mAdapter != null) {
                setEmptyShown(mAdapter.getItemCount() == 0);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
        }
    };

}