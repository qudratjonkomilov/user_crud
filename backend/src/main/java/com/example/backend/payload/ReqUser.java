package com.example.backend.payload;

import com.example.backend.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqUser {
    private Long id;

    private String firstname; // REQUIRED

    private String lastname; // REQUIRED

    private String middlename;

    private Date dateOfBirth;

    private Gender gender; // REQUIRED

    private String nation;

    private String addressOfBirth;

    private String citizenship; // fiqaroligi

    private Date passportGivenTime;

    private String passportWhoGave;

    private String pinfl;

    private String phoneNumber; // REQUIRED

    private String photo;

    private String currentStatus;

    private String susceptibilityToDisease;

    private String propensityToAssassinate;

    private String weaknessesAndStrengths;

    private String socialResponsibility;

    private String positionToConform;

    private List<String> anotherPhotos;
}
