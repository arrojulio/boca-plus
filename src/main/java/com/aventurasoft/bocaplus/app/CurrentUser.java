package com.aventurasoft.bocaplus.app;

import com.aventurasoft.bocaplus.data.entity.security.User;

@FunctionalInterface
public interface CurrentUser {

    User getUser();
}
