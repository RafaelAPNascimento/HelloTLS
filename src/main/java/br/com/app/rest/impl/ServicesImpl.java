package br.com.app.rest.impl;

import br.com.app.rest.Services;

public class ServicesImpl implements Services {

    @Override
    public String getMessage(String name) {
        return String.format("Hello, %s!!", name);
    }
}
