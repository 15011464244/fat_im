// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AddCommonActivity$$ViewInjector<T extends com.ems.express.ui.AddCommonActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427344, "field 'mBtnSave'");
    target.mBtnSave = finder.castView(view, 2131427344, "field 'mBtnSave'");
    view = finder.findRequiredView(source, 2131427343, "field 'mEtCommonWord'");
    target.mEtCommonWord = finder.castView(view, 2131427343, "field 'mEtCommonWord'");
    view = finder.findRequiredView(source, 2131427866, "field 'mTvInfo'");
    target.mTvInfo = finder.castView(view, 2131427866, "field 'mTvInfo'");
  }

  @Override public void reset(T target) {
    target.mBtnSave = null;
    target.mEtCommonWord = null;
    target.mTvInfo = null;
  }
}
