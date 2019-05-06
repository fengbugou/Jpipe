package com.dt.jpipe.model;

import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
@AllArgsConstructor
public class WebPage {

    public Url url;
    public Document document;

}
