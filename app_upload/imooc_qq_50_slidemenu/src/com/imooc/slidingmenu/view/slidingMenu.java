package com.imooc.slidingmenu.view;

import com.example.imooc_qq_50_slidemenu.R;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class slidingMenu extends HorizontalScrollView {
	
	private LinearLayout mWapper;//
	private ViewGroup mMenu;//����
	private ViewGroup mContent;//����
	private int mScreenWidth;
	private int mMenuWidth;
	//�˵�����Ļ�Ҳ�ľ��뵥λdp
	private int mMenuRightPadding = 50;
	private boolean once = false;
	private boolean isOpen;
/*
 * δʹ���Զ��������ǵ���
 * */
	public slidingMenu(Context context, AttributeSet attrs) {
		//super(context, attrs);
		// TODO Auto-generated constructor stub
		this(context,attrs,0);
	}
	/*
	 * ʹ�����Զ�������ʱ����
	 * */
	public slidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		//��ȡ���Ƕ��������
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for(int i = 0;i<n;i++){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = a.getDimensionPixelSize(attr, 
						(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		a.recycle();
		WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics );
		mScreenWidth = outMetrics.widthPixels;
		//��dpת��Ϊpx
		//mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
	}

	public slidingMenu(Context context) {
		//super(context);
		// TODO Auto-generated constructor stub
		this(context,null);
	}


	/*
	 * ������view�Ŀ�͸�
	 * ����Ŀ�͸�
	 * */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if(!once){
		//ָ����view�Ŀ�͸�
		    mWapper = (LinearLayout) getChildAt(0);
		    mMenu = (ViewGroup) mWapper.getChildAt(0);
		    mContent = (ViewGroup) mWapper.getChildAt(1);
		
		    mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth-mMenuRightPadding;
		    mContent.getLayoutParams().width = mScreenWidth;
		    once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	/*
	 * ͨ������ƫ������menu����
	 * */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(changed){
			this.scrollTo(mMenuWidth, 0);
		}
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			//content��������ߵĿ��
			int scrollX = getScrollX();
			if(scrollX>=mMenuWidth/2){
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			}else{
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
			

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	/*
	 * �򿪲˵�
	 * */
	private void openMenu(){
		if(isOpen)return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}
	/*
	 * �رղ˵�
	 * */
    private void closeMenu(){
    	if(!isOpen)return;
    	this.smoothScrollTo(mMenuWidth, 0);
    	isOpen = false;
    }
    public void toggle(){
    	if(isOpen){
    		closeMenu();
    	}else{
    		openMenu();
    	}
    }
    /*��������ʱ��ȫ����*/
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	// TODO Auto-generated method stub
    	super.onScrollChanged(l, t, oldl, oldt);
    	//l��scrollX
    	float scale = l*1.0f/mMenuWidth;//1~0
    	//���Զ���
    	ViewHelper.setTranslationX(mMenu, mMenuWidth*scale*0.7f);
    	//���ݵĶ���
    	float rightscale = 0.7f+0.3f*scale;
    	//����content���ŵ����ĵ�
    	ViewHelper.setPivotX(mContent, 0);
    	ViewHelper.setPivotY(mContent, mContent.getHeight()/2);
    	ViewHelper.setScaleX(mContent, rightscale);
    	ViewHelper.setScaleY(mContent, rightscale);
    	
    	//�˵�����
    	float leftScale = 1.0f-scale*0.3f;
    	float leftAlpha = 0.6f+0.4f*(1-scale);
    	ViewHelper.setScaleX(mMenu, leftScale);
    	ViewHelper.setScaleY(mMenu, leftScale);
    	ViewHelper.setAlpha(mMenu, leftAlpha);
    }
}
