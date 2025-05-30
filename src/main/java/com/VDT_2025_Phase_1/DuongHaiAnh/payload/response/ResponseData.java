package com.VDT_2025_Phase_1.DuongHaiAnh.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseData<T> {
    @Builder.Default
    private int status = 200;

    @Builder.Default
    private boolean isSuccess = true;

    private String desc;

    private T data;
    public static <T> ResponseData<T> success(String message, T data) {
        return ResponseData.<T>builder()
                .status(200)
                .isSuccess(true)
                .desc(message)
                .data(data)
                .build();
    }

    public static <T> ResponseData<T> error(String message) {
        return ResponseData.<T>builder()
                .status(500)
                .isSuccess(false)
                .desc(message)
                .data(null)
                .build();
    }

    public static <T> ResponseData<T> error(int status, String message) {
        return ResponseData.<T>builder()
                .status(status)
                .isSuccess(false)
                .desc(message)
                .data(null)
                .build();
    }
}
