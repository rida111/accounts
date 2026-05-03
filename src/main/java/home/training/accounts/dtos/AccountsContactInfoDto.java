package home.training.accounts.dtos;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "accounts") //for loans ms prefix = "loans" and same for cards ms, prefix= "cards"
@Getter
@Setter
public class AccountsContactInfoDto {
    private String message;

    private  Map<String, String> contactDetails;

    private  List<String> onCallSupport;


	
	
}
