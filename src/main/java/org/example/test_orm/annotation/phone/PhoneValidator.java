package org.example.test_orm.annotation.phone;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.test_orm.exception.TelephoneNumberException;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        try {
            PhoneNumberUtil.getInstance().parse(phone, null);   // TODO передалать позже (Самвел)
            return true;
        }   catch (NumberParseException e) {
            throw new TelephoneNumberException(e.getErrorType().name(), e);
        }
    }
}