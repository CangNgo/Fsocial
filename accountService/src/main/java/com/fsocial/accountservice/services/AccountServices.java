package com.fsocial.accountservice.services;

import com.fsocial.accountservice.dto.AccountDTO;
import com.fsocial.accountservice.exception.AppCheckedException;

public interface AccountServices {

    AccountDTO registerUser(AccountDTO accountDTO) throws AppCheckedException;

}
