package com.fsocial.postservice.exception;

import com.fsocial.accountservice.exception.StatusCode;

/*
AppCheckedException là một checked exception,
được sử dụng để xử lý các lỗi trong ứng dụng mà yêu cầu phải bắt buộc
khai báo throws hoặc xử lý bằng khối try-catch tại các nơi sử dụng phương thức ném ra nó.
Thường được sử dụng cho các trường hợp lỗi mà có thể được dự đoán và cần phải xử lý
ngay khi nó xảy ra để đảm bảo tính nhất quán và bảo mật trong ứng dụng Java.
*/
public class AppCheckedException extends Exception {
    private final StatusCode status;

    public AppCheckedException(String message, StatusCode status) {
        super(message);
        this.status = status;
    }

    public StatusCode getStatus() {
        return status;
    }
}
