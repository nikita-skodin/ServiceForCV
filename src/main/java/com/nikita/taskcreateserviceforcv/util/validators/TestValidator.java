package com.nikita.taskcreateserviceforcv.util.validators;

import com.nikita.taskcreateserviceforcv.entities.Test;
import com.nikita.taskcreateserviceforcv.services.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TestValidator implements Validator {

    private final TestService testService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Test.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Test test = (Test) target;

        Optional<Test> testOptional = testService.findByTitle(test.getTitle());

        if (testOptional.isPresent() && !Objects.equals(testOptional.get().getId(), test.getId())) {
            errors.rejectValue("title", "400",
                    "Test with name " + test.getTitle() + " is already exist");
        }

    }
}
