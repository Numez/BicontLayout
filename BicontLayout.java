package it.tizianomunegato.views;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;

public class BicontLayout extends LinearLayout {
	// author Tiziano Munegato
	private View divider;
	private View vTop, vBottom;
	private LinearLayout.LayoutParams vTopParams, vBottomParams;
	private int dividerClickableHeight;
	
	public BicontLayout(Context ctx, View viewTop, View viewBottom) {
		super(ctx);
		
		this.vTop = viewTop;
		this.vBottom = viewBottom;
		
	    setOrientation(LinearLayout.VERTICAL);
		setWeightSum(1f);
		
		vTopParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f);
		vBottomParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.5f);
		addView(vTop, vTopParams);

		divider = new View(ctx);
		divider.setBackgroundColor(Color.RED);
		
		dividerClickableHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics());
		addView(divider, LinearLayout.LayoutParams.MATCH_PARENT, 3);
		addView(vBottom, vBottomParams);
		
	}

	private float yStartTouch;
	private float yStartWeight;
	private boolean isDragging;
	private int[] dividerLocation = new int[2];

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(onTouchEvent(ev)) return true;
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		switch(me.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yStartTouch = me.getRawY();
				yStartWeight = vTopParams.weight;
				divider.getLocationOnScreen(dividerLocation);
				isDragging = Math.abs(dividerLocation[1]-yStartTouch) < dividerClickableHeight/2;
				break;
			case MotionEvent.ACTION_MOVE:
				if(!isDragging) break;
				
				float yDelta = me.getRawY() - yStartTouch;
				float yDeltaProg = yDelta/BicontLayout.this.getHeight();
				float yNewProg = yStartWeight + yDeltaProg;
				if(yNewProg<0.1f) yNewProg=0.1f;
				if(yNewProg>0.9f) yNewProg=0.9f;

				vTopParams.weight = yNewProg;
				vTop.setLayoutParams(vTopParams);

				vBottomParams.weight = 1f - yNewProg;
				vBottom.setLayoutParams(vBottomParams);

				break;
			case MotionEvent.ACTION_UP:
				isDragging=false;
				break;
		}
		
		if(isDragging) return true;
		return super.onTouchEvent(me);
	}
	
}
