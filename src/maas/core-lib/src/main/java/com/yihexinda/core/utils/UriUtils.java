package com.yihexinda.core.utils;

/**
 * @author Jack
 * @date 2018/10/13.
 */
public class UriUtils {
    public static String getFullServerName(String url) {
        String schema = url.substring(0, url.indexOf("//")) + "//";
        String urlWithoutSchema = url.substring(url.indexOf("//") + 2);//----:8080/api/test
        return schema + urlWithoutSchema.substring(0, urlWithoutSchema.indexOf("/"));
    }
}
