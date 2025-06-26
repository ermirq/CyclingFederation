package com.zerogravitysolutions.core.utilities;

public final class UserContextHolder {

    private static final ThreadLocal<UserContext> userContextThreadLocal = new ThreadLocal<>();

    public static UserContext getContext(){

        UserContext userCx = userContextThreadLocal.get();

        if(userCx == null){
            userCx = createEmptyContext();
            userContextThreadLocal.set(userCx);
        }

        return userContextThreadLocal.get();
    }

    public static void setContext(final UserContext userCx){
        userContextThreadLocal.set(userCx);
    }

    private static UserContext createEmptyContext(){

        return new UserContext();
    }
}
