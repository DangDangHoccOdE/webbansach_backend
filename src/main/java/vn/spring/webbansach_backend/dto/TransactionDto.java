package vn.spring.webbansach_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class TransactionDto implements Serializable {
    private long amount;
    private String orderInfo;
}
