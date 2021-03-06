package com.vpaliy.mvp.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.vpaliy.mvp.App;
import com.vpaliy.mvp.R;
import com.vpaliy.mvp.view.fragment.UsersFragment;
import com.vpaliy.mvp.view.utils.Constant;
import com.vpaliy.mvp.view.utils.eventBus.ExternalAction;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import com.squareup.otto.Subscribe;
import com.vpaliy.mvp.view.wrapper.TransitionWrapper;

public class MainActivity extends BaseActivity{

    private static final String TAG=MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState==null) {
            setUpFragment();
        }
    }

    private void setUpFragment() {
        FragmentManager manager=getSupportFragmentManager();
        Fragment booksFragment=manager.findFragmentByTag(Constant.BOOKS_FRAGMENT);
        if(booksFragment!=null) {
            manager.beginTransaction()
                    .replace(R.id.fragment,booksFragment,Constant.BOOKS_FRAGMENT)
                    .commit();
        }else {
            Fragment usersFragment=manager.findFragmentByTag(Constant.USERS_FRAGMENT);
            if(usersFragment==null) {
                usersFragment=new UsersFragment();
            }
            manager.beginTransaction()
                    .add(R.id.fragment,usersFragment,Constant.USERS_FRAGMENT)
                    .commit();
        }
    }

    @Subscribe
    public void catchAction(@NonNull ExternalAction<?> action) {
        switch (action.getActionCode()) {
            case Constant.SWAP_TO_BOOKS:
            case Constant.SWAP_TO_USERS:
                swapAction(action.getActionCode());
                break;
            case Constant.ADD_BOOK:
            case Constant.ADD_USER:
                addAction(action.getActionCode());
                break;
            case Constant.BOOK_DETAILS:
            case Constant.USER_DETAILS:
                viewDetails(action);
                break;

        }
    }

    private void swapAction(int code) {

    }

    private void addAction(int code) {
        navigator.navigateToRegistration(this,code);
    }

    private void viewDetails(@NonNull ExternalAction<?> action) {
        TransitionWrapper wrapper=TransitionWrapper.class.cast(action.getData());
        navigator.navigateToDetails(this,wrapper,action.getActionCode());
    }

    @Override
    void register() {
        eventBus.register(this);
    }

    @Override
    void unregister() {
        eventBus.unregister(this);
    }

    @Override
    void initializeInjector() {
        App.app().provideAppComponent().inject(this);
    }


}
