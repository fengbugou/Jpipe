package com.dt.jpipe.model;

import lombok.AllArgsConstructor;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
public class Url {

    public String fullUrl;
    public String host;
    public String title;

    public Url(String fullUrl, String host) {
        this.fullUrl = fullUrl;
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        this.host = host;
    }
}
