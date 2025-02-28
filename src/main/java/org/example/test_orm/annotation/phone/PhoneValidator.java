package org.example.test_orm.annotation.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.exception.TelephoneNumberException;

@Slf4j
public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Phonenumber.PhoneNumber number = PhoneNumberUtil.getInstance().parse(phone, null);
            if(String.valueOf(number.getNationalNumber()).length() > 10) {
                throw new TelephoneNumberException("The phone number is incorrect");
            }
            return true;

        }   catch (NumberParseException e) {
            throw new TelephoneNumberException(e.getErrorType().name(), e);
        }
    }
}