package com.roomreservation.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String list(Model model) {
        try {
            // Fetch reservations
            ResponseEntity<Map[]> reservationsResponse = restTemplate.getForEntity(
                "http://reservation-service:8083/api/reservations", 
                Map[].class
            );
            List<Map> reservations = Arrays.asList(reservationsResponse.getBody());
            logger.info("Reservations found: {}", reservations.size());

            // Fetch rooms
            ResponseEntity<Map[]> roomsResponse = restTemplate.getForEntity(
                "http://room-service:8082/api/rooms", 
                Map[].class
            );
            List<Map> rooms = Arrays.asList(roomsResponse.getBody());
            logger.info("Rooms found: {}", rooms.size());

            // Fetch users
            ResponseEntity<Map[]> usersResponse = restTemplate.getForEntity(
                "http://user-service:8081/api/users", 
                Map[].class
            );
            List<Map> users = Arrays.asList(usersResponse.getBody());
            logger.info("Users found: {}", users.size());

            // Create maps for easy access to room and user names
            Map<Long, String> roomNames = rooms.stream()
                .collect(Collectors.toMap(
                    room -> Long.valueOf(room.get("id").toString()),
                    room -> room.get("name").toString()
                ));
            
            Map<Long, String> userNames = users.stream()
                .collect(Collectors.toMap(
                    user -> Long.valueOf(user.get("id").toString()),
                    user -> user.get("name").toString()
                ));
            
            // Add room and user names to reservations and format dates
            for (Map reservation : reservations) {
                if (reservation.get("roomId") == null || reservation.get("userId") == null) {
                    logger.error("Reservation with missing data: {}", reservation);
                    continue;
                }
                
                Long roomId = Long.valueOf(reservation.get("roomId").toString());
                Long userId = Long.valueOf(reservation.get("userId").toString());
                
                reservation.put("roomName", roomNames.getOrDefault(roomId, "Room not found"));
                reservation.put("userName", userNames.getOrDefault(userId, "User not found"));
                
                // Format date for display
                if (reservation.get("dateTime") != null) {
                    try {
                        String dateTimeStr = reservation.get("dateTime").toString();
                        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
                        reservation.put("formattedDateTime", dateTime.format(DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.error("Error formatting date: {}", e.getMessage());
                        reservation.put("formattedDateTime", reservation.get("dateTime").toString());
                    }
                }
                
                // Handle start and end times if they exist
                if (reservation.get("startDateTime") != null) {
                    try {
                        String startTimeStr = reservation.get("startDateTime").toString();
                        LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
                        reservation.put("formattedStartTime", startTime.format(DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.error("Error formatting start time: {}", e.getMessage());
                        reservation.put("formattedStartTime", reservation.get("startDateTime").toString());
                    }
                }
                
                if (reservation.get("endDateTime") != null) {
                    try {
                        String endTimeStr = reservation.get("endDateTime").toString();
                        LocalDateTime endTime = LocalDateTime.parse(endTimeStr);
                        reservation.put("formattedEndTime", endTime.format(DATE_FORMATTER));
                    } catch (DateTimeParseException e) {
                        logger.error("Error formatting end time: {}", e.getMessage());
                        reservation.put("formattedEndTime", reservation.get("endDateTime").toString());
                    }
                }
            }
            
            // Marcar salas como ocupadas se houver conflito de período
            for (Map room : rooms) {
                Long roomId = Long.valueOf(room.get("id").toString());
                boolean occupied = false;
                String occupiedBy = null;
                String occupiedUntil = null;
                LocalDateTime now = LocalDateTime.now();

                // Check all reservations for this room
                for (Map reservation : reservations) {
                    if (reservation.get("roomId") == null || reservation.get("startDateTime") == null || reservation.get("endDateTime") == null) {
                        continue;
                    }
                    
                    Long reservationRoomId = Long.valueOf(reservation.get("roomId").toString());
                    if (!reservationRoomId.equals(roomId)) {
                        continue;
                    }
                    
                    try {
                        LocalDateTime start = LocalDateTime.parse(reservation.get("startDateTime").toString());
                        LocalDateTime end = LocalDateTime.parse(reservation.get("endDateTime").toString());
                        
                        // Check if current time is within reservation period
                        if (now.isEqual(start) || now.isEqual(end) || 
                            (now.isAfter(start) && now.isBefore(end))) {
                            occupied = true;
                            occupiedBy = reservation.get("userName").toString();
                            occupiedUntil = end.format(DATE_FORMATTER);
                            break;
                        }
                    } catch (DateTimeParseException e) {
                        logger.error("Error parsing reservation period: {}", e.getMessage());
                    }
                }
                
                room.put("available", !occupied);
                if (occupied) {
                    room.put("occupiedBy", occupiedBy);
                    room.put("occupiedUntil", occupiedUntil);
                }
            }

            model.addAttribute("reservations", reservations);
            model.addAttribute("rooms", rooms);
            model.addAttribute("users", users);
            
            // Add success/error messages
            String success = (String) model.getAttribute("success");
            String error = (String) model.getAttribute("error");
            if (success != null) {
                model.addAttribute("message", success);
                model.addAttribute("messageType", "success");
            } else if (error != null) {
                model.addAttribute("message", error);
                model.addAttribute("messageType", "danger");
            }
            
            return "reservations";
        } catch (Exception e) {
            logger.error("Error loading data: {}", e.getMessage(), e);
            model.addAttribute("error", "Error loading data: " + e.getMessage());
            return "reservations";
        }
    }

    @PostMapping("/save")
    public String save(
            @RequestParam String roomId,
            @RequestParam String userId,
            @RequestParam String startDateTime,
            @RequestParam String endDateTime,
            Model model) {
        try {
            logger.info("Data received - RoomId: {}, UserId: {}, StartDateTime: {}, EndDateTime: {}", roomId, userId, startDateTime, endDateTime);

            Map<String, Object> reservation = new HashMap<>();
            reservation.put("roomId", Long.parseLong(roomId));
            reservation.put("userId", Long.parseLong(userId));
            reservation.put("startDateTime", startDateTime + ":00");
            reservation.put("endDateTime", endDateTime + ":00");

            logger.info("Sending request to create reservation: {}", reservation);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://reservation-service:8083/api/reservations",
                reservation,
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Reservation created successfully");
                model.addAttribute("success", "Reservation created successfully");
            } else {
                logger.error("Error creating reservation. Status: {}", response.getStatusCode());
                model.addAttribute("error", "Error creating reservation");
            }

            return "redirect:/reservations";
        } catch (Exception e) {
            logger.error("Error creating reservation: {}", e.getMessage(), e);
            model.addAttribute("error", "Error creating reservation: " + e.getMessage());
            return "redirect:/reservations";
        }
    }

    @PostMapping("/users")
    public String createUser(
            @RequestParam String name,
            @RequestParam String email,
            Model model) {
        
        try {
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://user-service:8081/api/users",
                user,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                model.addAttribute("success", "User created successfully");
            } else {
                model.addAttribute("error", "Error creating user");
            }

            return "redirect:/reservations";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating user: " + e.getMessage());
            return "redirect:/reservations";
        }
    }

    @PostMapping("/rooms")
    public String createRoom(
            @RequestParam String name,
            @RequestParam int capacity,
            Model model) {
        
        try {
            Map<String, Object> room = new HashMap<>();
            room.put("name", name);
            room.put("capacity", capacity);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://room-service:8082/api/rooms",
                room,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                model.addAttribute("success", "Room created successfully");
            } else {
                model.addAttribute("error", "Error creating room");
            }

            return "redirect:/reservations";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating room: " + e.getMessage());
            return "redirect:/reservations";
        }
    }

    @PostMapping(value = "/save", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveJson(@RequestBody Map<String, Object> reservationJson, Model model) {
        Map<String, String> response = new HashMap<>();
        
        try {
            logger.info("Received reservation data (JSON): {}", reservationJson);

            // Forward the request to the reservation service
            ResponseEntity<Map> serviceResponse = restTemplate.postForEntity(
                "http://reservation-service:8083/api/reservations",
                reservationJson,
                Map.class
            );

            if (serviceResponse.getStatusCode().is2xxSuccessful()) {
                logger.info("Reservation created successfully (JSON)");
                response.put("status", "success");
                response.put("message", "Reservation created successfully");
                return ResponseEntity.ok(response);
            } else {
                logger.error("Error creating reservation (JSON). Status: {}", serviceResponse.getStatusCode());
                response.put("status", "error");
                response.put("message", "Error creating reservation");
                return ResponseEntity.status(serviceResponse.getStatusCode()).body(response);
            }
        } catch (Exception e) {
            logger.error("Error creating reservation (JSON): {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Error creating reservation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        
        try {
            restTemplate.delete("http://reservation-service:8083/api/reservations/" + id);
            
            response.put("status", "success");
            response.put("message", "Reservation deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error deleting reservation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteRoom(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        
        try {
            restTemplate.delete("http://room-service:8082/api/rooms/" + id);
            
            response.put("status", "success");
            response.put("message", "Room deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error deleting room: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> editRoom(
            @PathVariable Long id,
            @RequestBody Map<String, Object> roomJson) {
        Map<String, String> response = new HashMap<>();
        
        try {
            restTemplate.put("http://room-service:8082/api/rooms/" + id, roomJson);
            
            response.put("status", "success");
            response.put("message", "Room updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error updating room: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        
        try {
            restTemplate.delete("http://user-service:8081/api/users/" + id);
            
            response.put("status", "success");
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error deleting user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> editUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> userJson) {
        Map<String, String> response = new HashMap<>();
        
        try {
            restTemplate.put("http://user-service:8081/api/users/" + id, userJson);
            
            response.put("status", "success");
            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error updating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/users", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createUserJson(@RequestBody Map<String, Object> userJson) {
        Map<String, String> response = new HashMap<>();
        
        try {
            logger.info("Received user data (JSON): {}", userJson);
            
            // Make sure active is set if not provided
            if (!userJson.containsKey("active")) {
                userJson.put("active", true);
            }

            // Rename registration to registrationNumber if needed
            if (userJson.containsKey("registration") && !userJson.containsKey("registrationNumber")) {
                userJson.put("registrationNumber", userJson.get("registration"));
                userJson.remove("registration");
                logger.info("Renamed registration to registrationNumber: {}", userJson);
            }

            // Forward the request to the user service
            logger.info("Sending user data to user-service: {}", userJson);
            ResponseEntity<Map> serviceResponse = restTemplate.postForEntity(
                "http://user-service:8081/api/users",
                userJson,
                Map.class
            );

            if (serviceResponse.getStatusCode().is2xxSuccessful()) {
                logger.info("User created successfully (JSON)");
                response.put("status", "success");
                response.put("message", "User created successfully");
                return ResponseEntity.ok(response);
            } else {
                logger.error("Error creating user (JSON). Status: {}", serviceResponse.getStatusCode());
                response.put("status", "error");
                response.put("message", "Error creating user");
                return ResponseEntity.status(serviceResponse.getStatusCode()).body(response);
            }
        } catch (Exception e) {
            logger.error("Error creating user (JSON): {}", e.getMessage(), e);
            response.put("status", "error");
            response.put("message", "Error creating user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 