package com.bean.simplenews.api;

public class Urls {

    //http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
    public static final int PAGE_SIZE = 20;
    public static final String CATEGORY_TOP = "headline";
    public static final String CATEGORY_COMMON = "list";

    public static final String TOP_ID = "T1348647909107";
    public static final String NBA_ID = "T1348649145984";
    public static final String CAR_ID = "T1348654060988";
    public static final String JOKE_ID = "T1350383429665";

    public static final String HOST = "http://c.m.163.com/";
    public static final String END_URL = "-" + PAGE_SIZE + ".html";
    public static final String END_DETAIL_URL = "/full.html";
    // 头条
    public static final String TOP_URL = HOST + "nc/article/headline/";
    public static final String COMMON_URL = HOST + "nc/article/list/";
    // 新闻详情
    public static final String NEW_DETAIL = HOST + "nc/article/";
}
