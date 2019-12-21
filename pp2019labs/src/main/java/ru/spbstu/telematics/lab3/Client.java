package ru.spbstu.telematics.lab3;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable{

    static private Queue<Client> clientsQueue = new LinkedList<>();
    static private Lock terminalLock = new ReentrantLock();
    static public  List<Terminal> terminals;
    private volatile boolean inQueue = false;
    private Terminal selectedTerminal;
    private int clientId;
    private Ticket selectedTicket;
    public static int leftClients = 0;
    public static List<Ticket> purchasedTickets;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Ticket getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(Ticket selectedTicket) {
        this.selectedTicket = selectedTicket;
    }

    public Client(int id) {
        clientId = id;
    }

    public void chooseTerminal() {
        terminalLock.lock();
        try {
            for(Terminal t : terminals){
                if(t.isFree()){
                    selectedTerminal = t;
                    selectedTerminal.sendCommand(new Command("SET_FREE",0));
                  //ждём подтверждение команды
                    selectedTerminal.getLastResult();
                    System.out.println("Client-" + clientId + "  has just chosen " + "terminal-" + selectedTerminal.getTerminalId());
                    return;
                }
            }
          //Если нет свободных терминалов, встаём в очередь
            clientsQueue.add(this);
            inQueue = true;
            System.out.println("Client-" + clientId + " now  in queue");
        }finally {
            terminalLock.unlock();
        }

    }

    private void leave(){
        terminalLock.lock();
        try {
            System.out.println("Client-" + clientId + "  has just left and terminal-" + selectedTerminal.getTerminalId() + " is now free");
            leftClients++;
          //Перёдаём терминал
            Client nextClient = clientsQueue.poll();
            if (nextClient != null) {
                System.out.println("Next client-" + nextClient.clientId + "  has just chosen " + "terminal-" + selectedTerminal.getTerminalId());
                nextClient.selectedTerminal = selectedTerminal;
                nextClient.inQueue = false;
            }
          //Освобождаем терминал
            else{
                selectedTerminal.sendCommand(new Command("SET_FREE",1));
                //ждём подтверждение команды
                selectedTerminal.getLastResult();
            }
        }finally {
            terminalLock.unlock();
        }
    }

    private void reserveTicket() {
        List<Integer> unreservedSeatNumbers;
        while(selectedTicket == null || selectedTicket.getId() == -1) {
            selectedTerminal.sendCommand(new Command("GET_CURRENT_STATUS",0));
            unreservedSeatNumbers = (List<Integer>)selectedTerminal.getLastResult();
            if(unreservedSeatNumbers.size() != 0) {
                selectedTerminal.sendCommand(new Command("SELECT_SEAT_NUMBER", unreservedSeatNumbers.get(0)));
                selectedTicket = (Ticket)selectedTerminal.getLastResult();
            }else {
                System.out.println("No tickets for client-" + clientId);
                return;
            }
        }
        System.out.println("Client-" + clientId + " reserved " + "ticket-" + selectedTicket.getId());
        purchasedTickets.add(selectedTicket);
    }

    @Override
    public void run() {
        clientId = (int) Thread.currentThread().getId();
        chooseTerminal();

        while(inQueue){

        }

//        try {
//            //ACTION SIMULATION
//            Thread.sleep((int)(20*Math.random()));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        reserveTicket();
        leave();
        System.out.println("Clients left : " + leftClients);
    }

}
