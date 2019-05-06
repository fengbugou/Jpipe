package com.dt.jpipe.pipes;

import com.dt.jpipe.core.BasicPipe;
import com.dt.jpipe.core.JobContext;
import com.dt.jpipe.util.JsonUtil;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
public class ToJsonPipe<T> extends BasicPipe<T, String> {

    @Override
    protected void process(JobContext context, T t) {
        sendToNext(JsonUtil.beanToJson(t));
    }
}
