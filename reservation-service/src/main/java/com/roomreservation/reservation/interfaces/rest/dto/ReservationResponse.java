package com.roomreservation.reservation.interfaces.rest.dto;

public class ReservationResponse {
    private Long id;
    private Long roomId;
    private Long userId;
    private String dateTime;

    public ReservationResponse(Long id, Long roomId, Long userId, String dateTime) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
} 