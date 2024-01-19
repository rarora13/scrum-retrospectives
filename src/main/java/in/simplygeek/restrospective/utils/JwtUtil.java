package in.simplygeek.restrospective.utils;

import java.util.Optional;

import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtil {
    private static final String AUTHORISATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public static Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORISATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return Optional.of(bearerToken.substring(TOKEN_PREFIX.length()));
        }
        return Optional.empty();
    }
}