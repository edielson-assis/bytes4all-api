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

import br.com.edielsonassis.bookstore.dtos.v1.response.TokenJWT;
import br.com.edielsonassis.bookstore.security.exceptions.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider  {

	@Value("${security.jwt.token.secret-key}")
	private String secretKey;
	
	@Value("${security.jwt.token.expire-length}")
	private long exprirationToken;

    @Value("${security.jwt.token.refresh-expire-length}")
	private long refreshExprirationToken;
	
	private final UserDetailsService userDetailsService;
	private Algorithm algorithm;
    private static final String CLAIM = "roles";
	
	public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}

	public TokenJWT createAccessToken(String username, List<String> roles) {
		Instant now = Instant.now();
		Instant expiration = exprirationToken();
		var accessToken = getAccessToken(username, roles, now, expiration);
		var refreshToken = getRefreshToken(username, roles, now);
		
		return new TokenJWT(username, true, now, expiration, accessToken, refreshToken);
	}

	
	public TokenJWT refreshToken(String refreshToken) {
		if (refreshToken.contains("Bearer ")) {
            refreshToken = refreshToken.substring("Bearer ".length());
        }		
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(refreshToken);
		String username = decodedJWT.getSubject();

		var roles = decodedJWT.getClaim(CLAIM).asList(String.class);

		return createAccessToken(username, roles);
	}

    public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

    public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}
		return null;
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decodedJWT = decodedToken(token);
		try {
			if (decodedJWT.getExpiresAt().before(new Date())) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
		}
	}
	
	private String getAccessToken(String username, List<String> roles, Instant now, Instant expiration) {
		String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		return JWT.create()
				.withClaim(CLAIM, roles)
				.withIssuedAt(now)
				.withExpiresAt(expiration)
				.withSubject(username)
				.withIssuer(issuerUrl)
				.sign(algorithm)
				.strip();
	}
	
	private String getRefreshToken(String username, List<String> roles, Instant now) {
		Instant validityRefreshToken = exprirationRefreshToken();
		return JWT.create()
				.withClaim(CLAIM, roles)
				.withIssuedAt(now)
				.withExpiresAt(validityRefreshToken)
				.withSubject(username)
				.sign(algorithm)
				.strip();
	}

	private DecodedJWT decodedToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier verifier = JWT.require(algorithm).build();
		return verifier.verify(token);
	}

    private Instant exprirationToken() {
        return LocalDateTime.now().plusHours(exprirationToken).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant exprirationRefreshToken() {
        return LocalDateTime.now().plusHours(refreshExprirationToken).toInstant(ZoneOffset.of("-03:00"));
    }
}