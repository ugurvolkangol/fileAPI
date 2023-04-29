package com.etstur.fileapi.common;

public class SecurityConstants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 600_000; // 10 minutes
    public static final String TOKEN_SECRET = "secret";
}