package ru.spbstu.telematics.lab3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Cashier implements Runnable{

    CentralComputer centralComputer;

    Map<Integer, BlockingQueue<Command>> commandQueues = new HashMap<>();
    Map<Integer, BlockingQueue<Object>> resultQueues = new HashMap<>();

    public Cashier(CentralComputer centralComputer, int numberOfTerminals) {
        this.centralComputer = centralComputer;
        for(int i =0 ; i < numberOfTerminals; i++){
            resultQueues.put(i, new ArrayBlockingQueue<Object>(5));
            commandQueues.put(i, new ArrayBlockingQueue<Command>(5));
        }

    }

    public List<Integer> displayCurrentStatus(){

        centralComputer.sendCommand(new Command("Get_All_Unreserved_Seat_Numbers",0));
        return (List<Integer>) centralComputer.getLastResult();

    }

    public boolean enterSeatNumber(Integer number) {

        centralComputer.sendCommand(new Command("Get_Ticket",number));
        Ticket hold = (Ticket)centralComputer.getLastResult();

        if(hold.isSelected()){
            return false;
        }else{
            hold.setSelected(true);
        }

        centralComputer.sendCommand(new Command("Reserve_Seat_Number",number));
        centralComputer.getLastResult();
        return true;
    }

    public Ticket getTicket(Integer number) {
        centralComputer.sendCommand(new Command("Print_Ticket",number));
        return (Ticket)centralComputer.getLastResult();
    }


    public void sendCommand(Command command, int ID) {
        try {
            commandQueues.get(ID).put(command);
        }catch (InterruptedException e){

        }
    }

    public Object getLastResult(int ID) {
        try {
            return resultQueues.get(ID).take();
        }catch (InterruptedException e){

        }
        return null;
    }

    @Override
    public void run() {
        try {

            while(true){
                for(Entry<Integer, BlockingQueue<Command>> commandMapPair : commandQueues.entrySet()){
                    Command command = commandMapPair.getValue().poll();
                    if(command != null){
                        switch (command.getCommandName()){
                            case "Display_Current_Status":
                                resultQueues.get(commandMapPair.getKey()).put(displayCurrentStatus());
                                break;
                            case "Enter_Seat_Number":
                                resultQueues.get(commandMapPair.getKey()).put(enterSeatNumber(command.getData()));
                                break;
                            case "Get_Ticket":
                                resultQueues.get(commandMapPair.getKey()).put(getTicket(command.getData()));
                                break;

                        }
                    }
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
