package com.fsocial.postservice.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    OK(200, "Thành công", HttpStatus.OK),
    HTTPMETHOD_NOT_SUPPORTED(201, "Phương thức HTTP không được hỗ trợ", HttpStatus.NOT_IMPLEMENTED),
    REGISTER_FAILED(101, "Đăng ký thất bại", HttpStatus.BAD_REQUEST),
    CREATE_POST_SUCCESS(100, "Tạo bài viết thành công", HttpStatus.OK),
    CREATE_POST_FAILED(211, "Tạo bài viết thất bại", HttpStatus.BAD_REQUEST),
    UPDATE_POST_SUCCESS(212, "Cập nhật bài viết thành công", HttpStatus.OK),
    DELETE_POST_SUCCESS(213, "Xóa bài viết thành công", HttpStatus.OK),
    FILE_NOT_FOUND(202, "Không tìm thấy tệp", HttpStatus.NOT_FOUND),
    UPLOAD_FILE_SUCCESS(203, "Tải lên tệp thành công", HttpStatus.OK),
    UPLOAD_FILE_FAILED(204, "Tải lên tệp thất bại", HttpStatus.BAD_REQUEST),
    CREATE_COMMENT_SUCCESS(205, "Tạo bình luận thành công", HttpStatus.OK),
    CREATE_COMMENT_FAILED(206, "Tạo bình luận thất bại", HttpStatus.BAD_REQUEST),
    GET_COMMENT_SUCCESS(207, "Lấy bình luận thành công", HttpStatus.OK),
    USER_NOT_FOUND(208, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    POST_NOT_FOUND(209, "Không tìm thấy bài viết", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(468, "Tài khoản chưa được xác thực", HttpStatus.BAD_REQUEST),
    UPLOAD_MEDIA_FAILED(210, "Tải lên media thất bại", HttpStatus.BAD_REQUEST),
    ;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
