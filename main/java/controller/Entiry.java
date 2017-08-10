package controller;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by wafer on 17/8/7.
 */
public class Entiry {
    Entiry(ArrayList<String> content) {
        this.content = content;
    }

    Entiry(ArrayList<String> content, String text, String type) {
        this.content = content;
        this.text = text;
        int i = typeNames.indexOf(type);
        switch (i) {
            case 0:
                总裁办 = 1;
                break;
            case 1:
                随访 = 1;
                break;
            case 2:
                家庭医生 = 1;
                break;
            case 3:
                点评 = 1;
                break;
            case 4:
                服务供应部 = 1;
                break;
            case 5:
                处方运营 = 1;
                break;
            case 6:
                付费文章 = 1;
                break;
            case 7:
                分诊 = 1;
                break;
            case 8:
                转诊 = 1;
                break;
            case 9:
                会诊出差 = 1;
                break;
            case 10:
                APP报错 = 1;
                break;
            case 11:
                产品 = 1;
                break;
            case 12:
                客服中心 = 1;
                break;
            case 13:
                患者运营 = 1;
                break;
            case 14:
                验证码 = 1;
                break;
            case 15:
                商务运营部 = 1;
                break;
            case 16:
                诊后报到 = 1;
                break;
            case 17:
                团队接诊 = 1;
                break;
            case 18:
                电话咨询 = 1;
                break;
            case 19:
                内容 = 1;
                break;
            case 20:
                认证数据 = 1;
                break;
        }
    }

    ArrayList<String> content;
    String text;
    static ArrayList<String> typeNames = new ArrayList<String>();

    static {
        String[] strings = {"总裁办", "随访", "家庭医生", "点评", "服务供应部", "处方运营", "付费文章"
                , "分诊", "转诊", "会诊&出差", "APP报错", "产品", "客服中心", "患者运营"
                , "验证码", "商务运营部", "诊后报到", "团队接诊", "电话咨询", "内容", "认证 数据"};
        Collections.addAll(typeNames, strings);
    }

    int 总裁办, 随访, 家庭医生, 点评, 服务供应部, 处方运营, 付费文章, 分诊, 转诊, 会诊出差, APP报错, 产品, 客服中心, 患者运营, 验证码, 商务运营部, 诊后报到,
            团队接诊, 电话咨询, 内容, 认证数据;


}
