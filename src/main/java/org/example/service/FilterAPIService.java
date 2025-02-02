package org.example.service;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class FilterAPIService extends Filter {



    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
    }

    @Override
    public String description() {
        return null;
    }
}
