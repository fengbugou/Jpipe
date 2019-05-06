package com.dt.jpipe.util;

import com.dt.jpipe.model.Url;
import com.dt.jpipe.model.WebPage;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO desc
 *
 * @author ofisheye
 * @date 2019-04-16
 */
public class SpiderUtil {

    public static final Pattern URL_PATTERN = Pattern.compile("(http://|https://)([\\w.]+)/?.*");

    public static Url parseFullUrl(String fullUrl) {
        String host = findHostFromUrl(fullUrl);
        return new Url(fullUrl, host);
    }

    public static Url parseUrl(String maybeRelativeUrl, Url currentUrl) {
        if (URL_PATTERN.matcher(maybeRelativeUrl).matches()) {
            return parseFullUrl(maybeRelativeUrl);
        } else {
            String fullUrl;
            if (maybeRelativeUrl.startsWith("/")) {
                fullUrl = currentUrl.host + maybeRelativeUrl;
            } else {
                if (maybeRelativeUrl.startsWith("../")) {
                    String commonPrefixUrl = currentUrl.fullUrl;
                    while (maybeRelativeUrl.startsWith("../")) {
                        commonPrefixUrl = commonPrefixUrl.substring(0, commonPrefixUrl.lastIndexOf("/"));
                        maybeRelativeUrl = maybeRelativeUrl.substring("../".length());
                    }
                    fullUrl = commonPrefixUrl + "/" + maybeRelativeUrl;
                } else {
                    fullUrl = currentUrl.fullUrl.substring(0, currentUrl.fullUrl.lastIndexOf("/")) + "/" + maybeRelativeUrl;
                }
            }
            return new Url(fullUrl, currentUrl.host);
        }
    }

    public static String findHostFromUrl(String url) {
        Matcher matcher = URL_PATTERN.matcher(url);
        if (matcher.matches()) {
            String scheme = matcher.group(1);
            if (scheme == null) {
                scheme = "http://";
            }
            String host = matcher.group(2);
            return scheme + host;
        } else {
            throw new IllegalArgumentException("invalid url:" + url);
        }
    }

    public static Url aTagToUrl(Url currentUrl, Element element) {
        String href = element.attr("href");
        String title = element.text();
        if (title == null || title.isEmpty()) {
            title = element.attr("title");
        }
        Url output = SpiderUtil.parseUrl(href, currentUrl);
        output.title = title;
        return output;
    }
}
