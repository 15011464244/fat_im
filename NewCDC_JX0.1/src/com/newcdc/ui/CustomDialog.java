package com.newcdc.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newcdc.R;
import com.newcdc.application.BaseActivity;


/**
 * Dialog公用类
 * title ： Dialog标题
 * message：Dialog提示内容
 * */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;// 提示内容
		private String positiveButtonText;// 左边按钮
		private String negativeButtonText;// 右边按钮
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;// 左边按钮监听
		private DialogInterface.OnClickListener negativeButtonClickListener;// 右边按钮监听

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,
					R.style.dialogss);
			View layout = inflater.inflate(R.layout.dialog_userexit_layout,
					null);
			dialog.setContentView(layout, new LayoutParams(
					BaseActivity.width * 17 / 20, LayoutParams.WRAP_CONTENT));
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.info_cancel))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.info_cancel))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.info_cancel).setVisibility(View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.info_sure))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.info_sure))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.info_sure).setVisibility(View.GONE);
			}
			// set the content message
			if (title != null) {
				((TextView) layout.findViewById(R.id.title_dialog_userexit))
						.setText(title);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.title_dialog_userexit))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.title_dialog_userexit))
						.addView(contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			if (message != null) {
				((TextView) layout.findViewById(R.id.map_address))
						.setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.map_address))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.map_address)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
//			dialog.setContentView(layout);//这句去掉 否则就不能控制对话框的大小了 
			return dialog;
		}

	}
}
