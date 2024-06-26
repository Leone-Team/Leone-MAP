package di.uniba.leone.save;

import di.uniba.leone.game.Game;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Scanner;

public class SaveManager {

    private final File savingDirectory;
    private File loadedMatch;

    public SaveManager(String path) {
        savingDirectory = new File(path.concat("/leone_game/partite"));
        loadedMatch = new File(savingDirectory.getPath());
    }

    public String newGame() {
        String newMatchPath = null;
        File[] matches = savingDirectory.listFiles();
        if (matches.length < 3) {
            File newDir = new File(savingDirectory, "partita".concat(Integer.toString(savingDirectory.listFiles().length + 1)));
            newMatchPath = newDir.getAbsolutePath();
            newDir.mkdir();
        } else {
            Boolean pass = false;
            do {
                Scanner scanner = new Scanner(System.in);
                System.out.print(">Limite partite raggiunto, scegliere una da sovrascrivere:1, 2, 3.\n?>");
                String ans = scanner.nextLine();
                if (ans.compareTo("1") == 0 || ans.compareTo("2") == 0 || ans.compareTo("3") == 0) {
                    File newDir = new File(savingDirectory, "partita".concat(ans));
                    for (File file : newDir.listFiles()) {
                        file.delete();
                    }
                    newMatchPath = newDir.getAbsolutePath();
                }
            } while (pass);
        }
        return newMatchPath;
    }

    public String loadMatch(Game game) {
        File loadedDir = null;
        Saving data;
        Scanner scanner = new Scanner(System.in);
        Boolean pass = false;
        File[] matches = savingDirectory.listFiles();

        Arrays.asList(matches).forEach(file -> System.out.println(">" + file.getName()));
        System.out.print("?>");
        do {
            String ans = scanner.nextLine();
            loadedDir = Arrays.asList(matches).stream().filter(file -> file.getName().contentEquals(ans)).findFirst().orElse(null);
            if (loadedDir == null) {
                System.out.println(">Partita scelta non valida. Reinserisci un nome valido.");
            } else {
                try (FileInputStream matchToLoad = new FileInputStream(new File(loadedDir, "/dati.ser"))) {
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
        return loadedDir.getAbsolutePath();
    }

    public void saveMatch(Saving match) {
        try (FileOutputStream matchToSave = new FileOutputStream(new File(loadedMatch, "dati.ser"))) {
            ObjectOutputStream out = new ObjectOutputStream(matchToSave);
            out.writeObject(match);
            if (loadedMatch.listFiles().length != 0) {
                System.out.println(">Dati correttamente salvati");
            } else {
                System.out.println("Errore nel salvataggio");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("exeption");
        }
    }

    public void setLoadedMatch(File loadedMatch) {
        this.loadedMatch = loadedMatch;
    }

    public void close() {
        File[] matchDir = savingDirectory.listFiles();

        for (File dir : matchDir) {
            if (dir.listFiles().length == 0) {
                dir.delete();
            }
        }
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
