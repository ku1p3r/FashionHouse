package common.wrapper;

public final class Option {
    private String command;
    private String description;
    private Runnable function;

    public Option(String command, String description, Runnable function){
        this.command = command;
        this.description = description;
        this.function = function;
    }

    public String getCommand(){
        return command;
    }

    public String getDescription(){
        return description;
    }

    public void pick(){
        function.run();
    }
}
