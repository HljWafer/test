package controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * Created by wafer on 17/8/7.
 */
@Controller
public class YIjianjianyiColler {


    @Autowired
    private YIjianjianyiLabelBuidel buidel;

    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String search(String text) throws Exception {
        ArrayList<String> strings = buidel.get(text);
        return new Gson().toJson(strings);
    }

    @Autowired
    private YIjianjianyiLabelBuidel2 buidel2;

    @RequestMapping(value = "/get2", method = {RequestMethod.GET, RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String search2(String text) throws Exception {
        return new Gson().toJson(buidel2.get(text));
    }

    @RequestMapping(value = "/init", method = {RequestMethod.GET, RequestMethod.POST}, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    public String init() throws Exception {
//        buidel.init();
        buidel2.init();
        return new Gson().toJson("init end");
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST}, produces = {
            "application/json; charset=utf-8"})
    @ResponseBody
    public String test() throws Exception {
        return new Gson().toJson("ok");
    }
}
