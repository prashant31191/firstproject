package com.qinnuan.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.showu.baogu.R;
import com.qinnuan.engine.adapter.EmojiViewFaceAdapter;
import com.qinnuan.engine.adapter.GridViewFaceAdapter;
import com.qinnuan.common.util.EmojiParser;
import com.qinnuan.common.util.FaceParser;
import com.qinnuan.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaceGridView extends LinearLayout {
	
	public GridView gridView;
	public Button mBtnInput, mBtnNormal, mBtnEmoji, btnDelete;

	private  InputMethodManager imm;
	private EmojiParser mParser;
	private Resources resources;
	private int FACE_EMOJI = 1;
	private int FACE_NORMAL = 2;
	private int type = FACE_NORMAL;
	private Context context;
	private EditText editText;
	List<Face> faces = new ArrayList<Face>();
	public FaceGridView(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	public FaceGridView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		this.context = context;
		init(context);
	}
	
	public void bindEditText(EditText editText) {
		this.editText = editText;
		gridView.setOnItemClickListener(itemClickListener);
	}
	
	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			SpannableString ss = new SpannableString(arg1.getTag().toString());
			Drawable d = (Drawable) getFaceAdapter(context).getItem(arg2);
			d.setBounds(0, 0, 32, 32);//设置表情图片的显示大小
			ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
			ss.setSpan(span, 0, arg1.getTag().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			//在光标所在处插入表情
			editText.getText().insert(editText.getSelectionStart(), ss);
		}
	};
	
	public void init(final Context context) {
		resources = context.getResources();
		mParser = EmojiParser.getInstance(context);
		mParser.readMap(context);
		faces = new FaceParser().readMap(context);
		imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE); 
		LayoutInflater.from(context).inflate(R.layout.widget_face_grid, this, true);
		gridView = (GridView) findViewById(R.id.gv_face);
		GridViewFaceAdapter adapter = new GridViewFaceAdapter(context, faces);
//		EmojiViewFaceAdapter adapter = new EmojiViewFaceAdapter(context, imageIds );
		gridView.setAdapter(adapter);
		mBtnInput = (Button) findViewById(R.id.btn_input);
		mBtnNormal = (Button) findViewById(R.id.btn_normal_face);
		//mBtnNormal.setBackgroundResource(R.drawable.btn_face);
		mBtnNormal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Drawable drawable = getResources().getDrawable(R.drawable.face_tip_icon);
                drawable.setBounds(0, 0, 25, 14);
                mBtnNormal.setCompoundDrawables(null,drawable, null, null);
                mBtnEmoji.setCompoundDrawables(null,null,null,null);
               // b.setCompoundDrawables(null,getResources().getDrawable(R.drawable.face_tip_icon),null,null);
				btnNormalPressed();
			}
		});
		
		mBtnEmoji = (Button) findViewById(R.id.btn_emoji_face);
		mBtnEmoji.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Drawable drawable = getResources().getDrawable(R.drawable.face_tip_icon);
                drawable.setBounds(0, 0, 25, 14);
                mBtnEmoji.setCompoundDrawables(null,drawable, null, null);
                mBtnNormal.setCompoundDrawables(null,null,null,null);
				btnEmojiPressed();
			}
		});
		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LogUtil.e(getClass(), "Content:" + editText.getText().toString());
                if(editText!=null){
				editText.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
                }
			}
		});
		mBtnInput.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FaceGridView.this.setVisibility(View.GONE);
				imm.showSoftInput(editText, 1);
			}
		});
	}
	
	private void btnNormalPressed() {
		GridViewFaceAdapter adapter = new GridViewFaceAdapter(context, faces);
		gridView.setAdapter(adapter);
		type = FACE_NORMAL;
	}
	
	private void btnEmojiPressed() {
		ArrayList<String> paths = getAllEmojiFacePath(context);
		EmojiViewFaceAdapter adapter = adapter = new EmojiViewFaceAdapter(context, paths);;
		gridView.setAdapter(adapter);
		type = FACE_EMOJI;
		mBtnNormal.setBackgroundDrawable(null);
	}

	final KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
	
	private ArrayList<String> getAllEmojiFacePath(Context context) {
		HashMap<String, ArrayList<String>> hashMap = mParser.getEmoMap();
		ArrayList<String> peoples = hashMap.get("people");
		ArrayList<String> paths = new ArrayList<String>();
		for (int i = 0; i < peoples.size(); i++) {
			paths.add("emoticon/emoji_" + peoples.get(i) + ".png");
		}
		return paths;
	}
	
	public String getTagByPostion(int postion) {
		mParser = EmojiParser.getInstance(context);
		HashMap<String, ArrayList<String>> hashMap = mParser.getEmoMap();
		ArrayList<String> peoples = hashMap.get("people");
		return "emoji_" + peoples.get(postion);
	}
	
	public BaseAdapter getFaceAdapter(Context context) {
		BaseAdapter adapter = null;
		if (type == FACE_EMOJI) {
			ArrayList<String> paths = getAllEmojiFacePath(context);
			//int[] imageIds = getAllEmojiFacePath(context);
			adapter = new EmojiViewFaceAdapter(context, paths);
		} else {
			adapter = new GridViewFaceAdapter(context, faces);
		}
		return adapter;
	}
	
	public void setGridViewHide() {
		if(gridView != null) {
			gridView.setVisibility(View.GONE);
		}
	}


}
