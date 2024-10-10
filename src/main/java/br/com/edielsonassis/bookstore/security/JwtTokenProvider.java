package br.com.edielsonassis.bookstore.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.edielsonassis.bookstore.dtos.v1.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.bookstore.dtos.v1.response.TokenResponse;
import br.com.edielsonassis.bookstore.security.exceptions.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtTokenProvider {

    private static final String CLAIM_ROLES = "roles";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expiration-length}")
    private int expirationToken;

    @Value("${security.jwt.token.refresh-expiration-length}")
    private int refreshExpirationToken;

    private final UserDetailsService userDetailsService;
    private Algorithm algorithm;

	public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenAndRefreshTokenResponse createAccessTokenRefreshToken(String username, List<String> roles) {
		log.info("Generating access token for user: {}", username);
        Instant now = Instant.now();
        Instant expiration = calculateExpirationToken();

        var accessToken = createToken(username, roles, now, expiration);
        var refreshToken = createRefreshToken(username, roles, now);
		log.debug("Access token and refresh token generated for user: {}", username);
        return new TokenAndRefreshTokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(String refreshToken, String username) {
        refreshToken = stripBearerPrefix(refreshToken);
        DecodedJWT decodedJWT = verifyToken(refreshToken);
        String user = decodedJWT.getSubject();
		verifyingUsername(username, decodedJWT);
        List<String> roles = decodedJWT.getClaim(CLAIM_ROLES).asList(String.class);
		log.debug("Token refreshed for user: {}", user);
        return createAccessToken(user, roles);
    }

    public Authentication getAuthentication(String token) {
		log.info("Getting authentication from token");
        DecodedJWT decodedJWT = verifyToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        log.debug("User authenticated: {}", decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
		log.info("Resolving token from request header");
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
		log.info("Validating token");
        DecodedJWT decodedJWT = verifyToken(token);
        return !decodedJWT.getExpiresAt().before(new Date());
    }

	private TokenResponse createAccessToken(String username, List<String> roles) {
		log.info("Generating access token for user: {}", username);
        Instant now = Instant.now();
        Instant expiration = calculateExpirationToken();

        var accessToken = createToken(username, roles, now, expiration);
		log.debug("Access token and refresh token generated for user: {}", username);
        return new TokenResponse(accessToken);
    }

    private String createToken(String username, List<String> roles, Instant now, Instant expiration) {
        log.info("Creating access token for user: {}", username);
		String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim(CLAIM_ROLES, roles)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    private String createRefreshToken(String username, List<String> roles, Instant now) {
        log.info("Creating refresh token for user: {}", username);
		Instant expiration = calculateExpirationRefreshToken();
        return JWT.create()
                .withClaim(CLAIM_ROLES, roles)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withSubject(username)
                .sign(algorithm);
    }

    private Instant calculateExpirationToken() {
		log.info("Calculating expiration time for access token");
        return LocalDateTime.now().plusHours(expirationToken).toInstant(ZoneOffset.UTC);
    }

    private Instant calculateExpirationRefreshToken() {
		log.info("Calculating expiration time for refresh token");
        return LocalDateTime.now().plusDays(refreshExpirationToken).toInstant(ZoneOffset.UTC);
    }

    private DecodedJWT verifyToken(String token) {
		log.info("Decoding token");
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private String stripBearerPrefix(String token) {
        if (token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return token;
    }

	private void verifyingUsername(String username, DecodedJWT decodedJWT) {
		String tokenUsername = decodedJWT.getSubject();
		if (!username.equals(tokenUsername)) {
			log.error("Error during refresh token generation.");
			throw new InvalidJwtAuthenticationException("Failed to refresh the token.");
		}
	}
}