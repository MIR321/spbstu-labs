package ru.spbstu.telematics.lab3;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Terminal implements Runnable {

    static public Cashier cashier;
    private BlockingQueue<Command> commandsQueue = new ArrayBlockingQueue<>(5);
    private BlockingQueue<Object> resultsQueue = new ArrayBlockingQueue<>(5);
    private int terminalId;
    private boolean free;

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }


    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }


    public Terminal(int id) {
        terminalId = id;
    }

    public List<Integer> getCurrentStatus(){
        cashier.sendCommand(new Command("Display_Current_Status",0), terminalId);
        return (List<Integer>)cashier.getLastResult(terminalId);
    }


    public Ticket selectSeatNumber(Integer number) {
        cashier.sendCommand(new Command("Enter_Seat_Number", number), terminalId);
        if(!(Boolean)cashier.getLastResult(terminalId)){
            //ticket-"+number+" is already selected"
            return new Ticket(-1);
        }
        cashier.sendCommand(new Command("Get_Ticket", number), terminalId);
        return (Ticket)cashier.getLastResult(terminalId);
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
                case "SELECT_SEAT_NUMBER":
                    resultsQueue.put(selectSeatNumber(command.getData()));
                    break;
                case "GET_CURRENT_STATUS":
                    resultsQueue.put(getCurrentStatus());
                    break;
                case "SET_FREE":
                    setFree(command.getData() == 0 ? false : true);
                    resultsQueue.put(new Object()); 
                    break;
            }

        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
