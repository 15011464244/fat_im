// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PriceActivity$$ViewInjector<T extends com.ems.express.ui.PriceActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427532, "field 'mTvFinalPrice'");
    target.mTvFinalPrice = finder.castView(view, 2131427532, "field 'mTvFinalPrice'");
    view = finder.findRequiredView(source, 2131427531, "field 'mTvProduct'");
    target.mTvProduct = finder.castView(view, 2131427531, "field 'mTvProduct'");
    view = finder.findRequiredView(source, 2131427530, "field 'mLayoutResult'");
    target.mLayoutResult = view;
    view = finder.findRequiredView(source, 2131427437, "field 'mTvWeight'");
    target.mTvWeight = finder.castView(view, 2131427437, "field 'mTvWeight'");
    view = finder.findRequiredView(source, 2131427510, "field 'mMailTypeSelection'");
    target.mMailTypeSelection = finder.castView(view, 2131427510, "field 'mMailTypeSelection'");
    view = finder.findRequiredView(source, 2131427528, "field 'mEtWeight'");
    target.mEtWeight = finder.castView(view, 2131427528, "field 'mEtWeight'");
  }

  @Override public void reset(T target) {
    target.mTvFinalPrice = null;
    target.mTvProduct = null;
    target.mLayoutResult = null;
    target.mTvWeight = null;
    target.mMailTypeSelection = null;
    target.mEtWeight = null;
  }
}
