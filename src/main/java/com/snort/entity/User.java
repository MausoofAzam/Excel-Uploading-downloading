package com.snort.entity;

import com.snort.helper.EncryptionHelper;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String designation;

    public void setFirstName(String firstName) {
        this.firstName = EncryptionHelper.encrypt(firstName);
    }

    public String getFirstName() {
        return EncryptionHelper.decrypt(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = EncryptionHelper.encrypt(lastName);
    }

    public String getLastName() {
        return EncryptionHelper.decrypt(lastName);
    }

    public void setEmail(String email) {
        this.email = EncryptionHelper.encrypt(email);
    }

    public String getEmail() {
        return EncryptionHelper.decrypt(email);
    }

    public void setDesignation(String designation) {
        this.designation = EncryptionHelper.encrypt(designation);
    }

    public String getDesignation() {
        return EncryptionHelper.decrypt(designation);
    }
}
