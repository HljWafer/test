package controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by ggh on 16-8-29.
 */
@Controller
public class SQLController {

    @Autowired
    private YIjianjianyiLabelBuidel buidel;
    @RequestMapping(value = "/getResult", method = {RequestMethod.GET})
    public ModelAndView getHospitalFaculty(@RequestParam String query) throws SQLException {
        ModelAndView modelAndView = new ModelAndView("fit");
        ArrayList<String> strings = buidel.get(query);
        modelAndView.addObject("msg", new Gson().toJson(strings));
        modelAndView.addObject("msg1", query);
        return modelAndView;
    }



    @Autowired
    private YIjianjianyiLabelBuidel2 buidel2;
    @RequestMapping(value = "/getResult2", method = {RequestMethod.GET})
    public ModelAndView getHospitalFaculty2(@RequestParam String query) throws SQLException {
        ModelAndView modelAndView = new ModelAndView("fit2");
        modelAndView.addObject("msg", new Gson().toJson(buidel2.get(query)));
        modelAndView.addObject("msg1", query);
        return modelAndView;
    }

}
