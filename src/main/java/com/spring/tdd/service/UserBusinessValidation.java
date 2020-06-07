package com.spring.tdd.service;

import com.spring.tdd.exception.BusinessValidationException;
import com.spring.tdd.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@Component
@AllArgsConstructor
public class UserBusinessValidation {
    private MessageSource messageSource;

    /***
     *
     * @param userDTO
     */
    public void businessCriteria(UserDTO userDTO) {
        // check user eligible for discount
        if(userDTO.getAge() < 35) {
            throw new BusinessValidationException(this.messageSource.getMessage("user.business.validation.code", null, Locale.ENGLISH),
                    this.messageSource.getMessage("age.business.validation.msg", null, Locale.ENGLISH));
        }

        // check user's occupation in the current list
        if(!Arrays.asList("Manager", "Architecture", "Tech Lead").contains(userDTO.getOccupation())) {
            throw new BusinessValidationException(this.messageSource.getMessage("user.business.validation.code", null, Locale.ENGLISH),
                    this.messageSource.getMessage("occupation.business.validation.msg", null, Locale.ENGLISH));
        }
    }
}
