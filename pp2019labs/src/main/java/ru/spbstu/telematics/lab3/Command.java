package ru.spbstu.telematics.lab3;

public class Command {

    public Command(String commandName, int data) {
        this.data = data;
        this.commandName = commandName;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    private int data;

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    private String commandName;

}
