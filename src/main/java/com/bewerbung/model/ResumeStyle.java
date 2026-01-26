package com.bewerbung.model;

public enum ResumeStyle {
    MODERN,
    PROFESSIONAL,
    CREATIVE;
    
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
