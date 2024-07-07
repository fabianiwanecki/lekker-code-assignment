package com.iwanecki.gamemonitoring.shared.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class NullOrNotBlankValidatorTest {


    @InjectMocks
    private NullOrNotBlankValidator nullOrNotBlankValidator;

    @Test
    void isValid_WithValueIsNull_ShouldReturnTrue() {
        boolean actual = nullOrNotBlankValidator.isValid(null, null);

        assertTrue(actual);
    }
    @Test
    void isValid_WithValueIsOnlySpace_ShouldReturnFalse() {
        boolean actual = nullOrNotBlankValidator.isValid("    ", null);

        assertFalse(actual);
    }
    @Test
    void isValid_WithValidValue_ShouldReturnTrue() {
        boolean actual = nullOrNotBlankValidator.isValid("TestValue", null);

        assertTrue(actual);
    }

}