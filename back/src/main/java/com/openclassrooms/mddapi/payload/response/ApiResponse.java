package com.openclassrooms.mddapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String result;    // SUCCESS or ERROR
    private String message;   // success or error message
    private T data;           // return object from service class, if successful

    /**
     * Static helper method to create a success response.
     *
     * @param data    The data payload.
     * @param message Success message.
     * @param <T>     The type of the data payload.
     * @return An ApiResponse instance for success.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    /**
     * Static helper method to create an error response.
     *
     * @param message Error message.
     * @param <T>     The type of the data payload (typically Void or Object for errors).
     * @return An ApiResponse instance for error.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }
}