//package com.fsocial.accountservice.config;
//
//import com.poly.backend.service.admin.GoogleService;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class CustomJwtFilter extends OncePerRequestFilter {
//    @Value("${jwt.privateKey}")
//    private String secretKey;
//    @Autowired
//    GoogleService googleService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = getTokenFormReq(request);
//        if (request.getRequestURI().startsWith("/oauth2/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        if (token != null) {
//            if (verifyToken(token)) {
//                Map<String, Object> data = parserToken(token);
//                if (data != null && !data.isEmpty()) {
//                    List<GrantedAuthority> authorities = new ArrayList<>();
//                    authorities.add(new SimpleGrantedAuthority("ROLE_" + data.get("role")));
//                    SecurityContextHolder.getContext()
//                            .setAuthentication(new UsernamePasswordAuthenticationToken(data.get("sub"), "", authorities));
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    public Map<String, Object> parserToken(String token) {
//        try {
//            Map<String, Object> claimsMap = new HashMap<>();
//            for (Map.Entry<String, Object> entry : Jwts.parser()
//                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .entrySet()) {
//                claimsMap.put(entry.getKey(), entry.getValue());
//            }
//            return claimsMap;
//        } catch (ExpiredJwtException ex) {
//            Map<String, Object> claimsMap = new HashMap<>();
//            for (Map.Entry<String, Object> entry : ex.getClaims().entrySet()) {
//                claimsMap.put(entry.getKey(), entry.getValue());
//            }
//            return claimsMap;
//        } catch (JwtException ex) {
//            System.out.println("Lỗi JWT: " + ex.getMessage());
//            throw ex;
//        }
//    }
//
//    public String getTokenFormReq(HttpServletRequest request) {
//        String header = request.getHeader("Authorization");
//        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
//            return header.substring(7);
//        }
//        return null;
//    }
//
//    public boolean verifyToken(String token) {
//        try {
//            Jwts.parser()
//                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}