package com.phoenix.lib.app;

import android.view.View;

/**
 * date: 11/11/2014
 *
 * @author Dylan
 */
public interface IBaseView {
    int getLayoutId();

    <T extends View> T findView(int id);
}
