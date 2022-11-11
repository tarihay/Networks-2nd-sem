package ru.nsu.gorin.networks.lab5;

import ru.nsu.gorin.networks.lab5.proxy.ProxySocks;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        ProxySocks proxy = new ProxySocks(InetAddress.getLocalHost(), 8185);
        proxy.run();
    }
}
