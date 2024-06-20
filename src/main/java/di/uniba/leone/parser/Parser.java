package di.uniba.leone.parser;

import di.uniba.leone.type.Command;
import di.uniba.leone.type.Item;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Parser {
    final private Set<String> stopwords = new HashSet();
    
    public Parser(String stopwordsPath)
    {
        Set<String> stopwords = new HashSet();
        try(BufferedReader file = new BufferedReader( new FileReader( "./leone_game/"))){
            String word;
            while( (word = file.readLine()) != null)
            {
                stopwords.add(word);
            }
            this.stopwords.addAll(stopwords);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private Set<String> parseString(String line){
        Set<String> commandLine = new HashSet();
        
        for(String word:line.split("//s+")){
            if (!stopwords.contains(word))
                commandLine.add(word);
        }
        return commandLine;
    }
    
    private int isItem(String token, Collection<Item> items){
        for(Item it:items)
            if (it.getNames().contains(token))
                return it.getID();
        return -1;
    }
    
    
    private int isCommand(String token, List<Command> commands){
        for(Command cmd:commands)
            if(cmd.getNames().contains(token))
                return commands.indexOf(cmd);
        return -1;
    }
   
    public ActionInGame parse(String commandLine, HashMap<Integer, Item> items, List<Command> commands){
        ActionInGame action = null;
        
        Set<String> tokens = new HashSet(parseString(commandLine));
        
        int i;
        for(String token:tokens)
        {
            if ((i = isItem(token, items.values())) !=-1)
                action.setItem(items.get(i));
            else if((i = isCommand(token, commands)) != -1)
                action.setCommand(commands.get(i));
            }
        
        if((action.getCommand() == null) || (action.getItem() == null) )
        {
            System.out.println(">Non ho capito");
            action.setCommand(null);
            action.setItem(null);
        }
        return action;
    }
    
}
