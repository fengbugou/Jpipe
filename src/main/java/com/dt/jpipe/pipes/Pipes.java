package com.dt.jpipe.pipes;

import com.dt.jpipe.core.BasicPipe;
import com.dt.jpipe.core.JobContext;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
public class Pipes {

    public static <T> BasicPipe<Void, T> just(T item) {
        return new BasicPipe<Void, T>() {
            @Override
            protected void process(JobContext context, Void aVoid) throws Exception {
                sendToNext(item);
            }
        };
    }
}
