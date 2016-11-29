package com.cn.guojinhu.Result;

import com.google.zxing.client.result.ParsedResult;

import java.io.Serializable;

/**
 * Created by guojin.hu on 2016/11/28.
 */

public class ResultWrapper implements Serializable {

    private ParsedResult mParsedResult;

    public ResultWrapper(ParsedResult parsedResult) {
        mParsedResult = parsedResult;
    }

    public ParsedResult getParsedResult() {
        return mParsedResult;
    }
}
