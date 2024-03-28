package com.parkchoi.scrum.util.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "API 응답 DTO")
public class ApiResponse<T> {

    // 상태 구분
    private static final String SUCCESS_STATUS = "success";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    @Schema(description = "API 응답 상태, success : 성공, fail : 실패, error : 서버에러")
    private String status;
    @Schema(description = "API 응답 데이터, null = 반환 데이터 없음, null != 반환 데이터 있음")
    private T data;
    @Schema(description = "API 응답 메시지, message로 구체적인 상황 전달")
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
    public static ApiResponse<Void> createSuccessNoContent(String message){
        return new ApiResponse<>(SUCCESS_STATUS, null, message);
    }

    // 예외 발생으로 API 호출 실패시 반환 (클라이언트 에러)
    public static ApiResponse<Void> createError(String code, String message) {
        return new ApiResponse<>(code, null, message);
    }

    // 예외 발생으로 API 호출 실패시 반환 (클라이언트 에러)
    public static ApiResponse<Void> createClientError(String message) {
        return new ApiResponse<>(FAIL_STATUS, null ,message);
    }

    // 예외 발생으로 API 호출 실패시 반환 (클라이언트 에러)
    public static ApiResponse<Void> createServerError(String message) {
        return new ApiResponse<>(FAIL_STATUS, null ,message);
    }



}
