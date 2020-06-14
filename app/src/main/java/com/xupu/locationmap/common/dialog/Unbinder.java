package com.xupu.locationmap.common.dialog;



import androidx.annotation.UiThread;

/** An unbinder contract that will unbind views when called. */
public interface Unbinder {
    @UiThread
    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override public void unbind() { }
    };
}
