package com.nguyenthithao.model;

public interface ResponseCallback {
    void onResponse(String response);
    void onError(Throwable throwable);
}
