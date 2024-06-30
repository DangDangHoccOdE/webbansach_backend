package vn.spring.webbansach_backend.utils;

public class MaskEmail {
    public static String maskEmail(String email){
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];

        // Tách phần domain name và domain extension
        String[] domainParts = domainPart.split("\\.");
        String domainName = domainParts[0];
        String domainExtension = domainParts[1];

        // Mã hóa phần local part của email
        String maskedLocalPart = localPart.substring(0, 3) + "*".repeat(localPart.length() - 3);

        // Mã hóa phần domain name của email
        String maskedDomainName = "*".repeat(domainName.length());

        // Trả về email đã mã hóa
        return maskedLocalPart + "@" + maskedDomainName + "." + domainExtension;
    }
}
