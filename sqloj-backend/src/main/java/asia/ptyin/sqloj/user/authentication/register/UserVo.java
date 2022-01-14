package asia.ptyin.sqloj.user.authentication.register;

import asia.ptyin.sqloj.user.authentication.login.UserLoginDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User Validation Object
 */
@Deprecated
public class UserVo implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return UserLoginDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmpty(errors, "username", "username.empty");
        UserLoginDto user = (UserLoginDto) target;
    }

    private void validateOnUsername(Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty");
//        ValidationUtils.rejec
    }
}
