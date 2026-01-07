package com.group5.rental_room.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResponseDTO<T> {
    private boolean success;
    private String message;
    private List<T> data;
    private Meta meta; // <-- reference to inner class

    //Inner static class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private long totalItems;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    // Constructor for paginated responses
    public ListResponseDTO(boolean success, String message, List<T> data,
                           int currentPage, int pageSize, int totalPages,
                           long totalItems, boolean hasNext, boolean hasPrevious) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.meta = new Meta(currentPage, pageSize, totalPages, totalItems, hasNext, hasPrevious);
    }

    // Constructor for simple responses
    public ListResponseDTO(boolean success, String message, List<T> data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.meta = new Meta(1, data.size(), 1, data.size(), false, false);
    }
}
