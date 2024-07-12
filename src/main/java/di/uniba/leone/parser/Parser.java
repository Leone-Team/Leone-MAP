package di.uniba.leone.parser;

import di.uniba.leone.type.Command;
import di.uniba.leone.type.CommandType;
import static di.uniba.leone.type.CommandType.LOOK;
import di.uniba.leone.type.Item;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author giann
 */
public class Parser {
    final private Set<String> stopwords = new HashSet();
    
    /**
     *
     * @param stopwordsPath
     */
    public Parser(String stopwordsPath)
    {
        Set<String> stopwords = new HashSet();
        try(BufferedReader file = new BufferedReader( new FileReader(stopwordsPath))){
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
    
    
    /** 
     * @param line
     * @return List<String>
     */
    private List<String> parseString(String line){
        List<String> commandLine = new ArrayList();
        
        for(String word:line.split("\\s+"))
            if (!stopwords.contains(word))
                commandLine.add(word);
        
        return commandLine;
    }
    
    
    /** 
     * @param token
     * @param items
     * @return int
     */
    private int isItem(String token, Collection<Item> items){
        for(Item it:items)
            if (it.getNames().contains(token.toLowerCase()))
                return it.getID();
        return -1;
    }
    
    
    
    /** 
     * @param token
     * @param commands
     * @return int
     */
    private int isCommand(String token, List<Command> commands){
        for(Command cmd:commands)
            if(cmd.getNames().contains(token.toLowerCase()))
                return commands.indexOf(cmd);
           
        return -1;
    }
   
    /**
     *
     * @param commandLine
     * @param items
     * @param commands
     * @return
     */
    public ActionInGame parse(String commandLine, HashMap<Integer, Item> items, List<Command> commands){
        ActionInGame action = new ActionInGame();
        
        List<String> tokens = new ArrayList(parseString(commandLine));
        
        switch(tokens.size()){
            case 1 ->{
                if(isCommand(tokens.get(0), commands) == -1)
                    action.setCommand(null);
                else
                    action.setCommand(commands.get(isCommand(tokens.get(0), commands)));               
                action.setItem1(null);
                action.setItem2(null);
            }
            case 2 ->{
                if(isCommand(tokens.get(0), commands) == -1)
                    action.setCommand(null);
                else
                    action.setCommand(commands.get(isCommand(tokens.get(0), commands)));
                if(isItem(tokens.get(1), items.values()) == -1)
                    action.setItem1(null);
                else
                    action.setItem1(items.get( isItem( tokens.get(1), items.values())));
                action.setItem2(null);
            }
            case 3 ->{
                if(isCommand(tokens.get(0), commands) == -1)
                    action.setCommand(null);
                else
                    action.setCommand(commands.get(isCommand(tokens.get(0), commands)));
                if(isItem(tokens.get(1), items.values()) == -1)
                    action.setItem1(null);
                else
                    action.setItem1(items.get( isItem( tokens.get(1), items.values())));
                if(isItem(tokens.get(2), items.values()) == -1)
                    action.setItem2(null);
                else
                    action.setItem2(items.get( isItem( tokens.get(2), items.values())));
            }
            default ->{
                action.setCommand(null);
                action.setItem1(null);
                action.setItem2(null);
            }
        }

        return action;
    }
    
}
