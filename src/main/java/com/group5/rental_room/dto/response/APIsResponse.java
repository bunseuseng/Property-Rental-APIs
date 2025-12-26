package com.group5.rental_room.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
public class APIsResponse<T> {
    @JsonProperty("Message")
    private String message;

    @JsonProperty("StatusCode")
    private Integer statusCode;

    @JsonProperty("Date")
    private T date;

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("ListData")
    private List<T> listData;

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonProperty("role")
    private String role;

    @JsonProperty("userId")
    private Long userId;
}