package home.training.accounts.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class NewAccountDto {

    private String customerId;

    private String accounttype;

    private String branchAddress;
}
