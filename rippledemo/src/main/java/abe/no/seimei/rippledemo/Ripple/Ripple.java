package abe.no.seimei.rippledemo.Ripple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by guojin.hu on 2016/11/11.
 */

public class Ripple {

    public static final String KEY_START_POINT = "start_point";

    private Ripple() {
    }

    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static void startActivity(Context cotnext, Class<?> cls, View startView) {
        int[] location = {0, 0};
        startView.getLocationInWindow(location);
        location[1] = location[1] - dip2px(cotnext, 25);
        Point point = new Point(location[0] + startView.getWidth() / 2, location[1] + startView.getHeight() / 2);
        startActivity(cotnext, cls, point);
    }

    public static void startActivity(Context context, Class<?> cls, Point point) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_START_POINT, point);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }
}
