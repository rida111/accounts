package home.training.accounts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import home.training.accounts.dtos.AccountsContactInfoDto;
import home.training.accounts.dtos.CustomerDto;
import home.training.accounts.dtos.ResponseDto;
import home.training.accounts.services.AccountService;
import home.training.accounts.utils.AccountsConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

/**
 * AAccountController: add, remove account
 */
@RestController
@RequestMapping(value = "/v1/api/bank/accounts")
@Validated //telling sf to perform validations on all the REST endpoints inside this controller.
public class AccountController {

	  @Value("${build.version}")
	    private String buildVersion;

	 @Autowired
	 private Environment environment;
	    
    @Autowired
    private  AccountService accountService;
    
    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;
    

    @PostMapping("/new")
    public ResponseEntity<ResponseDto> addAccount(@Valid @RequestBody CustomerDto accountRequest) throws Exception {
    	

        accountService.addAccount(accountRequest);
        
    return  ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }
    
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
     
    	boolean isUpdated = accountService.updateAccount(customerDto);
        
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }
    
    
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> findByPhoneNumber(@RequestParam 
    		@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    		String phoneNum) throws Exception {

        CustomerDto foundCustomer =  accountService.findByPhoneNumber(phoneNum);
        
        
    return  ResponseEntity.status(HttpStatus.OK)
            .body(foundCustomer);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteByAccountNumber(@RequestParam 
    		@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    		String mobileNumber) throws Exception {

        boolean isDeleted = accountService.deleteAccountByMobileNumber(mobileNumber);
        
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }
    
    
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(buildVersion);
    }
    
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
    	System.out.println("java => " + environment.getProperty("JAVA_VERSION"));
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(environment.getProperty("JAVA_HOME"));
    }
    
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getAccountInfo() {
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(accountsContactInfoDto);
    }
    
    
    
}
