/**
 * 
 */
package com.newcdc.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

/**
 * @author zhangfan 2015-1-28,下午6:06:02
 * 
 */
public class MyArrayAdapter extends ArrayAdapter<String> {

	/**
	 * @param context
	 * @param resource
	 * @param objects
	 */
	public MyArrayAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
	}

	@Override
	public Filter getFilter() {
		return new ArrayFilter();
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			if (prefix == null || prefix.length() == 0) {
				ArrayList<String> list = new ArrayList<String>();
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<String> values = new ArrayList<String>();

				final int count = values.size();
				final ArrayList<String> newValues = new ArrayList<String>();

				for (int i = 0; i < count; i++) {
					final String value = values.get(i);
					final String valueText = value.toString().toLowerCase();

					// First match against the whole, non-splitted value
					if (valueText.contains(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with
						// space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].contains(prefixString)) {
								newValues.add(value);
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
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

		}
	}
}
