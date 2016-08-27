package com.example.imooc_qq_50_slidemenu;

import com.imooc.slidingmenu.view.slidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
	private slidingMenu mLeftMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mLeftMenu = (slidingMenu) findViewById(R.id.id_menu);
	}
	public void toggleMenu(View view){
		mLeftMenu.toggle();
	}
}
