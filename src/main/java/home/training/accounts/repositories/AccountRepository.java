package home.training.accounts.repositories;

import home.training.accounts.entities.Account;
import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

//	Optional<Account> findByPhoneNumber(String phoneNumber);

	@Query("SELECT a FROM Account a WHERE a.customerId = :customerId")
	Optional<Account> findByCustomerId(@Param("customerId") Long customerId);

	/*
	 * TODO
	 * 
	 * @Query("SELECT a FROM Account a inner join Customer c WHERE a.customerId c.customerId and c.mobileNumber = :mobileNumber"
	 * ) Optional<Account> findByAccountNumber(@Param("mobileNumber") String
	 * customerId);
	 */

	Optional<Account> findByAccountNumber(Long accountNumber);

	/* @Query("DELETE FROM Account a WHERE a.customerId = :customerId") =>
	Without @Modifying, Spring will STILL try to execute it like a SELECT.

	👉 That leads to errors like:

	Not supported for DML operations
	or
	Query is not a select query
	*/
	
	//@Modifying => here not needed because methods state deleteBy
	//Spring already understands from the method name that this is a DELETE operation
	//You only need it when you explicitly write a query using @Query that modifies data.
	/*@Transactional
	@Modifying
	@Query("DELETE FROM Account a WHERE a.customerId = :customerId")
	void deleteAccountsByCustomerId(Long customerId);*/
	
	//Here Spring cannot infer it's a DELETE unless you tell it → so @Modifying is required.
	
	@Transactional
	@Modifying 
	int deleteByCustomerId(Long customerId);

}
