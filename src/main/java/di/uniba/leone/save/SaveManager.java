package di.uniba.leone.save;

import di.uniba.leone.game.Game;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveManager {

    private final String SERVERADDRS = "localhost";
    private final int SERVERPORT = 6666;
    private String username = "notLogged";
    private File loadedMatch;
    private final File savingDirectory;
    private Scanner scanner;

    public SaveManager(String path, Scanner scanner) {
        savingDirectory = new File(path.concat("/leone_game/partite"));
        this.scanner = scanner;
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

                System.out.print(">Limite partite raggiunto, scegliere una da sovrascrivere:1, 2, 3.\n?>");
                String ans = scanner.nextLine();
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

        Arrays.asList(matches).forEach(file -> System.out.println(">" + file.getName()));
        System.out.print("?>");
        do {
            String ans = scanner.nextLine();
            selectedMatch = Arrays.asList(matches).stream().filter(file -> file.getName().contentEquals(ans)).findFirst().orElse(null);
            if (selectedMatch == null) {
                System.out.println(">Partita scelta non valida. Reinserisci un nome valido.");
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

                    System.out.println(">Partita caricata.");
                    pass = true;
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } while (!pass);

        return selectedMatch.getAbsolutePath();
    }

    public void saveMatch(Saving match) {
        try (FileOutputStream matchToSave = new FileOutputStream(loadedMatch); ObjectOutputStream out = new ObjectOutputStream(matchToSave)) {

            if (loadedMatch.exists()) {
                loadedMatch.delete();
            }
            out.writeObject(match);
            System.out.println(">Salvataggio completato.");
                    
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
                System.out.print(">Desideri collegarti sul server?\nSi/No>");
                ans = scanner.nextLine().toLowerCase();
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
                                        System.out.println(msg);
                                    }
                                    username = msg.split(":")[1];
                                } else if (!msg.contains(">DISCONNECTED") && !msg.contains(">Accesso effettuato")) {
                                    System.out.print(msg);
                                    out.writeObject(scanner.nextLine());
                                    out.flush();
                                }

                            } while (!msg.contains("LOGGED"));

                        } catch (IOException | ClassNotFoundException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (!ans.contentEquals("no")) {
                    System.out.println(">Non ho capito.");
                    pass = false;
                }
            } while (!pass);
        } else {
            ans = "logged";
        }
        return ans;
    }

    public void backUpServer() {
        System.out.print(">Vuoi eseguire un backup su server?\n>");

        if (scanner.nextLine().toLowerCase().contentEquals("si")) {
            String ans = connectToServer();
            System.out.println("");
            if (ans.contentEquals("si") || ans.contentEquals("logged")) {
                try (Socket user = new Socket(SERVERADDRS, 6666)) {
                    try (ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream()); ObjectInputStream in = new ObjectInputStream(user.getInputStream())) {
                        out.writeObject(username);
                        out.flush();
                        out.writeObject("BACKUP");
                        out.flush();

                        String msg;
                        do {
                            msg = (String) in.readObject();
                            System.out.print(msg);
                            if (msg.contentEquals(">BACKUP STARTED")) {
                                System.out.println("");
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
                            }

                            if (!msg.contains("DISCONNECTED") && !msg.contains(">BACKUP STARTED") && !msg.contentEquals(">Accesso effettuato.\n")) {
                                out.writeObject(scanner.nextLine());
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
                System.out.println(">Backup non eseguito");
            }
        } else {
            System.out.println(">Backup non eseguito.");
        }
    }

    public void recoveryFromServer() {
        System.out.print(">Vuoi ripristinare in locale le partite salvate su server?\n>");

        if (scanner.nextLine().toLowerCase().contentEquals("si")) {
            String ans = connectToServer();
            System.out.println("");
            if (ans.contentEquals("si") || ans.contentEquals("logged")) {
                try (Socket user = new Socket(SERVERADDRS, 6666)) {
                    try (ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream()); ObjectInputStream in = new ObjectInputStream(user.getInputStream())) {

                        out.writeObject(username);
                        out.flush();
                        out.writeObject("RECOVERY");
                        out.flush();

                        String msg;
                        msg = (String) in.readObject();
                        System.out.println(msg);
                        if (msg.contentEquals(">RECOVERY STARTED")) {
                            System.out.println("");
                            if (((String) in.readObject()).contentEquals("<INVIO DATI>")) {
                                File dir = new File(savingDirectory.getAbsolutePath());
                                if (dir.listFiles().length != 0) {
                                    Arrays.asList(dir.listFiles()).forEach(file -> file.delete());
                                }

                                Integer count = 1;
                                Object obj;
                                while ((obj = in.readObject()) != null) {
                                    if (obj instanceof Saving match) {
                                        File matchBackup = new File(dir, "/".concat("partita").concat(count.toString()).concat(".ser"));
                                        matchBackup.createNewFile();
                                        count++;

                                        try (FileOutputStream fileWr = new FileOutputStream(matchBackup); ObjectOutputStream dataWr = new ObjectOutputStream(fileWr)) {
                                            dataWr.writeObject(match);
                                        }

                                    } else if ((obj instanceof String message) && (message.contains("<FINE>"))) {
                                        System.out.println(">RECOVERY ENDED");
                                        out.flush();
                                    }
                                }
                            }
                        }

                    } catch (ClassNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                System.out.println(">Recovery non eseguito.");
            }
        } else {
            System.out.println(">Recovery non eseguito.");
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
}
