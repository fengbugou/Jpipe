package com.dt.jpipe;

import com.dt.jpipe.core.JobConfig;
import com.dt.jpipe.core.JobContext;
import com.dt.jpipe.core.PipeBuilder;
import com.dt.jpipe.pipes.ConsolePipe;
import com.dt.jpipe.pipes.Pipes;
import com.dt.jpipe.pipes.SpiderFindLinkPipe;
import com.dt.jpipe.pipes.SpiderUrlToWebPagePipe;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Optional;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
public class App {

    public static void main(String[] args) {
        final JobConfig config = new JobConfig();
        PipeBuilder.firstPipe(Pipes.just("https://manhua.fzdm.com/39"))
                   .nextPipe(new SpiderUrlToWebPagePipe())
                   .nextPipe(new SpiderFindLinkPipe(d -> d.select("li.pure-u-1-2 a")))
                   .nextPipe(new SpiderPagingPipe() {

                       @Override
                       String getNextPageATagSelector() {
                           return "a:contains(下一页)";
                       }

                       @Override
                       String getPageResource(Document document) {
                           return "abc";
                       }
                   })
                   .nextPipe(new ConsolePipe<>())
                   .execute(new JobContext("abc", config));
    }
}
