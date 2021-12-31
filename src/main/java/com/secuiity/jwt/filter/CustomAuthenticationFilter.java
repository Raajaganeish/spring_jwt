package com.secuiity.jwt.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		return this.authenticationManager.authenticate(authenticationToken);
//		return super.attemptAuthentication(request, response);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
//		super.successfulAuthentication(request, response, chain, authResult);
		String domain = request.getScheme() + "://" + // "http" + "://
				request.getServerName() + // "myhost"
				":" + request.getServerPort();
		Map<String, String> hmap = new HashMap<String, String>();
		hmap.put("signedAt", new SimpleDateFormat("MM.dd.yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));

		User user = (User) authResult.getPrincipal();
		Algorithm algorithm = Algorithm.HMAC256("secret");
		String access_token = JWT.create().withSubject(user.getUsername()).withPayload(hmap)
				.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)).withIssuer(domain)
				.withClaim("roles",
						user.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList()))
				.sign(algorithm);
		String refresh_token = JWT.create().withSubject(user.getUsername()).withPayload(hmap)
				.withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000)).withIssuer(domain)
				.withClaim("roles",
						user.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList()))
				.sign(algorithm);

		Cookie c1 = new Cookie("access_token", access_token);
		c1.setHttpOnly(true);
		c1.setSecure(true);

		Cookie c2 = new Cookie("refresh_token", refresh_token);
		c2.setHttpOnly(true);
		c2.setSecure(true);
		response.addCookie(c1);
		response.addCookie(c2);

		response.setHeader("access_token", access_token);
		response.setHeader("refresh_token", refresh_token);
	}

	@Override
	protected AuthenticationManager getAuthenticationManager() {
		// TODO Auto-generated method stub
		return super.getAuthenticationManager();
	}

}
