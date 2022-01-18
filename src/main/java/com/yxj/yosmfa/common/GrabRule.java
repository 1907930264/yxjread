package com.yxj.yosmfa.common;

import lombok.Data;

@Data
public class GrabRule {

    private String url;
    private String cssQuery;
    private String novelCssQuery;
    private String urlPrefix;
    private String bookInfoQuery;
}
