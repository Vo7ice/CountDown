package com.cn.guojinhu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.cn.guojinhu.Result.ResultActivity;

import static com.cn.guojinhu.Result.ResultActivity.CONTACT;
import static com.cn.guojinhu.Result.ResultActivity.KEY_RESULT;
import static com.cn.guojinhu.Result.ResultActivity.KEY_RESULT_TYPE;
import static com.cn.guojinhu.Result.ResultActivity.NORMAL;
import static com.cn.guojinhu.Result.ResultActivity.URL;

/**
 * Created by guojin.hu on 2016/11/24.
 */

public class Utils {

    public static void startActionPhase(Context context, String content) {
        Intent intent = new Intent(context, ResultActivity.class);
        Bundle bundle = handleActionPhase(content);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private static Bundle handleActionPhase(String content) {
        Bundle bundle = new Bundle();
        boolean matches = Patterns.WEB_URL.matcher(content).matches();
        if (content.startsWith("BEGIN:VCARD")) {
            bundle.putString(KEY_RESULT, content);
            bundle.putInt(KEY_RESULT_TYPE, CONTACT);
            phaseContact(bundle, content);
        } else if (matches) {
            bundle.putString(KEY_RESULT, content);
            bundle.putInt(KEY_RESULT_TYPE, URL);
        } else {
            bundle.putString(KEY_RESULT, content);
            bundle.putInt(KEY_RESULT_TYPE, NORMAL);
        }
        return bundle;
    }

    private static void phaseContact(Bundle bundle, String content) {

    }
}
