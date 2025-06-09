package com.roomreservation.reservation.interfaces.rest.dto;

public class ReservationRequest {
    private Long roomId;
    private Long userId;
    private String dateTime;

    // getters and setters
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
} 