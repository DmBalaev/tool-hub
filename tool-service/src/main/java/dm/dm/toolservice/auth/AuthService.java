package dm.dm.toolservice.auth;

public interface AuthService {
    AuthResponse signup(RegistrationRequest request);
    AuthResponse signin(AuthRequest request);
}
