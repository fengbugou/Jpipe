package com.dt.jpipe.pipes;

import com.dt.jpipe.core.BasicPipe;
import com.dt.jpipe.core.JobContext;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-17
 */
public class FilterByIndexPipe<T> extends BasicPipe<T, T> {
    private boolean[] filter;
    private int index;

    public FilterByIndexPipe(int period, int... choices) {
        filter = new boolean[period];
        for (int choice : choices) {
            filter[choice] = true;
        }
    }

    @Override
    protected void process(JobContext context, T t) throws Exception {
        if (filter[index % filter.length]) {
            sendToNext(t);
        }
        index++;
    }
}
