package com.showu.common.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/** 限制输入字数的类 <br/>
 *  JingHui Liu <br/>
 */
public class FontCountLimit {

	private EditText mEditText;
	private TextView mShowCountTV;
	private int MAX_NUM_TEXT;
	/** editText EditText 输入框 EditText, 不能为null<br/>
	 *  showCountTV TextView 显示剩余字数的textView，可以为null <br/>
	 */
	public FontCountLimit(EditText editText, TextView showCountTV) {
		mEditText = editText;
		mShowCountTV = showCountTV;
	}
	
	/** 监听editext的字数 <br/>
	 *  fontCount 要限制的字数
	 */
	public void setOnEditTextListener(int fontCount) {
		MAX_NUM_TEXT = fontCount;
		mEditText.addTextChangedListener(new TextWatcher() {

			private CharSequence contentTemp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				contentTemp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int CURRENT_NUM_TEXT = MAX_NUM_TEXT - s.length();
				if(mShowCountTV != null) {
					mShowCountTV.setText(CURRENT_NUM_TEXT + "");
				}
				selectionStart = mEditText.getSelectionStart();
				selectionEnd = mEditText.getSelectionEnd();
				if (contentTemp.length() > MAX_NUM_TEXT) {
					s.delete(selectionStart - 1, selectionEnd);
					int selectionTemp = selectionEnd;
					mEditText.setText(s);
					mEditText.setSelection(selectionTemp);
				}
			}
		});
	}
}
