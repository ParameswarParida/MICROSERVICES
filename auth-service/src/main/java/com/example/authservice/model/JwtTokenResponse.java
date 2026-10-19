package com.example.authservice.model;

public class JwtTokenResponse {

    private String token;
    private String refreshToken;
    private String tokenType;
    private String refreshTokenType;
    private String tokenExpiresIn;
    private String refreshTokenExpiresIn;

    @Override
    public String toString() {
        return "JwtTokenResponse{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", refreshTokenType='" + refreshTokenType + '\'' +
                ", tokenExpiresIn='" + tokenExpiresIn + '\'' +
                ", refreshTokenExpiresIn='" + refreshTokenExpiresIn + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshTokenType() {
        return refreshTokenType;
    }

    public void setRefreshTokenType(String refreshTokenType) {
        this.refreshTokenType = refreshTokenType;
    }

    public String getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void setTokenExpiresIn(String tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public String getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setRefreshTokenExpiresIn(String refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }
}
