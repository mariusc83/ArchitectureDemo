package org.mariusconstantin.dashlanetest.activities.mainactivity;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import org.mariusconstantin.dashlanetest.R;
import org.mariusconstantin.dashlanetest.presenter.IContract;

public class MainActivity extends AppCompatActivity implements IContract.IView {
    private MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainActivityPresenter(this.getBaseContext(), this);
        mPresenter.onCreate(this, savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        final SearchView sv = new SearchView(getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.filterWebAddresses(newText);
                return true;
            }
        });
        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.filterWebAddresses(sv.getQuery().toString());
            }
        });

        return true;
    }

    @Override
    public <T extends Fragment> T switchToFragment(T fragment) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        final Fragment prevFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (prevFragment != null) {
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
        }
        else {
            fragmentTransaction.add(R.id.fragment_container, fragment);
        }

        fragmentTransaction.commit();
        return fragment;
    }

    @Override
    public <T extends DialogFragment> T showDialogFragment(T fragment) {
        fragment.show(getSupportFragmentManager(),null);
        return fragment;
    }

    @Override
    public IContract.IContainerPresenter getPresenter() {
        return mPresenter;
    }
}
