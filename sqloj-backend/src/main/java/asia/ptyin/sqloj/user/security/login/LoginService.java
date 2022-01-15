package asia.ptyin.sqloj.user.security.login;

public interface LoginService
{
    /**
     * @param username username of user
     * @param password plain password of user
     * @return if login successfully then true, otherwise false.
     */
    boolean login(String username, String password);
}
