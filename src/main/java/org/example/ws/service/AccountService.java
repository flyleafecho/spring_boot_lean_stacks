package org.example.ws.service;

import org.example.ws.model.Account;

public interface AccountService {

	Account findByUsername(String username);
}
