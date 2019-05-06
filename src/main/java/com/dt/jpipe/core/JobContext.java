package com.dt.jpipe.core;

import com.dt.jpipe.util.DbMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * a pipeline job's context, one for each pipeline job
 *
 * @author ofisheye
 * @date 2019-01-25
 */
@Data
@Slf4j
public class JobContext {

    private JobConfig config;
    private Metric metric = new Metric();
    private String session;
    /**
     * will log during pipeline execution, only context data should put in here, not security sensitive data
     */
    private DbMap publicData = new DbMap();
    private JobResult result;
    private List<BasicPipe> pipes;

    public JobContext(String session, JobConfig config) {
        this.session = session;
        this.config = config;
    }
}
