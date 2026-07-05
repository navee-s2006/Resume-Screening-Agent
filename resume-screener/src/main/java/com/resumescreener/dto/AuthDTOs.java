package com.resumescreener.dto;

public class AuthDTOs {

    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String fullName;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class ProfileUpdateRequest {
        private String fullName;
        private String phone;
        private String college;
        private String degree;
        private String graduationYear;
        private String location;
        private String bio;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getCollege() { return college; }
        public void setCollege(String college) { this.college = college; }

        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }

        public String getGraduationYear() { return graduationYear; }
        public void setGraduationYear(String graduationYear) { this.graduationYear = graduationYear; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
    }
}
