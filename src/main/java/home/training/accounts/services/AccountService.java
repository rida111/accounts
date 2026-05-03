package home.training.accounts.services;

import home.training.accounts.dtos.AccountsDto;
import home.training.accounts.dtos.CustomerDto;
import home.training.accounts.entities.Account;
import home.training.accounts.entities.Customer;
import home.training.accounts.exceptions.CustomerAlreadyExistsException;
import home.training.accounts.exceptions.ResourceNotFoundException;
import home.training.accounts.mapper.AccountsMapper;
import home.training.accounts.mapper.CustomerMapper;
import home.training.accounts.repositories.AccountRepository;
import home.training.accounts.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.security.auth.login.AccountNotFoundException;

/**
 ***** Ideal flow in a service method **********
 * 1. Validate input (basic checks)
2. Apply business rules (can we do this?)
3. Fetch existing data (if needed)
4. 👉 Map DTO → Entity (HERE)
5. Save to database
 */

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void addAccount(CustomerDto newCustomerDto) throws BadRequestException {

        // 1. Validate input
        if (newCustomerDto == null
                || StringUtils.isBlank(newCustomerDto.getMobileNumber())
                || StringUtils.isBlank(newCustomerDto.getName())
                || StringUtils.isBlank(newCustomerDto.getEmail())) {
            throw new BadRequestException("Customer data is invalid or empty");
        }

        // 2. Check for existing customer
        customerRepository.findByMobileNumber(newCustomerDto.getMobileNumber())
                .ifPresent(c -> {
                    throw new CustomerAlreadyExistsException(
                            "Customer already registered with mobile number " + newCustomerDto.getMobileNumber()
                    );
                });

        // 3. Map DTO to entity
        Customer newCustomer = CustomerMapper.mapToCustomer(newCustomerDto, new Customer());

        // 4. Save customer
        Customer savedCustomer = customerRepository.save(newCustomer);

        // 5. Create and save account for this customer
        Account newAccount = createNewAccount(savedCustomer);
        accountRepository.save(newAccount);
    }
    
    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        
        AccountsDto accountsDto = customerDto.getAccountsDto();
        
        if(accountsDto !=null ){
            Account account = accountRepository.findByAccountNumber(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, account, "System");
            account = accountRepository.save(account);

            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }
    

    public CustomerDto findByPhoneNumber(String phoneNumber) {
    	
    	
   	   Customer customer = customerRepository.findByMobileNumber(phoneNumber).orElseThrow(
             () -> new ResourceNotFoundException("Customer", "mobileNumber", phoneNumber));
   	 
   	   
   	 Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
             () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
   	 
   	 CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
     customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
     return customerDto;
    	
    }
    
    private Account createNewAccount(Customer customer) {
        Account account = new Account();
        account.setCustomerId(customer.getCustomerId());
        account.setAccountType("SAVINGS"); // default account type
        account.setBranchAddress("Main Branch"); // default branch
        account.setAccountNumber(generateAccountNumber()); // unique account number
        //set => audit fields:
        
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy("System");
        return account;
    }

    private Long generateAccountNumber() {
        // Example: generate random 10-digit number (production may need more robust approach)
        return ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L);
    }

	public boolean deleteAccountByMobileNumber(String phoneNumber) {
		
		 if(!StringUtils.isEmpty(phoneNumber)) {
			   Customer customer = customerRepository.findByMobileNumber(phoneNumber).orElseThrow(
		               () -> new ResourceNotFoundException("Customer", "mobileNumber", phoneNumber));
			   
			   	accountRepository.deleteByCustomerId(customer.getCustomerId());
		        customerRepository.deleteById(customer.getCustomerId());
		        return true;
			  
		 }
	  	
		 
		return false;
	}
}
