package ru.spbstu.telematics.lab3;

import java.util.ArrayList;
import java.util.List;

public class ConcertHall {

    public static int NUMBER_OF_TERMINALS;
    public static int NUMBER_OF_CLIENTS;
    public static int NUMBER_OF_TICKETS;
    
    
    public static void launch(int amountOfTerminals, int amountOfClients, int amountOfTickets) throws InterruptedException {
    	
    	NUMBER_OF_TERMINALS = amountOfTerminals;
    	NUMBER_OF_CLIENTS = amountOfClients;
    	NUMBER_OF_TICKETS = amountOfTickets;
    	
    	List<Terminal> terminals = new ArrayList<>(NUMBER_OF_TERMINALS);
        CentralComputer centralComputer = new CentralComputer(NUMBER_OF_TICKETS);
        Cashier cashier = new Cashier(centralComputer, NUMBER_OF_TERMINALS);
        Client.terminals = terminals;
        Client.purchasedTickets = new ArrayList<>();
        Client.leftClients = 0;
        Terminal.cashier = cashier;
        List<Client> clients = new ArrayList<>(NUMBER_OF_CLIENTS);
        List<Thread> threads = new ArrayList<>(NUMBER_OF_CLIENTS);

        new Thread(centralComputer).start();
        new Thread(cashier).start();

        for(int i = 0; i < NUMBER_OF_TERMINALS; i++){
            terminals.add(new Terminal(i));
            terminals.get(i).setFree(true);
            new Thread(terminals.get(i)).start();
        }

        for(int i = 0; i < NUMBER_OF_CLIENTS; i++){
            clients.add(new Client(i));
            threads.add(new Thread(clients.get(i)));
            threads.get(i).start();
        }

        for (Thread t :
                threads) {
            t.join();
        }
      //Проверка
        for (Client client :
                clients) {
            if(client.getSelectedTicket() != null && client.getSelectedTicket().getId() != -1)
                System.out.println("Client-" + client.getClientId() + " reserved " + "ticket-" + client.getSelectedTicket().getId());
        }
    	
    	
    }

//    public static void main(String[] args) throws InterruptedException {
//        
//    	launch(50, 800, 20);
//
//    }
}
