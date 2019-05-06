package com.dt.jpipe.core;

import lombok.Data;

import java.time.Duration;

/**
 * result of a job
 *
 * @author ofisheye
 * @date 2019-02-28
 */
@Data
public class JobResult {

    private long startTime;
    private long endTime;
    private Throwable error;
    private boolean success;

    public Duration getJobDuration() {
        return Duration.ofMillis(endTime - startTime);
    }

    public void setError(Throwable error) {
        this.error = error;
        this.success = false;
    }
}
