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
	private ViewGroup mMenu;//子类
	private ViewGroup mContent;//子类
	private int mScreenWidth;
	private int mMenuWidth;
	//菜单到屏幕右侧的距离单位dp
	private int mMenuRightPadding = 50;
	private boolean once = false;
	private boolean isOpen;
/*
 * 未使用自定义属性是调用
 * */
	public slidingMenu(Context context, AttributeSet attrs) {
		//super(context, attrs);
		// TODO Auto-generated constructor stub
		this(context,attrs,0);
	}
	/*
	 * 使用了自定义属性时调用
	 * */
	public slidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		//获取我们定义的属性
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
		//把dp转化为px
		//mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
	}

	public slidingMenu(Context context) {
		//super(context);
		// TODO Auto-generated constructor stub
		this(context,null);
	}


	/*
	 * 设置子view的宽和高
	 * 自身的宽和高
	 * */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if(!once){
		//指定子view的宽和高
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
	 * 通过设置偏移量将menu隐藏
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
			//content隐藏在左边的宽度
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
	 * 打开菜单
	 * */
	private void openMenu(){
		if(isOpen)return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}
	/*
	 * 关闭菜单
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
    /*滚动发生时的全过程*/
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	// TODO Auto-generated method stub
    	super.onScrollChanged(l, t, oldl, oldt);
    	//l是scrollX
    	float scale = l*1.0f/mMenuWidth;//1~0
    	//属性动画
    	ViewHelper.setTranslationX(mMenu, mMenuWidth*scale*0.7f);
    	//内容的动画
    	float rightscale = 0.7f+0.3f*scale;
    	//设置content缩放的中心点
    	ViewHelper.setPivotX(mContent, 0);
    	ViewHelper.setPivotY(mContent, mContent.getHeight()/2);
    	ViewHelper.setScaleX(mContent, rightscale);
    	ViewHelper.setScaleY(mContent, rightscale);
    	
    	//菜单动画
    	float leftScale = 1.0f-scale*0.3f;
    	float leftAlpha = 0.6f+0.4f*(1-scale);
    	ViewHelper.setScaleX(mMenu, leftScale);
    	ViewHelper.setScaleY(mMenu, leftScale);
    	ViewHelper.setAlpha(mMenu, leftAlpha);
    }
}
