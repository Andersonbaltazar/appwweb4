package com.pcfinal.apiapp.dto;

public class MessageResponseDTO {
    
    private String message;
    
    // Constructores
    public MessageResponseDTO() {}
    
    public MessageResponseDTO(String message) {
        this.message = message;
    }
    
    // Getters y Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
