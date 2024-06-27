package vn.spring.webbansach_backend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmailDto {
    @Pattern(regexp = "^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$",message = "Định dạng email không hợp lệ!")
    private String email;

    @Pattern(regexp = "^((?!\\.)[\\w-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$",message = "Định dạng email mới không hợp lệ!")
    private String newEmail;

}
