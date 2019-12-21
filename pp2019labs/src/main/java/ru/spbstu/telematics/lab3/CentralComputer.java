package ru.spbstu.telematics.lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CentralComputer implements Runnable {

    private BlockingQueue<Command> commandsQueue = new ArrayBlockingQueue<>(5);
    private BlockingQueue<Object> resultsQueue = new ArrayBlockingQueue<>(5);

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public static List<Ticket> tickets;

    public CentralComputer(int numberOfTickets) {
        tickets = new ArrayList<>(numberOfTickets);
        for(int i = 0; i < numberOfTickets; i++){
            //Create ticket with id = i
            tickets.add(new Ticket(i));
        }
    }

    public List<Integer> getAllUnreservedSeatNumbers() {
        List<Integer> unreservedSeatNumbers = new ArrayList<>();
        for (Ticket t:tickets) {
            if(!t.isReserved())
                unreservedSeatNumbers.add(t.getId());
        }
        return unreservedSeatNumbers;
    }

    public void reserveSeatNumber(Integer number) {
        tickets.get(number).setReserved(true);
    }

    public Ticket printTicket(Integer number) {
        return tickets.get(number);
    }


    public void sendCommand(Command command) {
        try {
            commandsQueue.put(command);
        }catch (InterruptedException e){

        }
    }

    public Object getLastResult() {
        try {
            return resultsQueue.take();
        }catch (InterruptedException e){

        }
        return null;
    }
    @Override
    public void run() {

        try {
            while(true){

                Command command = commandsQueue.take();
                switch (command.getCommandName()){
                    case "Get_All_Unreserved_Seat_Numbers":
                        resultsQueue.put(getAllUnreservedSeatNumbers());
                        break;
                    case "Get_Ticket":
                        resultsQueue.put(getTickets().get(command.getData()));
                        break;
                    case "Reserve_Seat_Number":
                        reserveSeatNumber(command.getData());
                        resultsQueue.put(new Object());
                        break;
                    case "Print_Ticket":
                        resultsQueue.put(printTicket(command.getData()));
                        break;
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
