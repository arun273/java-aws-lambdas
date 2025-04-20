package com.example.lambda;


public class ServiceFactory {

    private static final MyService MY_SERVICE;

    static {
        System.out.println("Initializing MyService eagerly...");
        MY_SERVICE = new MyService(); // uses default constructor
        MY_SERVICE.init();            // warm-up logic
    }

    public static MyService getService() {
        return MY_SERVICE;
    }

    private ServiceFactory() {
        // Prevent instantiation
    }

/*
    private static class Holder {
        static final MyService INSTANCE = createService();
    }

    private static MyService createService() {
        MyService service = new MyService();
        service.init();
        return service;
    }

    public static MyService getService() {
        return Holder.INSTANCE;
    }*/

}

