package com.fpt.mss.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status", "message", "data", "errorData", "error", "timestamp", "path"})
public class ApiResponse<T> {
    private int status;
    private T data;
    private T errorData;
    private String error;
    private String message;
    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(HttpStatus status, T data, String message) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return success(HttpStatus.OK, null, message);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(HttpStatus.OK, data, "Success");
    }

    public static <T> ApiResponse<T> error(T errorData, HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .errorData(errorData)
                .error(status.getReasonPhrase())
                .message(message)
                .path(getCurrentPath())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .errorData(null)
                .error(status.getReasonPhrase())
                .message(message)
                .path(getCurrentPath())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Shorthand methods for common responses
    public static <T> ApiResponse<T> success() {
        return success(HttpStatus.OK, null, "Success");
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return success(HttpStatus.CREATED, data, message);
    }

    public static <T> ApiResponse<T> created(T data) {
        return success(HttpStatus.CREATED, data, "Created successfully");
    }

    public static <T> ApiResponse<T> created() {
        return success(HttpStatus.CREATED, null, "Created successfully");
    }

    public static <T> ApiResponse<T> updated(T data, String message) {
        return success(HttpStatus.OK, data, message);
    }

    public static <T> ApiResponse<T> updated(T data) {
        return success(HttpStatus.OK, data, "Updated successfully");
    }

    public static <T> ApiResponse<T> updated() {
        return success(HttpStatus.OK, null, "Updated successfully");
    }

    public static <T> ApiResponse<T> noContent() {
        return success(HttpStatus.NO_CONTENT, null, "No Content");
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return error(HttpStatus.CONFLICT, message);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return error(HttpStatus.FORBIDDEN, message);
    }

    static ResponseEntity<Resource> file(Resource file, String filename, String contentType) {
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

    public static ResponseEntity<Resource> file(Resource file, String filename) {
        String lower = filename.toLowerCase();
        String contentType;

        if (lower.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (lower.endsWith(".docx")) {
            contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (lower.endsWith(".xlsx")) {
            contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else if (lower.endsWith(".csv")) {
            contentType = "text/csv";
        } else if (lower.endsWith(".txt")) {
            contentType = "text/plain";
        } else {
            contentType = "application/octet-stream";
        }
        return file(file, filename, contentType);
    }

    private static String getCurrentPath() {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest().getRequestURI();
        } catch (IllegalStateException e) {
            return "unknown";
        }
    }
}
