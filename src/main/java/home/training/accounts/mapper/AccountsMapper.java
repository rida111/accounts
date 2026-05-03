package home.training.accounts.mapper;

import java.time.LocalDateTime;

import home.training.accounts.dtos.AccountsDto;
import home.training.accounts.entities.Account;

/*
maps dto to entity and vice versa.
IMPORTANT NOTE => The Mapper is typically used right before saving to the database
 */
public class AccountsMapper {

    public static AccountsDto mapToAccountsDto(Account account, AccountsDto accountsDto) {
        accountsDto.setAccountNumber(account.getAccountNumber());
        accountsDto.setAccountType(account.getAccountType());
        accountsDto.setBranchAddress(account.getBranchAddress());
        return accountsDto;
    }

    public static Account mapToAccounts(AccountsDto accountsDto, Account account, String loginUser) {
        account.setAccountNumber(accountsDto.getAccountNumber());
        account.setAccountType(accountsDto.getAccountType());
        account.setBranchAddress(accountsDto.getBranchAddress());
		/*
		 * //audit fields: account.setCreatedAt(LocalDateTime.now());
		 * account.setCreatedBy(loginUser); account.setUpdatedAt(LocalDateTime.now());
		 * account.setUpdatedBy(loginUser);
		 */
        
        return account;
    }
}