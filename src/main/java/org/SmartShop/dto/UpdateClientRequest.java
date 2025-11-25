package org.SmartShop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateClientRequest {

    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @Email(message = "Format d'email invalide")
    private String email;

    public UpdateClientRequest() {}

    public UpdateClientRequest(String nom, String email) {
        this.nom = nom;
        this.email = email;
    }

    // Getters and Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String nom;
        private String email;

        public Builder nom(String nom) { this. nom = nom; return this; }
        public Builder email(String email) { this.email = email; return this; }

        public UpdateClientRequest build() {
            return new UpdateClientRequest(nom, email);
        }
    }
}