package com.nathaliamello.wallet.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.logging.Logger;

public class GeradorSenha {

    private static final Logger LOGGER = Logger.getLogger(GeradorSenha.class.getName());
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String crypt = encoder.encode("m0b1l30");
        LOGGER.info(crypt);
    }
}
