package com.dt.jpipe.pipes;

import com.dt.jpipe.core.BasicPipe;
import com.dt.jpipe.core.JobContext;
import com.dt.jpipe.model.Url;
import com.dt.jpipe.model.WebPage;
import com.dt.jpipe.util.SpiderUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
public class SpiderUrlToWebPagePipe extends BasicPipe<String, WebPage> {

    @Override
    protected void process(JobContext context, String url) throws IOException {
        String host = SpiderUtil.findHostFromUrl(url);
        Document document = Jsoup.connect(url)
                                 .get();
        sendToNext(new WebPage(new Url(document.location(), host), document));
    }

}
