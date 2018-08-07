package io.github.longlinht.library.base;

import io.github.longlinht.library.base.util.DataKeeper;
import io.github.longlinht.library.base.util.Log;
import io.github.longlinht.library.base.util.SettingUtil;
import io.github.longlinht.library.rx.RxInspect;
import io.github.longlinht.library.utils.GlobalContext;

import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

import static io.github.longlinht.library.BuildConfig.DEBUG;

/**基础Application
 * @author Lemon
 * @see #init
 * @use extends BaseApplication 或 在你的Application的onCreate方法中BaseApplication.init(this);
 */
abstract public class BaseApplication extends Application {
	private static final String TAG = "BaseApplication";

	public BaseApplication() {
	}
	
	private static Application instance;
	public static Application getInstance() {
		return instance;
	}

	@CallSuper
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @CallSuper
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "项目启动 >>>>>>>>>>>>>>>>>>>> \n\n");

		GlobalContext.setAppContext(this);
        GlobalContext.setApplication(this);

        if (DEBUG) {
            RxInspect.inspect();
        }
		
		init(this);
	}

	/**初始化方法
	 * @param application
	 * @must 调用init方法且只能调用一次，如果extends BaseApplication会自动调用
	 */
	public static void init(Application application) {
		instance = application;
		if (instance == null) {
			Log.e(TAG, "\n\n\n\n\n !!!!!! 调用BaseApplication中的init方法，instance不能为null !!!" +
					"\n <<<<<< init  instance == null ！！！ >>>>>>>> \n\n\n\n");
		}
		
		DataKeeper.init(instance);
		SettingUtil.init(instance);
	}

	/**获取应用名
	 * @return
	 */
	abstract public String getAppName();
	/**获取应用版本名(显示给用户看的)
	 * @return
	 */
	abstract public String getAppVersion();

}
