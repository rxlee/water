package com.linkel.water.server;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Qualifier("clientManage")
public class ClientManage {

    CopyOnWriteArrayList<Client> clients;

}
