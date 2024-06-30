package vn.spring.webbansach_backend.utils;

public class PasswordRegex {
    public static boolean passwordRegex(String password){
        String regex= "^(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$";

        if (password.matches(regex)) return false;
        else return true;
    }
}
