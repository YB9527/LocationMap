package com.xupu.locationmap.common.imgview;

import java.io.Serializable;

public class Page  implements Serializable {

    private final String text;

    private final String path;

    public Page(String path, String text) {
        this.path = path;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getPath() {
        return path;
    }


}
