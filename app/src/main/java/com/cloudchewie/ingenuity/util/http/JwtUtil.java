/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.http;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

import io.jsonwebtoken.MalformedJwtException;

public class JwtUtil {

    /**
     * 获取Jwt的负载
     */
    public static Map<String, Claim> getPayload(String token) {
        try {
            return JWT.decode(token).getClaims();
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Illegal token format!");
        }
    }

    public static int getAud(String token) {
        try {
            return Integer.parseInt(JWT.decode(token).getAudience().get(0));
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Illegal token format!");
        }
    }
}
