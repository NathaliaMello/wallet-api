package com.nathaliamello.wallet.api.restservice;

import com.nathaliamello.wallet.api.config.property.WalletApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/tokens")
public class TokenRestService {

    private WalletApiProperty walletApiProperty;

    @Autowired
    public TokenRestService(WalletApiProperty walletApiProperty) {
        this.walletApiProperty = walletApiProperty;
    }

    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(walletApiProperty.getSeguranca().isEnableHttps());
        cookie.setPath(httpServletRequest.getContextPath() + "/oauth/token");
        cookie.setMaxAge(0);

        httpServletResponse.addCookie(cookie);
        httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());

    }
}
