package com.nikita.taskcreateserviceforcv.util.validators;

import com.nikita.taskcreateserviceforcv.entities.Area;
import com.nikita.taskcreateserviceforcv.services.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AreaValidator implements Validator {

    private final AreaService areaService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Area.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Area area = (Area) target;

        Optional<Area> areaOptional = areaService.findByTitle(area.getTitle());

        if (areaOptional.isPresent() && !Objects.equals(areaOptional.get().getId(), area.getId())) {
            errors.rejectValue("title", "400",
                    "Area with name " + area.getTitle() + " is already exist");
        }

    }
}
