package com.fsocial.relationshipService.exception;

import com.fsocial.relationshipService.enums.ErrorCode;
import lombok.Getter;

/*
AppCheckedException là một checked exception,
được sử dụng để xử lý các lỗi trong ứng dụng mà yêu cầu phải bắt buộc
khai báo throws hoặc xử lý bằng khối try-catch tại các nơi sử dụng phương thức ném ra nó.
Thường được sử dụng cho các trường hợp lỗi mà có thể được dự đoán và cần phải xử lý
ngay khi nó xảy ra để đảm bảo tính nhất quán và bảo mật trong ứng dụng Java.
*/
@Getter
public class AppCheckedException extends Exception {
    private final ErrorCode status;

    public AppCheckedException(ErrorCode status) {
        super(status.getMessage());
        this.status = status;
    }

}
