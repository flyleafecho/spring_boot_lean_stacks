package org.example.ws.service;

import org.example.ws.model.Account;
import org.example.ws.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceBean implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account findByUsername(String username) {
		return accountRepository.findByUsername(username);
	}
}
