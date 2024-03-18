package service;

public class ServiceA {

    private final ServiceB serviceB;
    private final ServiceC serviceC;

    public ServiceA(ServiceB serviceB, ServiceC serviceC) {
        this.serviceB = serviceB;
        this.serviceC = serviceC;
    }
}
