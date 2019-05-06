package com.dt.jpipe.core;

import com.dt.jpipe.util.DbMap;
import com.dt.jpipe.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-02-14
 */
@Slf4j
public class PipeBuilder {

    private List<BasicPipe> pipes = new LinkedList<>();

    private PipeBuilder() {
    }

    public static <From, To> PipeJoint<From, To> firstPipe(BasicPipe<From, To> firstPipe) {
        PipeBuilder builder = new PipeBuilder();
        builder.pipes.add(firstPipe);
        return new PipeJoint<>(firstPipe, builder);
    }

    public static class PipeJoint<Input, Output> {
        private final PipeBuilder builder;
        private final BasicPipe<Input, Output> pipe;

        private PipeJoint(BasicPipe<Input, Output> pipe, PipeBuilder builder) {
            this.pipe = pipe;
            this.builder = builder;
        }

        public <NextOutput> PipeJoint<Output, NextOutput> nextPipe(BasicPipe<Output, NextOutput> nextPipe) {
            this.pipe.nextPipe(nextPipe);
            this.builder.pipes.add(nextPipe);
            return new PipeJoint<>(nextPipe, builder);
        }

        public void execute(JobContext context) {
            context.setPipes(builder.pipes);

            JobResult result = new JobResult();
            context.setResult(result);

            result.setStartTime(System.currentTimeMillis());

            String session = context.getSession();

            try {
                log.info("pipeline start, session={}, publicData={}", session, context.getPublicData());

                for (BasicPipe pipe : builder.pipes) {
                    pipe.setContext(context);
                    pipe.onStart(context);
                }

                builder.pipes.get(0).preProcess(context, null);
                for (BasicPipe p : builder.pipes) {
                    p.onFinish(context);
                }
                result.setSuccess(true);
            } catch (Throwable e) {
                result.setError(e);
            } finally {
                result.setEndTime(System.currentTimeMillis());
                DbMap stat = new DbMap();
                for (int i = 1; i <= context.getPipes().size(); i++) {
                    BasicPipe p = context.getPipes().get(i - 1);
                    try {
                        stat.put(String.format("-outputCount-%02d-%s", i, p.getDesc()), p.getOutputCount());
                        stat.put(String.format("-costTime-%02d-%s", i, p.getDesc()), p.getMetric());
                    } catch (Exception e) {
                        log.error("", e);
                    }
                }
                if (result.isSuccess()) {
                    log.info("pipeline end, success=true, session='{}', costTime={}sec, publicData={}, pipeStat={}",
                            context.getSession(), result.getJobDuration().getSeconds(), JsonUtil.beanToJson(context.getPublicData()), JsonUtil.beanToJson(stat)
                    );
                } else {
                    log.error("pipeline end, success=false, session={}, pipeStat={}",
                            context.getSession(), JsonUtil.beanToJson(stat), result.getError()
                    );
                }
            }
        }
    }
}
