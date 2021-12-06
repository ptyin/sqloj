package asia.ptyin.sqloj.auth.user;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmpty(errors, "username", "username.empty");
        UserDto user = (UserDto) target;
    }

    private void validateOnUsername(Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty");
//        ValidationUtils.rejec
    }
}
