package com.dt.jpipe.pipes;

import com.dt.jpipe.core.JobContext;
import com.dt.jpipe.model.Url;
import com.dt.jpipe.model.WebPage;
import com.dt.jpipe.util.SpiderUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.function.Function;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
public class SpiderFindLinkPipe extends SpiderReadWebPagePipe<Url> {

    private final Function<Document, List<Element>> howToFindElement;

    public SpiderFindLinkPipe(Function<Document, List<Element>> howToFindATag) {
        this.howToFindElement = howToFindATag;
    }

    @Override
    protected void process(JobContext context, WebPage webPage) {
        List<Element> elements = howToFindElement.apply(webPage.document);
        if (elements != null) {
            for (Element element : elements) {
                Url output = SpiderUtil.aTagToUrl(webPage.url, element);
                sendToNext(output);
            }
        }
    }
}
