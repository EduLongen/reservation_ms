package com.roomreservation.reservation.interfaces.rest.dto;

public class ReservationResponse {
    private Long id;
    private Long roomId;
    private Long userId;
    private String startDateTime;
    private String endDateTime;

    public ReservationResponse(Long id, Long roomId, Long userId, String startDateTime, String endDateTime) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getStartDateTime() { return startDateTime; }
    public void setStartDateTime(String startDateTime) { this.startDateTime = startDateTime; }
    public String getEndDateTime() { return endDateTime; }
    public void setEndDateTime(String endDateTime) { this.endDateTime = endDateTime; }
} 