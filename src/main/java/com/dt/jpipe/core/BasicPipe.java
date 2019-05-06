package com.dt.jpipe.core;

import com.dt.jpipe.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * basic pipe class
 *
 * @author ofisheye
 * @date 2019-02-12
 */
@Slf4j
public abstract class BasicPipe<Input, Output> {

    BasicPipe<Output, ?> nextPipe;
    private JobContext context;

    private AtomicInteger inputCount = new AtomicInteger();
    private AtomicInteger outputCount = new AtomicInteger();
    private ThreadLocal<List<Input>> buffer;
    private int bufferSize;
    private String desc;

    public BasicPipe<Input, Output> setBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        this.buffer = ThreadLocal.withInitial(LinkedList::new);
        return this;
    }

    public BasicPipe<Input, Output> setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    <NextOutput> BasicPipe<Output, NextOutput> nextPipe(BasicPipe<Output, NextOutput> nextPipe) {
        this.nextPipe = nextPipe;
        return nextPipe;
    }

    /**
     * called when job start, before any input data coming
     */
    protected void onStart(JobContext context) {
        //do nothing by default
    }

    protected abstract void process(JobContext context, Input input) throws Exception;

    protected void batchProcess(JobContext context, List<Input> inputList) {
        if (inputList == null) {
            return;
        }
        for (Input input : inputList) {
            tryToProcess(context, input);
        }
    }


    protected void tryToProcess(JobContext context, Input input) {
        try {
            process(context, input);
        } catch (Exception e) {
            context.getResult().setError(e);
            log.error("process encounter error, input={}", JsonUtil.beanToJson(input), e);
            onError(input, e);
        }
    }

    protected void onError(Input input, Exception e) {

    }

    protected void onFinish(JobContext context) {

    }

    protected void sendToNext(Output output) {
        this.outputCount.incrementAndGet();
        if (nextPipe != null) {
            nextPipe.inputCount.incrementAndGet();
            nextPipe.preProcess(context, output);
        }
    }

    protected void batchSendToNext(List<Output> outputList) {
        this.outputCount.addAndGet(outputList.size());
        if (nextPipe != null) {
            nextPipe.inputCount.addAndGet(outputList.size());
            nextPipe.preBatchProcess(context, outputList);
        }
    }

    void preProcess(JobContext context, Input input) {
        if (bufferSize <= 0) {
            tryToProcess(context, input);
        } else {
            this.buffer.get().add(input);
            flush(context, false);
        }
    }

    private void preBatchProcess(JobContext context, List<Input> input) {
        if (bufferSize <= 0) {
            batchProcess(context, input);
        } else {
            this.buffer.get().addAll(input);
            flush(context, false);
        }
    }

    protected void flush(JobContext context) {
        if (bufferSize > 0) {
            flush(context, true);
        }
        if (nextPipe != null) {
            nextPipe.flush(context);
        }
    }

    private void flush(JobContext context, boolean flushWhatLeft) {
        List<Input> buffered = buffer.get();
        if (buffered.isEmpty()) {
            return;
        }
        while (buffered.size() >= bufferSize) {
            List<Input> batch = new LinkedList<>();
            for (int i = 0; i < bufferSize; i++) {
                batch.add(buffered.remove(0));
            }
            batchProcess(context, batch);
        }
        if (flushWhatLeft) {
            batchProcess(context, buffered);
        }
    }

    void setContext(JobContext context) {
        this.context = context;
        context.getMetric().put(getMetricName(), 0);
    }

    public void addToMetricSince(long since) {
        context.getMetric().add(getMetricName(), System.currentTimeMillis() - since);
    }

    public long getMetric() {
        return context.getMetric().get(getMetricName());
    }

    private String getMetricName() {
        return "pipe-" + desc;
    }

    public String getDesc() {
        if (desc == null) {
            desc = getClass().getSimpleName();
        }
        return desc;
    }

    public int getInputCount() {
        return inputCount.get();
    }

    public int getOutputCount() {
        return outputCount.get();
    }

    public JobContext getContext() {
        return context;
    }
}
