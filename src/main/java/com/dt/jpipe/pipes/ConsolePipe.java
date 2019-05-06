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
public class ConsolePipe<T> extends BasicPipe<T, T> {
    @Override
    protected void process(JobContext context, T t) throws Exception {
        System.out.println(JsonUtil.beanToJson(t));
        sendToNext(t);
    }
}
