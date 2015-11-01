// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LoginActivity$$ViewInjector<T extends com.ems.express.ui.LoginActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427409, "field 'mTabLogin' and method 'toTabLogin'");
    target.mTabLogin = finder.castView(view, 2131427409, "field 'mTabLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toTabLogin();
        }
      });
    view = finder.findRequiredView(source, 2131427381, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427381, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427420, "field 'mRegistPW'");
    target.mRegistPW = finder.castView(view, 2131427420, "field 'mRegistPW'");
    view = finder.findRequiredView(source, 2131427425, "field 'mRegistTip'");
    target.mRegistTip = finder.castView(view, 2131427425, "field 'mRegistTip'");
    view = finder.findRequiredView(source, 2131427412, "field 'mLoginPhone'");
    target.mLoginPhone = finder.castView(view, 2131427412, "field 'mLoginPhone'");
    view = finder.findRequiredView(source, 2131427413, "field 'mLoginPW'");
    target.mLoginPW = finder.castView(view, 2131427413, "field 'mLoginPW'");
    view = finder.findRequiredView(source, 2131427418, "field 'mRegistPhone'");
    target.mRegistPhone = finder.castView(view, 2131427418, "field 'mRegistPhone'");
    view = finder.findRequiredView(source, 2131427416, "field 'mRegistContainer'");
    target.mRegistContainer = finder.castView(view, 2131427416, "field 'mRegistContainer'");
    view = finder.findRequiredView(source, 2131427410, "field 'mTabRegister' and method 'toTabRegist'");
    target.mTabRegister = finder.castView(view, 2131427410, "field 'mTabRegister'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toTabRegist();
        }
      });
    view = finder.findRequiredView(source, 2131427422, "field 'mRegistPWAgain'");
    target.mRegistPWAgain = finder.castView(view, 2131427422, "field 'mRegistPWAgain'");
    view = finder.findRequiredView(source, 2131427424, "field 'mInvitedPhoneNumber'");
    target.mInvitedPhoneNumber = finder.castView(view, 2131427424, "field 'mInvitedPhoneNumber'");
    view = finder.findRequiredView(source, 2131427411, "field 'mLoginContainer'");
    target.mLoginContainer = finder.castView(view, 2131427411, "field 'mLoginContainer'");
    view = finder.findRequiredView(source, 2131427426, "method 'toRegist'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toRegist();
        }
      });
    view = finder.findRequiredView(source, 2131427408, "method 'toBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toBack();
        }
      });
    view = finder.findRequiredView(source, 2131427414, "method 'toForgetPW'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toForgetPW();
        }
      });
    view = finder.findRequiredView(source, 2131427415, "method 'toLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toLogin();
        }
      });
  }

  @Override public void reset(T target) {
    target.mTabLogin = null;
    target.mTitle = null;
    target.mRegistPW = null;
    target.mRegistTip = null;
    target.mLoginPhone = null;
    target.mLoginPW = null;
    target.mRegistPhone = null;
    target.mRegistContainer = null;
    target.mTabRegister = null;
    target.mRegistPWAgain = null;
    target.mInvitedPhoneNumber = null;
    target.mLoginContainer = null;
  }
}
