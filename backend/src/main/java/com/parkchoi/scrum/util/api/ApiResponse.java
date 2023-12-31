package com.parkchoi.scrum.util.api;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    // 상태 구분1
    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    private String status;
    private T data;
    private String message;

    public ApiResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    // 성공했을 때 data와 message 리턴
    public static <T> ApiResponse<T> createSuccess(T data, String message){
        return new ApiResponse<>(SUCCESS_STATUS, data, message);
    }

    // 성공했지만 리턴 데이터가 없는 경우 message 리턴
    public static <T> ApiResponse<T> createSuccessNoContent(String message){
        return new ApiResponse<>(SUCCESS_STATUS, null, message);
    }

    // 예외 발생으로 API 호출 실패시 반환
    public static ApiResponse<?> createError(String message) {
        return new ApiResponse<>(ERROR_STATUS, null, message);
    }


}
