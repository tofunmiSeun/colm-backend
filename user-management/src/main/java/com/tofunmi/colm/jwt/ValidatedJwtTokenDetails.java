package com.tofunmi.colm.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created By tofunmi on 12/07/2022
 */
@Data
@AllArgsConstructor
public class ValidatedJwtTokenDetails {
    private String email;
    private String fullName;
    private String firstName;
}
