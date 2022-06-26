package io.login.client.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class UserOtpDetails {
    private String userName;
    private String otp;
    private String createdAt;

    public UserOtpDetails() {
    }

    public UserOtpDetails(String userName, String otp, String createdAt) {
        this.userName = userName;
        this.otp = otp;
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
