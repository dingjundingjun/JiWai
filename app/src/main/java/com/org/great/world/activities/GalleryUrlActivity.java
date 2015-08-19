/*
 * Copyright (c) 2012 Roman Truba Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software. THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.org.great.world.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.org.great.wrold.R;

public class GalleryUrlActivity extends Activity {
	
    public static final int FLAG_TRANSLUCENT_NAVIGATION = 0x08000000;
	public static final int FLAG_TRANSLUCENT_STATUS = 0x04000000;
	public static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = 0x80000000;
	
	private ImageView mImageView;
	protected ImageLoader mLoader;
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_main);

        final String urls = getIntent().getStringExtra("urls");
        mImageView = (ImageView)findViewById(R.id.viewer);
        mLoader = ImageLoader.getInstance();
        mLoader.init(ImageLoaderConfiguration.createDefault(GalleryUrlActivity.this));
        mLoader.displayImage(urls, mImageView, initOptions());
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    
	private DisplayImageOptions initOptions() 
	{
    	return new DisplayImageOptions.Builder()
        .showImageOnLoading(android.R.color.transparent)
        .showImageForEmptyUri(R.drawable.default_avatar)
        .showImageOnFail(R.drawable.default_avatar)
        .cacheInMemory(false)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
        .bitmapConfig(Bitmap.Config.ARGB_8888)
        .displayer(new FadeInBitmapDisplayer(100))
        .build();
    }
    
    @Override
    public void onAttachedToWindow() {
    	switchTransSystemUI(true);
    	super.onAttachedToWindow();
    }
    
    private void switchTransSystemUI(boolean on) {

		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = (FLAG_TRANSLUCENT_STATUS | FLAG_TRANSLUCENT_NAVIGATION);
		 if (on) {
			winParams.flags |= bits;
		 } else {
			winParams.flags &= ~bits;
		 }
		win.setAttributes(winParams);
	}

}