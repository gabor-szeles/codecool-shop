package com.codecool.shop;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.lang.reflect.Type;
import java.util.Map;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private static Utils ourInstance = null;

    private Utils() {
    }

    public static Utils getInstance() {
        if (ourInstance == null) {
            ourInstance = new Utils();
        }
        return ourInstance;
    }

    public static Map<String, String> parseJson(Request request) {
        logger.debug("Entering parseJson(request={})", request);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> out = gson.fromJson(request.body(), type);
        logger.debug("Leaving parseJson(): {}", out);
        return out;
    }

    public static String toJson(Object object) {
        logger.debug("Entering toJson(object={})", object);
        Gson gson = new Gson();
        logger.debug("Leaving toJson(): {}", gson.toJson(object));
        return gson.toJson(object);
    }

    public static String renderTemplate(Map model, String template) {
        logger.debug("Entering renderTemplate(model={}, template={})", model, template);
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, template));
    }

}
