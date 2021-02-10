package com.meritamerica.capstone.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.capstone.exceptions.CannotCloseAccountException;
import com.meritamerica.capstone.exceptions.ExceedsAvailableBalanceException;
import com.meritamerica.capstone.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.capstone.exceptions.MaxAccountsReachedException;
import com.meritamerica.capstone.exceptions.NegativeAmountException;
import com.meritamerica.capstone.exceptions.NotFoundException;
import com.meritamerica.capstone.exceptions.TransactionNotAllowedException;
import com.meritamerica.capstone.exceptions.UsernameAlreadyExistsException;
import com.meritamerica.capstone.models.AccountHolder;
import com.meritamerica.capstone.models.AuthenticationRequest;
import com.meritamerica.capstone.models.AuthenticationResponse;
import com.meritamerica.capstone.models.BankAccount;
import com.meritamerica.capstone.models.CDAccount;
import com.meritamerica.capstone.models.CDOffering;
import com.meritamerica.capstone.models.CheckingAccount;
import com.meritamerica.capstone.models.DBACheckingAccount;
import com.meritamerica.capstone.models.RegularIRA;
import com.meritamerica.capstone.models.RolloverIRA;
import com.meritamerica.capstone.models.RothIRA;
import com.meritamerica.capstone.models.SavingsAccount;
import com.meritamerica.capstone.models.Transaction;
import com.meritamerica.capstone.repositories.AccountHolderRepository;
import com.meritamerica.capstone.repositories.BankAccountRepository;
import com.meritamerica.capstone.repositories.CDOfferingRepository;
import com.meritamerica.capstone.repositories.TransactionRepository;
import com.meritamerica.capstone.services.JwtUtil;
import com.meritamerica.capstone.services.MyUserDetailsService;

@RestController
public class MeritBankController {
	public static void enforceFound(AccountHolder a) throws NotFoundException {
		enforceFound(a, true);
	}

	public static void enforceFound(AccountHolder a, boolean active) throws NotFoundException {
		if (a == null) {
			throw new NotFoundException();
		}
		if (!a.isActive() == active) {
			throw new NotFoundException();
		}
	}

	public static void enforceFound(BankAccount a) throws NotFoundException {
		enforceFound(a, true);
	}

	public static void enforceFound(BankAccount a, boolean active) throws NotFoundException {
		if (a == null) {
			throw new NotFoundException();
		}
		if (!a.isActive() == active) {
			throw new NotFoundException();
		}
	}

	public static void enforceFound(CDOffering a) throws NotFoundException {
		if (a == null) {
			throw new NotFoundException();
		}
	}

	public static void enforceFound(List<?> a) throws NotFoundException {
		if (a == null) {
			throw new NotFoundException();
		}
	}

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AccountHolderRepository accountHolderRepository;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private CDOfferingRepository cdOfferingRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@CrossOrigin
	@GetMapping(value = "/User/Transaction/{id}")
	public BankAccount accountHistory(@RequestHeader("Authorization") String auth, @PathVariable(name = "id") int id)
			throws NotFoundException {

		AccountHolder user = findUser(auth);
		enforceFound(user);

		BankAccount a = bankAccountRepository.findById(id);
		enforceFound(a);

		if (a.getUserId() != user.getId()) {
			throw new NotFoundException();
		}

		return a;
	}

	@CrossOrigin
	@GetMapping(value = "/Admin/Transaction/{id}/{activeNum}")
	public List<Transaction> accountHistory(@RequestHeader("Authorization") String auth,
			@PathVariable(name = "id") int id, @PathVariable(name = "activeNum") int activeNum)
			throws NotFoundException {
		boolean active = activeNum == 0 ? true : false;

		BankAccount a = bankAccountRepository.findById(id);
		enforceFound(a, active);

		return a.getTransactions();
	}

	@PostMapping("Admin/{id}/CDAccount/{cdo}")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addCDAccount(@PathVariable(name = "id") int id, @PathVariable(name = "cdo") int cdo,
			@RequestBody @Valid CDAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		return createAccount(id, a, cdo);
	}

	@PostMapping("User/CDAccount")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addCDAccount(@RequestHeader("Authorization") String auth, @RequestBody @Valid CDAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		AccountHolder user = findUser(auth);
		enforceFound(user);
		return createAccount(user, a);
	}

	@PostMapping("Admin/{id}/CheckingAccount")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addCheckingAccount(@PathVariable(name = "id") int id, @RequestBody @Valid CheckingAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		return createAccount(id, a);
	}

	@PostMapping("Admin/{id}/SavingsAccount")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addCheckingAccount(@PathVariable(name = "id") int id, @RequestBody @Valid SavingsAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		return createAccount(id, a);
	}

	@PostMapping("User/CheckingAccount")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addCheckingAccount(@RequestHeader("Authorization") String auth,
			@RequestBody @Valid CheckingAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		AccountHolder user = findUser(auth);
		enforceFound(user);
		return createAccount(user, a);
	}

	@PostMapping("Admin/{id}/DBACheckingAccount")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addDBACheckingAccount(@PathVariable(name = "id") int id,
			@RequestBody @Valid DBACheckingAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		return createAccount(id, a);
	}

	@PostMapping("User/DBACheckingAccount")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addDBACheckingAccount(@RequestHeader("Authorization") String auth,
			@RequestBody @Valid DBACheckingAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		AccountHolder user = findUser(auth);
		enforceFound(user);
		return createAccount(user, a);
	}

	@PostMapping("Admin/{id}/RegularIRA")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addRegularIRA(@PathVariable(name = "id") int id, @RequestBody @Valid RegularIRA a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		return createAccount(id, a);
	}

	@PostMapping("User/RegularIRA")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addRegularIRA(@RequestHeader("Authorization") String auth, @RequestBody @Valid RegularIRA a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		AccountHolder user = findUser(auth);
		enforceFound(user);
		return createAccount(user, a);
	}

	@PostMapping("Admin/{id}/RolloverIRA")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addRolloverIRA(@PathVariable(name = "id") int id, @RequestBody @Valid RolloverIRA a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		return createAccount(id, a);
	}

	@PostMapping("User/RolloverIRA")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addRolloverIRA(@RequestHeader("Authorization") String auth, @RequestBody @Valid RolloverIRA a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		AccountHolder user = findUser(auth);
		enforceFound(user);
		return createAccount(user, a);
	}

	@PostMapping("Admin/{id}/RothIRA")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addRothIRA(@PathVariable(name = "id") int id, @RequestBody @Valid RothIRA a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		return createAccount(id, a);
	}

	@PostMapping("User/RothIRA")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addRothIRA(@RequestHeader("Authorization") String auth, @RequestBody @Valid RothIRA a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		AccountHolder user = findUser(auth);
		enforceFound(user);
		return createAccount(user, a);
	}

	@PostMapping("User/SavingsAccount")
	@ResponseStatus(HttpStatus.CREATED)
	public BankAccount addSavingsAccount(@RequestHeader("Authorization") String auth,
			@RequestBody @Valid SavingsAccount a)
			throws NotFoundException, ExceedsCombinedBalanceLimitException, MaxAccountsReachedException {
		AccountHolder user = findUser(auth);
		enforceFound(user);
		return createAccount(user, a);
	}

	@PutMapping(value = "/Admin/UserInfo/{id}")
	public AccountHolder changeAccountHolderInfoByID(@PathVariable(name = "id") int id,
			@RequestBody AccountHolder newInfo) throws NotFoundException {
		AccountHolder user = accountHolderRepository.findById(id);
		user.updateContactInfo(newInfo);
		log.info("User # " + user.getId() + " updated contact info. ");
		return user;
	}

	@CrossOrigin
	@PutMapping(value = "/User/Close/{id}")
	public BankAccount closeAccoun(@RequestHeader("Authorization") String auth, @PathVariable(name = "id") int id)
			throws TransactionNotAllowedException, NotFoundException, CannotCloseAccountException,
			ExceedsAvailableBalanceException, NegativeAmountException {

		AccountHolder user = findUser(auth);
		enforceFound(user);

		BankAccount a = bankAccountRepository.findById(id);
		enforceFound(a);

		if (a.getUserId() != user.getId()) {
			throw new NotFoundException();
		}

		Transaction t = a.closeAccount(user);
		String s = user.getClosedAccounts();
		if (s.length() > 0) {
			s += ",";
		}
		s += id;
		user.setClosedAccounts(s);
		accountHolderRepository.save(user);

		bankAccountRepository.save(a);
		if (t != null) {
			transactionRepository.save(t);
		}

		log.info("User # " + user.getId() + " closed account # " + a.getAccountNumber());
		return a;
	}

	private BankAccount createAccount(AccountHolder user, BankAccount a) throws MaxAccountsReachedException {
		a.setUserId(user.getId());
		user.addBankAccount(a);
		bankAccountRepository.save(a);

		Transaction t = new Transaction();
		t.setSourceAccount(a.getAccountNumber());
		t.setTargetAccount(a.getAccountNumber());
		t.setTransactionMemo("Account Created");
		t.setBalanceAfterTransaction(a.getBalance());
		t.setTransactionSuccess(true);
		a.addTransaction(t);

		transactionRepository.save(t);
		bankAccountRepository.save(a);

		log.info("User # " + user.getId() + " created account # " + a.getAccountNumber());
		return a;
	}

	private BankAccount createAccount(int id, BankAccount a) throws NotFoundException, MaxAccountsReachedException {
		AccountHolder user = accountHolderRepository.findById(id);
		enforceFound(user);
		a.setUserId(id);
		user.addBankAccount(a);
		bankAccountRepository.save(a);

		log.info("Created new " + a.getAccountName() + " for user # " + user.getId());

		Transaction t = new Transaction();
		t.setSourceAccount(a.getAccountNumber());
		t.setTargetAccount(a.getAccountNumber());
		t.setTransactionMemo("Account Created");
		t.setBalanceAfterTransaction(a.getBalance());
		t.setTransactionSuccess(true);
		a.addTransaction(t);

		transactionRepository.save(t);
		bankAccountRepository.save(a);

		log.info("Created new " + a.getAccountName() + " for user # " + id);
		return a;
	}

	private BankAccount createAccount(int id, BankAccount a, int cdo)
			throws NotFoundException, MaxAccountsReachedException {
		CDOffering c = cdOfferingRepository.findById(cdo);
		enforceFound(c);

		AccountHolder user = accountHolderRepository.findById(id);
		enforceFound(user);
		a.setUserId(id);

		a.setTerm(c.getTerm());
		a.setInterestRate(c.getInterestRate());

		user.addBankAccount(a);
		bankAccountRepository.save(a);

		Transaction t = new Transaction();
		t.setSourceAccount(a.getAccountNumber());
		t.setTargetAccount(a.getAccountNumber());
		t.setTransactionMemo("Account Created");
		t.setBalanceAfterTransaction(a.getBalance());
		a.addTransaction(t);

		transactionRepository.save(t);
		bankAccountRepository.save(a);

		log.info("Created new " + a.getAccountName() + " for user # " + id);
		return a;
	}

	@CrossOrigin
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtUtil.generateToken(userDetails);

		log.info(authenticationRequest.getUsername() + " logged in with token " + jwt);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	@PostMapping("Admin/CDOfferings")
	@ResponseStatus(HttpStatus.CREATED)
	public CDOffering createCDOffering(@RequestBody CDOffering a) {
		cdOfferingRepository.save(a);
		log.info("Created new CD Offering: " + a.getInterestRate() + " for " + a.getTerm());
		return a;
	}

	@PostMapping("NewUser")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder createCustomer(@Valid @RequestBody AccountHolder user)
			throws UsernameAlreadyExistsException, MaxAccountsReachedException {

		if (accountHolderRepository.findByUsername(user.getUsername()) != null) {
			throw new UsernameAlreadyExistsException();
		}

		if (user.getAuthority() == null) {
			user.setAuthority("USER");
		}

		accountHolderRepository.save(user);

		SavingsAccount a = new SavingsAccount();
		a.setUserId(user.getId());
		user.addBankAccount(a);
		bankAccountRepository.save(a);

		Transaction t = new Transaction();
		t.setSourceAccount(a.getAccountNumber());
		t.setTargetAccount(a.getAccountNumber());
		t.setTransactionMemo("Account Created");
		t.setTransactionSuccess(true);
		a.addTransaction(t);

		transactionRepository.save(t);
		bankAccountRepository.save(a);

		log.info("Registered user # " + user.getId());

		return user;
	}

	@CrossOrigin
	@RequestMapping(value = "/direct", method = RequestMethod.GET)
	public String directByRole(@RequestHeader("Authorization") String auth) {

		String jwt = auth.substring(7);
		String username = jwtUtil.extractUsername(jwt);
		AccountHolder ua = accountHolderRepository.findByUsername(username);
		if (!ua.isActive()) {
			return "/error";
		}
		if (ua.getAuthority().equals("ADMIN")) {
			log.info("Directing " + jwtUtil.extractUsername(jwt) + " to admin.html");
			return "/admin";
		}
		log.info("Directing " + jwtUtil.extractUsername(jwt) + " to user.html");
		return "/user";
	}

	private AccountHolder findUser(String auth) throws NotFoundException {
		String jwt = auth.substring(7);
		String username = jwtUtil.extractUsername(jwt);
		AccountHolder user = accountHolderRepository.findByUsername(username);
		enforceFound(user);
		return user;
	}

	@CrossOrigin
	@PostMapping(value = "/FutureValue")
	public double futureValue(@RequestBody CDAccount a) throws NotFoundException {

		double v = a.futureValue(a.getTerm());
		v = Math.floor(v * 100);
		v /= 100;
		return v;
	}

	@GetMapping(value = "Admin/Users/{id}/{activeNum}")
	public AccountHolder getAccountHolderByID(@Valid @PathVariable(name = "id") int id,
			@PathVariable(name = "activeNum") int activeNum) throws NotFoundException {
		boolean active = activeNum == 0 ? true : false;

		AccountHolder user = accountHolderRepository.findById(id);
		enforceFound(user, active);
		return user;
	}

	@GetMapping(value = "/Admin/Users/{activeNum}")
	public Iterable<AccountHolder> getAccountHolders(@PathVariable(name = "activeNum") int activeNum) {
		boolean active = activeNum == 0 ? true : false;

		List<AccountHolder> bu = accountHolderRepository.findAll();
		List<AccountHolder> ah = new ArrayList<>();

		for (AccountHolder b : bu) {
			if (b.getAuthority() != null && !b.getAuthority().equals("ADMIN")) {

				b.setValue(b.getCombinedBalance());
				if (b.isActive() == active) {
					ah.add(b);
				}
			}
		}
		return ah;
	}

	@GetMapping(value = "/Admin/AllUsers")
	public Iterable<AccountHolder> getAllAccountHolders() {
		return accountHolderRepository.findAll();
	}

	@GetMapping("Admin/{id}/{t}/{activeNum}")
	public List<BankAccount> getAnyBankAccounts(@PathVariable(name = "id") int id, @PathVariable(name = "t") String t)
			throws NotFoundException, IllegalArgumentException {

		switch (t) {
		case "CDAccount":
			return getBankAccounts(id, new CDAccount());
		case "CheckingAccount":
			return getBankAccounts(id, new CheckingAccount());
		case "DBACheckingAccount":
			return getBankAccounts(id, new DBACheckingAccount());
		case "RegularIRA":
			return getBankAccounts(id, new RegularIRA());
		case "RolloverIRA":
			return getBankAccounts(id, new RolloverIRA());
		case "RothIRA":
			return getBankAccounts(id, new RothIRA());
		case "SavingsAccount":
			return getBankAccounts(id, new SavingsAccount());
		default:
			throw new IllegalArgumentException();
		}

	}

	@GetMapping("Me/{t}")
	public List<BankAccount> getAnyBankAccounts(@RequestHeader("Authorization") String auth,
			@PathVariable(name = "t") String t) throws NotFoundException, IllegalArgumentException {

		AccountHolder user = findUser(auth);

		switch (t) {
		case "CDAccount":
			return getBankAccounts(user, new CDAccount());
		case "CheckingAccount":
			return getBankAccounts(user, new CheckingAccount());
		case "DBACheckingAccount":
			return getBankAccounts(user, new DBACheckingAccount());
		case "RegularIRA":
			return getBankAccounts(user, new RegularIRA());
		case "RolloverIRA":
			return getBankAccounts(user, new RolloverIRA());
		case "RothIRA":
			return getBankAccounts(user, new RothIRA());
		case "SavingsAccount":
			return getBankAccounts(user, new SavingsAccount());
		default:
			throw new IllegalArgumentException();
		}
	}
	@GetMapping("Me/Balance/{t}")
	public double getBalanceByAccountType(@RequestHeader("Authorization") String auth,
			@PathVariable(name = "t") String t) throws NotFoundException, IllegalArgumentException {

		AccountHolder user = findUser(auth);

		switch (t) {
		case "CDAccount":
			return user.getBalanceByType(new CDAccount());
		case "CheckingAccount":
			return user.getBalanceByType(new CheckingAccount());
		case "DBACheckingAccount":
			return user.getBalanceByType(new DBACheckingAccount());
		case "RegularIRA":
			return user.getBalanceByType(new RegularIRA());
		case "RolloverIRA":
			return user.getBalanceByType(new RolloverIRA());
		case "RothIRA":
			return user.getBalanceByType(new RothIRA());
		case "SavingsAccount":
			return user.getBalanceByType(new SavingsAccount());
		default:
			throw new IllegalArgumentException();
		}
	}

	public List<BankAccount> getBankAccounts(AccountHolder user, BankAccount t) throws NotFoundException {
		List<BankAccount> a = bankAccountRepository.findByUserId(user.getId());
		List<BankAccount> matching = new ArrayList<>();
		for (BankAccount b : a) {
			if (b.getClass() == t.getClass() && b.isActive()) {
				matching.add(b);
			}
		}
		enforceFound(matching);
		return matching;
	}

	private List<BankAccount> getBankAccounts(int id, BankAccount t) throws NotFoundException {

		AccountHolder user = accountHolderRepository.findById(id);
		enforceFound(user);
		List<BankAccount> a = bankAccountRepository.findByUserId(user.getId());
		List<BankAccount> matching = new ArrayList<>();
		for (BankAccount b : a) {
			if (b.getClass() == t.getClass() && b.isActive()) {
				matching.add(b);
			}
		}
		enforceFound(matching);
		return matching;
	}

	public CDOffering getBestCDOffering(List<CDOffering> cdOfferings) {
		double bestRate = 0;
		CDOffering best = null;

		if (cdOfferings.size() == 0)
			return null;

		for (CDOffering c : cdOfferings) {
			if (c.getInterestRate() > bestRate) {
				best = c;
				bestRate = c.getInterestRate();
			}
		}
		return best;
	}

	@GetMapping("CDOffering/{id}")
	public CDOffering getCDOffering(@PathVariable(name = "id") int id) throws NotFoundException {
		return cdOfferingRepository.findById(id);
	}

	@GetMapping("CDOfferings")
	public List<CDOffering> getCDOfferings() throws NotFoundException {
		return cdOfferingRepository.findAll();
	}

	@CrossOrigin
	@GetMapping("Me/BankAccount")
	public List<BankAccount> getCheckingAccounts(@RequestHeader("Authorization") String auth) throws NotFoundException {

		AccountHolder user = findUser(auth);
		List<BankAccount> aa = bankAccountRepository.findByUserId(user.getId());
		List<BankAccount> a = new ArrayList<>();

		for (BankAccount b : aa) {
			if (b.isActive()) {
				a.add(b);
			}
		}

		return a;
	}

	@CrossOrigin
	@GetMapping(value = "/User")
	public AccountHolder getMyAccount(@RequestHeader("Authorization") String auth) throws NotFoundException {
		AccountHolder user = findUser(auth);
		return user;
	}

	public CDOffering getSecondBestCDOffering(List<CDOffering> cdOfferings) {
		double bestRate = 0;
		CDOffering best = null;
		CDOffering secondBest = null;

		if (cdOfferings.size() <= 1)
			return null;

		best = getBestCDOffering(cdOfferings);

		for (CDOffering c : cdOfferings) {
			if (c == best)
				continue;
			if (c.getInterestRate() > bestRate) {
				secondBest = c;
				bestRate = c.getInterestRate();
			}
		}
		return secondBest;

	}

	@CrossOrigin
	@PostMapping(value = "/User/Transaction")
	public Transaction inputTransaction(@RequestHeader("Authorization") String auth,
			@RequestBody @Valid Transaction transaction) throws NotFoundException {

		AccountHolder user = findUser(auth);
		enforceFound(user);

		BankAccount a = bankAccountRepository.findById(transaction.getSourceAccount());
		enforceFound(a);

		BankAccount bat = bankAccountRepository.findById(transaction.getTargetAccount());
		enforceFound(bat);

		if (a.getUserId() != user.getId()) {
			throw new NotFoundException();
		}

		a.processTransaction(transaction, a, bat);

		if (transaction.getSourceAccount() != transaction.getTargetAccount()) {
			if (transaction.getTransactionSuccess()) {
				Transaction t = new Transaction();
				t.setAmount(transaction.getAmount());
				t.setBalanceAfterTransaction(bat.getBalance());
				t.setSourceAccount(transaction.getSourceAccount());
				t.setTargetAccount(transaction.getTargetAccount());
				t.setTransactionMemo("Transfer from account # " + a.getAccountNumber());
				t.setTransactionSuccess(true);
				List<Transaction> ts = bat.getTransactions();
				ts.add(t);
				bat.setTransactions(ts);
				transactionRepository.save(t);
			}
		}

		bankAccountRepository.save(a);
		transactionRepository.save(transaction);

		log.info("User # " + user.getId() + " created a transaction from account " + a.getAccountNumber());
		return transaction;
	}

	@PostMapping(value = "Admin/Transaction")
	public Transaction inputTransaction(@RequestBody @Valid Transaction transaction) throws NotFoundException {

		BankAccount ba = bankAccountRepository.findById(transaction.getSourceAccount());
		enforceFound(ba);

		BankAccount bat = bankAccountRepository.findById(transaction.getTargetAccount());
		enforceFound(bat);

		ba.processTransaction(transaction, ba, bat);

		bankAccountRepository.save(ba);
		transactionRepository.save(transaction);

		log.info("Adjusted Funds of user # " + ba.getUserId() + "'s " + ba.getAccountName() + " by $"
				+ transaction.getAmount());
		return transaction;
	}

	@CrossOrigin
	@GetMapping(value = "/quickFunds")
	public double quickFunds(@RequestHeader("Authorization") String auth) throws NotFoundException {
		AccountHolder user = findUser(auth);

		return user.getAllAvailableBalance();
	}

	@CrossOrigin
	@PutMapping(value = "/User/Quit")
	public AccountHolder quitAccoun(@RequestHeader("Authorization") String auth) throws NotFoundException,
			CannotCloseAccountException, ExceedsAvailableBalanceException, NegativeAmountException {

		AccountHolder user = findUser(auth);
		enforceFound(user);

		List<BankAccount> allA = user.getBankAccounts();
		for (BankAccount b : allA) {

			String s = user.getClosedAccounts();
			if (s.length() > 0) {
				s += ",";
			}
			s += b.getAccountNumber();
			user.setClosedAccounts(s);
			accountHolderRepository.save(user);

			b.setActive(false);
			bankAccountRepository.save(b);
		}

		user.setActive(false);
		accountHolderRepository.save(user);

		log.info("User # " + user.getId() + " closed their user account. ");
		return user;
	}

	@PostMapping("Contact")
	@ResponseStatus(HttpStatus.CREATED)
	public AccountHolder updateUserInfo(@RequestHeader("Authorization") String auth,
			@Valid @RequestBody AccountHolder u) throws NotFoundException {

		String jwt = auth.substring(7);
		String username = jwtUtil.extractUsername(jwt);
		AccountHolder user = accountHolderRepository.findByUsername(username);
		enforceFound(user);

		user.setEmail(u.getEmail());
		user.setPhone(u.getPhone());
		user.setAddress(u.getAddress());
		user.setCity(u.getCity());
		user.setState(u.getState());
		user.setZip(u.getZip());

		accountHolderRepository.save(user);

		return user;
	}
}