package com.yihexinda.nodeweb.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Controller
@RequestMapping("")
public class IndexController {

    @RequestMapping("")
    public String index() {
        return "index";
    }

    @RequestMapping(value = {"{path:(?!assets|error|swagger|webjars|sso|api).*$}", "{path:(?!assets|error|swagger|webjars|sso|api).*$}/**"}, headers = "Accept=text/html")
    public String path(HttpServletRequest request, @PathVariable(value = "path") String path) {
        path = request.getServletPath();
        path = path.substring(1);
        if (path.toLowerCase().contains(".html")) {
            path = path.replace(".html", "");
        }
        if (path.contains("../")) {
            return "index";
        }
        return path;
    }

}
