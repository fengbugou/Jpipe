package com.dt.jpipe;


import com.dt.jpipe.core.BasicPipe;
import com.dt.jpipe.core.JobContext;
import com.dt.jpipe.core.Nullable;
import com.dt.jpipe.model.Url;
import com.dt.jpipe.util.SpiderUtil;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.jvm.hotspot.debugger.Page;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO desc
 *
 * @author vn010ts
 * @date 2019-04-26
 */
public abstract class SpiderPagingPipe extends BasicPipe<Url, SpiderPagingPipe.PageResource> {

    @Override
    protected void process(JobContext context, Url url) throws Exception {
        String nextPageATagSelector = getNextPageATagSelector();

        Url currentPageUrl = url;
        String title = url.title;
        while (true) {
            Document document = Jsoup.connect(currentPageUrl.fullUrl).get();
            String resource = getPageResource(document);
            PageResource page = new PageResource(title, currentPageUrl, resource);
            sendToNext(page);

            Elements elements = document.select(nextPageATagSelector);
            if (elements == null || elements.isEmpty()) {
                break;
            }
            currentPageUrl = SpiderUtil.aTagToUrl(url, elements.first());
        }
    }

    @AllArgsConstructor
    public static class PageResource {
        public String title;
        public Url url;
        public String resource;
    }

    @Nullable
    abstract String getNextPageATagSelector();

    abstract String getPageResource(Document document);
}
