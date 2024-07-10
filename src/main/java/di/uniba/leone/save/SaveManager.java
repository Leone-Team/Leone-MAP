package di.uniba.leone.save;

import di.uniba.leone.game.Game;
import di.uniba.leone.game.GameTime;
import di.uniba.leone.game.Ranking;
import di.uniba.leone.gui.MsgManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveManager {

    private final MsgManager mrMsg;
    private final String SERVERADDRS = "localhost";
    private final int SERVERPORT = 6666;
    private String username = "notLogged";
    private File loadedMatch;
    private final File savingDirectory;

    public SaveManager(String path, MsgManager mrMsg) {
        savingDirectory = new File(path.concat("/src/main/resources/leone_game/partite"));
        this.mrMsg = mrMsg;
    }

    public String newGame() {
        String newMatchPath = null;
        File[] matches = savingDirectory.listFiles();
        if (matches.length < 3) {
            File newMatch = new File(savingDirectory, "partita".concat(Integer.toString(savingDirectory.listFiles().length + 1)).concat(".ser"));
            newMatchPath = newMatch.getAbsolutePath();
        } else {
            Boolean pass = false;
            do {

                mrMsg.displayMsg(">Limite partite raggiunto, scegliere una da sovrascrivere:1, 2, 3.\n?>");
                String ans = mrMsg.getMsg();
                if (ans.matches("[1-3]")) {
                    File newMatch = new File(savingDirectory, "partita".concat(ans).concat(".ser"));
                    newMatchPath = newMatch.getAbsolutePath();
                }

            } while (pass);
        }
        return newMatchPath;
    }

    public String loadMatch(Game game) {
        File selectedMatch = null;
        Saving data;
        Boolean pass = false;
        File[] matches = savingDirectory.listFiles();

        if (matches.length != 0) {
            Arrays.asList(matches).forEach(file -> mrMsg.displayMsg(">" + file.getName()));
            mrMsg.displayMsg("?>");
            do {
                String ans = mrMsg.getMsg();
                selectedMatch = Arrays.asList(matches).stream().filter(file -> file.getName().contentEquals(ans)).findFirst().orElse(null);
                if (selectedMatch == null) {
                    mrMsg.displayMsg(">Partita scelta non valida. Reinserisci un nome valido.");
                } else {
                    try (FileInputStream matchToLoad = new FileInputStream(selectedMatch)) {
                        ObjectInputStream in = new ObjectInputStream(matchToLoad);
                        data = (Saving) in.readObject();

                        game.setCurrentRoom(data.getCurrentRoom());
                        game.setInventory(data.getInventory());
                        game.setItems(data.getItems());
                        game.setObsAttached(data.getObsAttached());
                        game.setRiddles(data.getRiddles());
                        game.setRooms(data.getRooms());

                        mrMsg.displayMsg(">Partita caricata.");
                        pass = true;
                    } catch (IOException | ClassNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } while (!pass);
        } else {
            selectedMatch = new File(savingDirectory, "partita".concat(Integer.toString(savingDirectory.listFiles().length + 1)).concat(".ser"));
            mrMsg.displayMsg(">Non ci sono partite salvate. Caricata nuova partita.\n");

        }
        return selectedMatch.getAbsolutePath();
    }

    public void saveMatch(Saving match) {
        try (FileOutputStream matchToSave = new FileOutputStream(loadedMatch); ObjectOutputStream out = new ObjectOutputStream(matchToSave)) {

            if (loadedMatch.exists()) {
                loadedMatch.delete();
            }
            out.writeObject(match);
            mrMsg.displayMsg(">Salvataggio completato.");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("exeption");
        }
    }

    public String connectToServer() {
        String ans = null;
        if (username.contentEquals("notLogged")) {
            Boolean pass;
            do {
                pass = true;
                mrMsg.displayMsg(">Desideri collegarti sul server?");
                ans = mrMsg.getMsg().toLowerCase();
                if (ans.contentEquals("si")) {
                    try (Socket user = new Socket(SERVERADDRS, SERVERPORT)) {
                        try (ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream()); ObjectInputStream in = new ObjectInputStream(user.getInputStream())) {

                            out.writeObject(username);
                            out.flush();
                            String msg;
                            do {
                                msg = (String) in.readObject();

                                if (msg.contains("LOGGED")) {
                                    if (!msg.contains("notLogged")) {
                                        mrMsg.displayMsg(">" + msg);
                                    }
                                    username = msg.split(":")[1];
                                } else if (!msg.contains(">DISCONNECTED") && !msg.contains(">Accesso effettuato")) {
                                    mrMsg.displayMsg(msg);
                                    out.writeObject(mrMsg.getMsg());
                                    out.flush();
                                }

                            } while (!msg.contains("LOGGED"));

                        } catch (IOException | ClassNotFoundException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } catch (IOException ex) {
                        mrMsg.displayMsg(">Server non risponde");
                        System.out.println(ex.getMessage());
                    }
                } else if (!ans.contentEquals("no")) {
                    mrMsg.displayMsg(">Non ho capito.");
                    pass = false;
                }
            } while (!pass);
        } else {
            ans = "logged";
        }
        return ans;
    }

    public void backUpServer() {
        mrMsg.displayMsg(">Vuoi eseguire un backup su server?\n>");

        if (mrMsg.getMsg().toLowerCase().contentEquals("si")) {
            String ans = connectToServer();
            mrMsg.displayMsg("");
            if (ans.contentEquals("si") || ans.contentEquals("logged")) {
                try (Socket user = new Socket(SERVERADDRS, SERVERPORT)) {
                    try (ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream()); ObjectInputStream in = new ObjectInputStream(user.getInputStream())) {
                        out.writeObject(username);
                        out.flush();
                        out.writeObject("BACKUP");
                        out.flush();

                        String msg;
                        do {
                            msg = (String) in.readObject();
                            //mrMsg.displayMsg(msg);
                            if (msg.contentEquals(">BACKUP STARTED")) {
                                //invio dati
                                out.writeObject("<INVIO DATI>");
                                out.flush();
                                for (File match : savingDirectory.listFiles()) {
                                    try (FileInputStream fileRd = new FileInputStream(match); ObjectInputStream dataRd = new ObjectInputStream(fileRd)) {
                                        out.writeObject(((Saving) dataRd.readObject()));
                                        out.flush();
                                    }
                                }
                                out.writeObject("<FINE>");
                                out.flush();
                                mrMsg.displayMsg(">Backup Eseguito");
                            }

                            if (!msg.contains("DISCONNECTED") && !msg.contains(">BACKUP STARTED")) {
                                out.writeObject(mrMsg.getMsg());
                                out.flush();
                            }
                        } while (!msg.contains("DISCONNECTED"));
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                mrMsg.displayMsg(">Backup non eseguito");
            }
        } else {
            mrMsg.displayMsg(">Backup non eseguito.");
        }
    }

    public void recoveryFromServer() {
        mrMsg.displayMsg(">Vuoi ripristinare in locale le partite salvate su server?\n>");

        if (mrMsg.getMsg().toLowerCase().contentEquals("si")) {
            String ans = connectToServer();
            mrMsg.displayMsg("");
            if (ans.contentEquals("si") || ans.contentEquals("logged")) {
                try (Socket user = new Socket(SERVERADDRS, SERVERPORT)) {
                    try (ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream()); ObjectInputStream in = new ObjectInputStream(user.getInputStream())) {

                        out.writeObject(username);
                        out.flush();
                        out.writeObject("RECOVERY");
                        out.flush();

                        String msg;
                        msg = (String) in.readObject();
                        //mrMsg.displayMsg(msg);
                        if (msg.contentEquals(">RECOVERY STARTED")) {
                            if (((String) in.readObject()).contentEquals("<INVIO DATI>")) {
                                File dir = new File(savingDirectory.getAbsolutePath());
                                if (dir.listFiles().length != 0) {
                                    Arrays.asList(dir.listFiles()).forEach(file -> file.delete());
                                }

                                Integer count = 1;

                                Object obj;

                                do {
                                    obj = in.readObject();
                                    if (obj instanceof Saving match) {
                                        File matchBackup = new File(dir, "/".concat("partita").concat(count.toString()).concat(".ser"));
                                        matchBackup.createNewFile();
                                        count++;

                                        try (FileOutputStream fileWr = new FileOutputStream(matchBackup); ObjectOutputStream dataWr = new ObjectOutputStream(fileWr)) {
                                            dataWr.writeObject(match);
                                        }

                                    } else if ((obj instanceof String message) && (message.contains("<FINE>"))) {
                                        mrMsg.displayMsg(">Dati ripristinati.");
                                        out.flush();
                                    }
                                } while (!((obj instanceof String message) && (message.contains("<FINE>"))));

                            }
                        }

                    } catch (ClassNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                mrMsg.displayMsg(">Recovery non eseguito.");
            }
        } else {
            mrMsg.displayMsg(">Recovery non eseguito.");
        }
    }

    public Ranking getGlobalRanking() {
        Ranking ranking = new Ranking();
        try (Socket user = new Socket(SERVERADDRS, SERVERPORT)) {
            try (ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream()); ObjectInputStream in = new ObjectInputStream(user.getInputStream())) {

                out.writeObject(username);
                out.flush();
                out.writeObject("RANKING OUT");
                out.flush();

                //mrMsg.displayMsg(msg);

                if (((String) in.readObject()).contentEquals("<INVIO DATI>")) {

                    if (in.readObject() instanceof Ranking rankingUpdated) {
                        ranking = rankingUpdated;
                    }
                    
                    if(in.readObject() instanceof String)
                       mrMsg.displayMsg(">Ranking:");
                }

            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return ranking;
    }

    public void addPlayerToGlobalRanking(GameTime player) {
        try (Socket user = new Socket(SERVERADDRS, SERVERPORT)) {
            try (ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream()); ObjectInputStream in = new ObjectInputStream(user.getInputStream())) {
                out.writeObject(username);
                out.flush();
                out.writeObject("ADD RANKING");
                out.flush();

                String msg;
                do {
                    msg = (String) in.readObject();
                    //mrMsg.displayMsg(msg);
                    if (msg.contentEquals(">RANKING STARTED")) {
                        out.writeObject(player);
                        out.flush();
                    }

                    if (!msg.contains("DISCONNECTED") && !msg.contains(">RANKING STARTED")) {
                        out.writeObject(mrMsg.getMsg());
                        out.flush();
                    }
                } while (!msg.contains("DISCONNECTED"));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setLoadedMatch(File loadedMatch) {
        this.loadedMatch = loadedMatch;
    }

    public void delete(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    public File getSavingDirectory() {
        return savingDirectory;
    }

    public File getLoadedMatch() {
        return loadedMatch;
    }

    public String getUsername() {
        return username;
    }

}
