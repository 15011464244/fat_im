// Generated code from Butter Knife. Do not modify!
package com.ems.express.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class TimeQueryActivity$$ViewInjector<T extends com.ems.express.ui.TimeQueryActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427604, "field 'mDatePicker'");
    target.mDatePicker = finder.castView(view, 2131427604, "field 'mDatePicker'");
    view = finder.findRequiredView(source, 2131427598, "field 'mSelecteDate'");
    target.mSelecteDate = finder.castView(view, 2131427598, "field 'mSelecteDate'");
    view = finder.findRequiredView(source, 2131427600, "field 'mDateSelectionView'");
    target.mDateSelectionView = view;
    view = finder.findRequiredView(source, 2131427378, "field 'mTvTime'");
    target.mTvTime = finder.castView(view, 2131427378, "field 'mTvTime'");
    view = finder.findRequiredView(source, 2131427599, "field 'mDateArrowView'");
    target.mDateArrowView = finder.castView(view, 2131427599, "field 'mDateArrowView'");
    view = finder.findRequiredView(source, 2131427601, "field 'mTvDate'");
    target.mTvDate = finder.castView(view, 2131427601, "field 'mTvDate'");
    view = finder.findRequiredView(source, 2131427603, "field 'mTvTimeLine'");
    target.mTvTimeLine = finder.castView(view, 2131427603, "field 'mTvTimeLine'");
    view = finder.findRequiredView(source, 2131427761, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427761, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427602, "field 'mTvDateLine'");
    target.mTvDateLine = finder.castView(view, 2131427602, "field 'mTvDateLine'");
    view = finder.findRequiredView(source, 2131427605, "field 'mTimePicker'");
    target.mTimePicker = finder.castView(view, 2131427605, "field 'mTimePicker'");
  }

  @Override public void reset(T target) {
    target.mDatePicker = null;
    target.mSelecteDate = null;
    target.mDateSelectionView = null;
    target.mTvTime = null;
    target.mDateArrowView = null;
    target.mTvDate = null;
    target.mTvTimeLine = null;
    target.mTitle = null;
    target.mTvDateLine = null;
    target.mTimePicker = null;
  }
}
