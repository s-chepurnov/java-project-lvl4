package hexlet.code.controllers;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.model.query.QUrl;
import io.ebean.PagedList;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class UrlController {

    private static Handler show = ctx -> {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);

        Url dbUrl = new QUrl()
                .id.equalTo(id)
                .findOne();

        if (dbUrl == null) {
            throw new NotFoundResponse();
        }

        ctx.attribute("url", dbUrl);
        ctx.render("url/show.html");
    };

    private static Handler list = ctx -> {

        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1) - 1;
        final int rowsPerPage = 10;

        PagedList<Url> pagedUrls = new QUrl()
                .setFirstRow(page * rowsPerPage)
                .setMaxRows(rowsPerPage)
                .orderBy()
                .id.asc()
                .findPagedList();

        List<Url> dbUrls = pagedUrls.getList();

        int lastPage = pagedUrls.getTotalPageCount() + 1;
        int currentPage = pagedUrls.getPageIndex() + 1;
        List<Integer> pages = IntStream
                .range(1, lastPage)
                .boxed()
                .collect(Collectors.toList());

        ctx.attribute("pages", pages);
        ctx.attribute("pages", pages);
        ctx.attribute("currentPage", currentPage);
        ctx.attribute("urls", dbUrls);
        ctx.render("url/list.html");
    };

    private static Handler create = ctx -> {
        String name = ctx.formParam("url");
        String parsedUrl = "";
        try {
            URL aURL = new URL(name);
            parsedUrl = aURL.getProtocol() + "://" +  aURL.getHost();
            if (aURL.getPort() > 0) {
                parsedUrl = parsedUrl + ":" +  aURL.getPort();
            }
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.render("/index.html");
            return;
        }

        Url url = new Url(parsedUrl);
        Url dbUrl = new QUrl()
                .name.equalTo(parsedUrl)
                .findOne();

        if (dbUrl != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.attribute("url", url);
            ctx.render("/index.html");
            return;
        }

        url.save();

        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("flash-type", "success");
        ctx.render("/index.html");
    };

    private static Handler check = ctx -> {
        int id = ctx.pathParamAsClass("id", Integer.class).getOrDefault(null);

        UrlCheck urlCheck = new UrlCheck();
        Url dbUrl = new QUrl()
                .id.equalTo(id)
                .findOne();

        if (dbUrl == null) {
            throw new NotFoundResponse();
        }

        String urlStr = dbUrl.getName();

        HttpResponse<String> response = Unirest.get(urlStr).asString();
        String html = response.getBody();
        Document doc = Jsoup.parse(html);
        Elements title = doc.getElementsByTag("title");
        Elements meta = doc.getElementsByTag("meta");
        Elements h1 = doc.getElementsByTag("h1");

        urlCheck.setUrl(dbUrl);
        urlCheck.setStatusCode(response.getStatus());
        urlCheck.setTitle(title.hasText() ? title.text() : "");
        urlCheck.setH1(h1.hasText() ? h1.text() : "");
        boolean isDsc = meta.hasAttr("description") && meta.hasAttr("content");
        urlCheck.setDescription(isDsc ? meta.attr("content") : "");
        urlCheck.save();

        ctx.attribute("url", dbUrl);
        ctx.render("url/show.html");
    };

    public static Handler listUrl() {
        return list;
    }

    public static Handler createUrl() {
        return create;
    }

    public static Handler showUrl() {
        return show;
    }

    public static Handler checkUrl() {
        return check;
    }
}
