package ru.practicum.shareit.booking.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {

    String message() default "My annotation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
