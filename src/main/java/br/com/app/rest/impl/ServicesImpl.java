package br.com.app.rest.impl;

import br.com.app.rest.Services;

import java.util.logging.Logger;

public class ServicesImpl implements Services {

    private static Logger LOG = Logger.getLogger(ServicesImpl.class.getName());

    @Override
    public String getMessage(String name) {

        LOG.info("Hello Service");
        return String.format("Hello, %s!!", name);
    }
}
