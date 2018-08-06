/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.github.longlinht.library.base;

import java.util.ArrayList;
import java.util.List;

import io.github.longlinht.library.R;
import io.github.longlinht.library.base.interfaces.ActivityPresenter;
import io.github.longlinht.library.base.util.Log;
import io.github.longlinht.library.base.util.ScreenUtil;
import io.github.longlinht.library.base.util.StringUtil;
import io.github.longlinht.library.manager.SystemBarTintManager;
import io.github.longlinht.library.manager.ThreadManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**基础android.support.v4.app.FragmentActivity，通过继承可获取或使用 里面创建的 组件 和 方法
 * *onFling内控制左右滑动手势操作范围，可自定义
 * @author Lemon
 * @see ActivityPresenter#getActivity
 * @see #context
 * @see #view
 * @see #fragmentManager
 * @see #setContentView
 * @see #runUiThread
 * @see #runThread
 * @see #onDestroy
 * @use extends BaseActivity, 具体参考 .DemoActivity 和 .DemoFragmentActivity
 */
public abstract class BaseActivity extends AppCompatActivity implements ActivityPresenter, OnGestureListener {
	private static final String TAG = "BaseActivity";

	/**
	 * 该Activity实例，命名为context是因为大部分方法都只需要context，写成context使用更方便
	 * @warn 不能在子类中创建
	 */
	protected BaseActivity context = null;
	/**
	 * 该Activity的界面，即contentView
	 * @warn 不能在子类中创建
	 */
	protected View view = null;
	/**
	 * 布局解释器
	 * @warn 不能在子类中创建
	 */
	protected LayoutInflater inflater = null;
	/**
	 * Fragment管理器
	 * @warn 不能在子类中创建
	 */
	protected FragmentManager fragmentManager = null;

	private boolean isAlive = false;
	private boolean isRunning = false;

	private PowerManager.WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = (BaseActivity) getActivity();
		isAlive = true;
		fragmentManager = getSupportFragmentManager();

		inflater = getLayoutInflater();

		threadNameList = new ArrayList<String>();
		
		BaseBroadcastReceiver.register(context, receiver, ACTION_EXIT_APP);
	}

	/** 子类可以重写改变状态栏颜色 */
    protected int setStatusBarColor() {
        return getColorPrimary();
    }

    /** 子类可以重写决定是否使用透明状态栏 */
    protected boolean translucentStatusBar() {
        return false;
    }

    /** 设置状态栏颜色 */
    protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0使用三方工具类，有些4.4的手机有问题，这里为演示方便，不使用沉浸式
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(setStatusBarColor());
        }
    }

    /** 获取主题色 */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /** 获取深主题色 */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    /** 初始化 Toolbar */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

	/**
	 * 用于 打开activity以及activity之间的通讯（传值）等；一些通讯相关基本操作（打电话、发短信等）
	 */
	protected Intent intent = null;

	/**
	 * 退出时之前的界面进入动画,可在finish();前通过改变它的值来改变动画效果
	 */
	protected int enterAnim = R.anim.fade;
	/**
	 * 退出时该界面动画,可在finish();前通过改变它的值来改变动画效果
	 */
	protected int exitAnim = R.anim.right_push_out;

	//显示与关闭进度弹窗方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 进度弹窗
	 */
	protected ProgressDialog progressDialog = null;

	/**展示加载进度条,无标题
	 * @param stringResId
	 */
	public void showProgressDialog(int stringResId){
		try {
			showProgressDialog(null, context.getResources().getString(stringResId));
		} catch (Exception e) {
			Log.e(TAG, "showProgressDialog  showProgressDialog(null, context.getResources().getString(stringResId));");
		}
	}
	/**展示加载进度条,无标题
	 * @param message
	 */
	public void showProgressDialog(String message){
		showProgressDialog(null, message);
	}
	/**展示加载进度条
	 * @param title 标题
	 * @param message 信息
	 */
	public void showProgressDialog(final String title, final String message){
		runUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(context);
				}
				if(progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				if (StringUtil.isNotEmpty(title, false)) {
					progressDialog.setTitle(title);
				}
				if (StringUtil.isNotEmpty(message, false)) {
					progressDialog.setMessage(message);
				}
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}
		});
	}


	/**隐藏加载进度
	 */
	public void dismissProgressDialog() {
		runUiThread(new Runnable() {
			@Override
			public void run() {
				//把判断写在runOnUiThread外面导致有时dismiss无效，可能不同线程判断progressDialog.isShowing()结果不一致
				if(progressDialog == null || progressDialog.isShowing() == false){
					Log.w(TAG, "dismissProgressDialog  progressDialog == null" +
							" || progressDialog.isShowing() == false >> return;");
					return;
				}
				progressDialog.dismiss();
			}
		});
	}
	//显示与关闭进度弹窗方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**打开新的Activity，向左滑入效果
	 * @param intent
	 */
	public void toActivity(Intent intent) {
		toActivity(intent, true);
	}
	/**打开新的Activity
	 * @param intent
	 * @param showAnimation
	 */
	public void toActivity(Intent intent, boolean showAnimation) {
		toActivity(intent, -1, showAnimation);
	}
	/**打开新的Activity，向左滑入效果
	 * @param intent
	 * @param requestCode
	 */
	public void toActivity(Intent intent, int requestCode) {
		toActivity(intent, requestCode, true);
	}
	/**打开新的Activity
	 * @param intent
	 * @param requestCode
	 * @param showAnimation
	 */
	public void toActivity(final Intent intent, final int requestCode, final boolean showAnimation) {
		runUiThread(new Runnable() {
			@Override
			public void run() {
				if (intent == null) {
					Log.w(TAG, "toActivity  intent == null >> return;");
					return;
				}
				//fragment中使用context.startActivity会导致在fragment中不能正常接收onActivityResult
				if (requestCode < 0) {
					startActivity(intent);
				} else {
					startActivityForResult(intent, requestCode);
				}
				if (showAnimation) {
					overridePendingTransition(R.anim.right_push_in, R.anim.hold);
				} else {
					overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
				}
			}
		});
	}
	//启动新Activity方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//show short toast 方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param stringResId
	 */
	public void showShortToast(int stringResId) {
		try {
			showShortToast(context.getResources().getString(stringResId));
		} catch (Exception e) {
			Log.e(TAG, "showShortToast  context.getResources().getString(resId)" +
					" >>  catch (Exception e) {" + e.getMessage());
		}
	}
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param string
	 */
	public void showShortToast(String string) {
		showShortToast(string, false);
	}
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param string
	 * @param isForceDismissProgressDialog
	 */
	public void showShortToast(final String string, final boolean isForceDismissProgressDialog) {
		runUiThread(new Runnable() {
			@Override
			public void run() {
				if (isForceDismissProgressDialog) {
					dismissProgressDialog();
				}
				Toast.makeText(context, "" + string, Toast.LENGTH_SHORT).show();
			}
		});
	}
	//show short toast 方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//运行线程 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**在UI线程中运行，建议用这个方法代替runOnUiThread
	 * @param action
	 */
	public final void runUiThread(Runnable action) {
		if (isAlive() == false) {
			Log.w(TAG, "runUiThread  isAlive() == false >> return;");
			return;
		}
		runOnUiThread(action);
	}
	/**
	 * 线程名列表
	 */
	protected List<String> threadNameList;
	/**运行线程
	 * @param name
	 * @param runnable
	 * @return
	 */
	public final Handler runThread(String name, Runnable runnable) {
		if (isAlive() == false) {
			Log.w(TAG, "runThread  isAlive() == false >> return null;");
			return null;
		}
		name = StringUtil.getTrimedString(name);
		Handler handler = ThreadManager.getInstance().runThread(name, runnable);
		if (handler == null) {
			Log.e(TAG, "runThread handler == null >> return null;");
			return null;
		}

		if (threadNameList.contains(name) == false) {
			threadNameList.add(name);
		}
		return handler;
	}

	//运行线程 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/**
     * 保持屏幕常量
     */
    protected void keepScreenOn() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tao");
        wakeLock.setReferenceCounted(false);
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 弹出软键盘
     */
    public boolean showSoftInput(Context context, EditText editText) {
        try {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 隐藏键盘
     */
    public void hideSoftInput(Activity context) {
        if (context == null) {
            return;
        }
        try {
            View focusView = context.getCurrentFocus();
            if (focusView != null) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	//Activity的返回按钮和底部弹窗的取消按钮几乎是必备，正好原生支持反射；而其它比如Fragment极少用到，也不支持反射<<<<<<<<<
	/**返回按钮被点击，默认处理是onBottomDragListener.onDragBottom(false)，重写可自定义事件处理
	 * @param v
	 * @use layout.xml中的组件添加android:onClick="onReturnClick"即可
	 * @warn 只能在Activity对应的contentView layout中使用；
	 * *给对应View setOnClickListener会导致android:onClick="onReturnClick"失效
	 */
	@Override
	public void onReturnClick(View v) {
		Log.d(TAG, "onReturnClick >>>");
		onBackPressed();//会从最外层子类调finish();BaseBottomWindow就是示例
	}
	/**前进按钮被点击，默认处理是onBottomDragListener.onDragBottom(true)，重写可自定义事件处理
	 * @param v
	 * @use layout.xml中的组件添加android:onClick="onForwardClick"即可
	 * @warn 只能在Activity对应的contentView layout中使用；
	 * *给对应View setOnClickListener会导致android:onClick="onForwardClick"失效
	 */
	@Override
	public void onForwardClick(View v) {
		Log.d(TAG, "onForwardClick >>>");
	}
	//Activity常用导航栏右边按钮，而且底部弹窗BottomWindow的确定按钮是必备；而其它比如Fragment极少用到，也不支持反射>>>>>


	@Override
	public final boolean isAlive() {
		return isAlive && context != null;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
	}
	@Override
	public final boolean isRunning() {
		return isRunning & isAlive();
	}

	/**一般用于对不支持的数据的处理，比如onCreate中获取到不能接受的id(id<=0)可以这样处理
	 */
	public void finishWithError(String error) {
		showShortToast(error);
		enterAnim = exitAnim = R.anim.null_anim;
		finish();
	}
	
	@Override
	public void finish() {
		super.finish();//必须写在最前才能显示自定义动画
		runUiThread(new Runnable() {
			@Override
			public void run() {
				if (enterAnim > 0 && exitAnim > 0) {
					try {
						overridePendingTransition(enterAnim, exitAnim);
					} catch (Exception e) {
						Log.e(TAG, "finish overridePendingTransition(enterAnim, exitAnim);" +
								" >> catch (Exception e) {  " + e.getMessage());
					}
				}
			}
		});
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "\n onResume <<<<<<<<<<<<<<<<<<<<<<<");
		super.onResume();
		isRunning = true;
		Log.d(TAG, "onResume >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "\n onPause <<<<<<<<<<<<<<<<<<<<<<<");
		super.onPause();
		isRunning = false;
		Log.d(TAG, "onPause >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

	/**销毁并回收内存
	 * @warn 子类如果要使用这个方法内用到的变量，应重写onDestroy方法并在super.onDestroy();前操作
	 */
	@Override
	protected void onDestroy() {
		Log.d(TAG, "\n onDestroy <<<<<<<<<<<<<<<<<<<<<<<");
		dismissProgressDialog();
		BaseBroadcastReceiver.unregister(context, receiver);
		ThreadManager.getInstance().destroyThread(threadNameList);
		if (view != null) {
			try {
				view.destroyDrawingCache();
			} catch (Exception e) {
				Log.w(TAG, "onDestroy  try { view.destroyDrawingCache();" +
						" >> } catch (Exception e) {\n" + e.getMessage());
			}
		}

		isAlive = false;
		isRunning = false;
		super.onDestroy();

		inflater = null;
		view = null;

		fragmentManager = null;
		progressDialog = null;
		threadNameList = null;

		intent = null;

		context = null;

		Log.d(TAG, "onDestroy >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

	
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			String action = intent == null ? null : intent.getAction();
			if (isAlive() == false || StringUtil.isNotEmpty(action, true) == false) {
				Log.e(TAG, "receiver.onReceive  isAlive() == false" +
						" || StringUtil.isNotEmpty(action, true) == false >> return;");
				return;
			}

			if (ACTION_EXIT_APP.equals(action)) {
				finish();
			} 
		}
	};
	


	//手机返回键和菜单键实现同点击标题栏左右按钮效果<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private boolean isOnKeyLongPress = false;
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		isOnKeyLongPress = true;
		return true;
	}

	//手机返回键和菜单键实现同点击标题栏左右按钮效果>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//底部滑动实现同点击标题栏左右按钮效果<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
	}

}