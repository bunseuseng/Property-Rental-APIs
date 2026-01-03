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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Message")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("StatusCode")
    private Integer statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Date")
    private T date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("accessToken")
    private String accessToken;

    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("ListData")


    private List<T> listData;
    @JsonInclude(JsonInclude.Include.NON_NULL)

    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("role")
    private List<String> role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("userId")
    private Long userId;
}