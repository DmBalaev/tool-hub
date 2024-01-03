package dm.dm.toolservice.auth;

import dm.dm.toolservice.auth.AuthRequest;
import dm.dm.toolservice.auth.AuthResponse;
import dm.dm.toolservice.auth.RegistrationRequest;

public interface AuthService {
    AuthResponse signup(RegistrationRequest request);

    AuthResponse signin(AuthRequest request);
}
