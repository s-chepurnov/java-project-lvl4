package hexlet.code.controllers;

import io.javalin.http.Handler;

public final class RootController {

    private static Handler index = ctx -> {
        ctx.render("index.html");
    };

    public static Handler getIndex() {
        return index;
    }
}
