package org.mariusconstantin.dashlanetest.fragments.addresseslist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.mariusconstantin.dashlanetest.DashlaneApp;
import org.mariusconstantin.dashlanetest.IAppConfig;
import org.mariusconstantin.dashlanetest.R;
import org.mariusconstantin.dashlanetest.data.models.IWebsiteModel;
import org.mariusconstantin.dashlanetest.activities.mainactivity.IMainActivityContract;
import org.mariusconstantin.dashlanetest.fragments.AbstractRecycleListFragment;
import org.mariusconstantin.dashlanetest.helpers.IPathHelper;
import org.mariusconstantin.dashlanetest.presenter.IContract;
import org.mariusconstantin.dashlanetest.recyclerview.VarColumnGridLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by Marius on 1/3/2016.
 */
public class WebAddressListFragment extends AbstractRecycleListFragment implements IWebAddressListContract.IView {
    private WebAddressListPresenter mPresenter;
    private static final String PAGE_ID_KEY = "page_id";
    private VerticalRecyclerViewFastScroller mFastScroller;

    public static WebAddressListFragment newInstance(long pageId) {
        final WebAddressListFragment fragment = new WebAddressListFragment();
        final Bundle bundle = new Bundle();
        bundle.putLong(PAGE_ID_KEY, pageId);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mAdapter == null)
            mAdapter = new AddressesAdapter(getActivity(), DashlaneApp.getInstance().getAppInjector().getAppConfig(), null, mItemListener, DashlaneApp.getInstance().getAppInjector().getPathHelper());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.addresses_list_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFastScroller = (VerticalRecyclerViewFastScroller) view.findViewById(R.id.fast_scroller);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        mFastScroller.setRecyclerView(mRecyclerView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        mRecyclerView.addOnScrollListener(mFastScroller.getOnScrollListener());

    }

    @Override
    public void onDestroyView() {
        mRecyclerView.removeOnScrollListener(mFastScroller.getOnScrollListener());
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new WebAddressListPresenter((IMainActivityContract.IPresenter) ((IContract.IView) getActivity()).getPresenter());
        mPresenter.onAttach(this);
        if (savedInstanceState == null || mAdapter.getItemCount() == 0) {
            setListShown(false, false);
            mPresenter.requestAdapterData(getArguments().getLong(PAGE_ID_KEY, 0));
        } else {
            setListShown(true, false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.onDetach(this);
    }

    @Override
    public void refreshAdapterData(List<IWebsiteModel> data) {
        ((AddressesAdapter) mAdapter).replaceData(data);
        setListShown(true, !isResumed());
    }

    @Override
    public void performFilter(String inputValue) {
        ((AddressesAdapter) mAdapter).getFilter().filter(inputValue);
    }

    @NonNull
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new VarColumnGridLayoutManager(getContext(), getResources().getDimensionPixelSize(R.dimen.min_item_width));
    }

    @Override
    protected boolean hasFixedSize() {
        return true;
    }

    private final AddressItemListener mItemListener = new AddressItemListener() {
        @Override
        public void onNoteClick(IWebsiteModel address) {
            mPresenter.getParent().navigateToAddressDetails(address, ((GridLayoutManager) mRecyclerView.getLayoutManager()).getSpanCount() > 1);
        }
    };

    private static class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> implements Filterable {

        private List<IWebsiteModel> mAddresses;
        private List<IWebsiteModel> mOriginalData;
        private final Object mLocker = new Object();
        private AddressItemListener mItemListener;
        private final WeakReference<Context> mContextWeakReference;
        private final IPathHelper mPathHelper;
        private final DataFilter mFilter = new DataFilter();
        private final IAppConfig mAppConfig;

        public AddressesAdapter(Context context,
                                IAppConfig appConfig,
                                List<IWebsiteModel> addresses,
                                AddressItemListener itemListener, IPathHelper pathHelper) {
            setList(addresses);
            mItemListener = itemListener;
            mContextWeakReference = new WeakReference<>(context);
            mPathHelper = pathHelper;
            mAppConfig = appConfig;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.web_address_item, parent, false);

            return new ViewHolder(noteView, mItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            IWebsiteModel address = mAddresses.get(position);
            viewHolder.title.setText(address.getTitle());
            final String subtitle = address.getSubtitle();
            final String logoId = address.getLogoId();
            // check to see if we are in MOCK - TESTING MODE
            if (!mAppConfig.isMock()) {
                if (!TextUtils.isEmpty(logoId)) {
                    final Context context = mContextWeakReference.get();
                    if (context != null) {
                        Picasso.with(context).load(mPathHelper.getLogoPath(logoId)).into(viewHolder.logo);
                    }
                } else {
                    // TODO: 1/5/2016 add a placeholder here
                }
            } else {

                viewHolder.logo.setImageResource(R.mipmap.ic_launcher);
            }
            viewHolder.description.setText(TextUtils.isEmpty(subtitle) ? "" : subtitle);
        }

        public void replaceData(List<IWebsiteModel> addresses) {
            setList(addresses != null ? addresses : new ArrayList<IWebsiteModel>());
            notifyDataSetChanged();
        }

        private void setList(@NonNull List<IWebsiteModel> addresses) {
            synchronized (mLocker) {
                mOriginalData = addresses;
                mAddresses = addresses;
            }
        }

        @Override
        public int getItemCount() {
            return mAddresses != null ? mAddresses.size() : 0;
        }

        public IWebsiteModel getItem(int position) {
            return mAddresses.get(position);
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView title;
            public TextView description;
            public ImageView logo;
            private AddressItemListener mItemListener;

            public ViewHolder(View itemView, AddressItemListener listener) {
                super(itemView);
                mItemListener = listener;
                title = (TextView) itemView.findViewById(android.R.id.text1);
                description = (TextView) itemView.findViewById(android.R.id.text2);
                logo = (ImageView) itemView.findViewById(R.id.image);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                IWebsiteModel address = getItem(position);
                mItemListener.onNoteClick(address);

            }
        }

        private class DataFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();
                synchronized (mLocker) {
                    if (mOriginalData == null) {

                        mOriginalData = new ArrayList<>(mAddresses);

                    }
                }
                if (prefix == null || prefix.length() == 0) {
                    ArrayList<IWebsiteModel> list;
                    synchronized (mLocker) {
                        list = new ArrayList<>(mOriginalData);
                    }
                    results.values = list;
                    results.count = list.size();
                } else {
                    String prefixString = prefix.toString().toLowerCase();

                    ArrayList<IWebsiteModel> values;
                    synchronized (mLocker) {
                        values = new ArrayList<>(mOriginalData);
                    }

                    final int count = values.size();
                    final List<IWebsiteModel> newValues = new ArrayList<>();

                    for (int i = 0; i < count; i++) {
                        final IWebsiteModel model = values.get(i);
                        final String toCompare = model.getTitle().toLowerCase();
                        // First match against the whole, non-splitted value
                        if (toCompare.startsWith(prefixString)) {
                            newValues.add(model);
                        } else {
                            final String[] words = toCompare.split(" ");
                            final int wordCount = words.length;

                            // Start at index 0, in case valueText starts with space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    newValues.add(model);
                                    break;
                                }
                            }
                        }

                    }

                    results.values = newValues;
                    results.count = newValues.size();

                }

                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                synchronized (mLocker) {
                    mAddresses = (List<IWebsiteModel>) results.values;
                }
                notifyDataSetChanged();
            }

        }
    }

    public interface AddressItemListener {

        void onNoteClick(IWebsiteModel address);
    }
}
