package com.linkel.water.server;

import java.util.ArrayList;

public class Client {

    private boolean alive = false;
    private ArrayList<Collector> collectors;
    private String ip;

    Client(String ip){
        this.ip = ip;
    }

    Client(ArrayList<Collector> collectors){
        this.collectors = collectors;
    }




}
