// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ResetPWActivity$$ViewInjector<T extends com.ems.express.ui.ResetPWActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427557, "field 'mTVauthCode' and method 'getAuthCode'");
    target.mTVauthCode = finder.castView(view, 2131427557, "field 'mTVauthCode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getAuthCode();
        }
      });
    view = finder.findRequiredView(source, 2131427553, "field 'mPhone'");
    target.mPhone = finder.castView(view, 2131427553, "field 'mPhone'");
    view = finder.findRequiredView(source, 2131427556, "field 'mETAuthCode'");
    target.mETAuthCode = finder.castView(view, 2131427556, "field 'mETAuthCode'");
    view = finder.findRequiredView(source, 2131427559, "field 'mPW'");
    target.mPW = finder.castView(view, 2131427559, "field 'mPW'");
    view = finder.findRequiredView(source, 2131427561, "method 'toCommit'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toCommit();
        }
      });
  }

  @Override public void reset(T target) {
    target.mTVauthCode = null;
    target.mPhone = null;
    target.mETAuthCode = null;
    target.mPW = null;
  }
}
