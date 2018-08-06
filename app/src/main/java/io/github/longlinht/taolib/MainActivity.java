package io.github.longlinht.taolib;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.longlinht.library.network.OkGo;
import io.github.longlinht.library.network.adapter.ObservableResponse;
import io.github.longlinht.library.network.convert.BitmapConvert;
import io.github.longlinht.library.network.model.Response;
import io.github.longlinht.library.utils.GlobalContext;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
