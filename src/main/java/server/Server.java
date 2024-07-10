package server;

import di.uniba.leone.save.Saving;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author feder
 */
public class Server {

    private static class UserThread extends Thread {

        Properties dbProps = new Properties();
        private final String DBPATH = "jdbc:h2:./server/users";
        private String username;
        private final Socket user;
        private String command;

        public UserThread(Socket user) {
            this.user = user;
            setDBProperties();
        }

        private void setDBProperties() {
            dbProps.setProperty("user", "Leone");
            dbProps.setProperty("password", "1234");
        }

        private Properties getDBProperties() {
            return dbProps;
        }

        @Override
        public void run() {

            try (ObjectInputStream in = new ObjectInputStream(user.getInputStream()); ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream())) {
                try (Connection conn = DriverManager.getConnection(DBPATH, getDBProperties())) {
                    //controllo se il login è stato effettuato o meno
                    username = (String) in.readObject();

                    if (username.contentEquals("notLogged")) {
                        //bisognerà eseguire il login o signin
                        out.writeObject(">Login o Signin:");
                        out.flush();
                        boolean pass;
                        do {
                            pass = true;
                            switch (((String) in.readObject()).toLowerCase()) {
                                case "login" ->
                                    login(out, in, conn);

                                case "signin" ->
                                    signin(out, in, conn);

                                default -> {
                                    out.writeObject(">Opzione non valida.\nInserire Login o Signin");
                                    out.flush();
                                    pass = false;
                                }
                            }
                        } while (!pass);
                        out.writeObject("LOGGED:".concat(username));
                        out.flush();
                    } else {

                        switch ((String) in.readObject()) {
                            case "BACKUP" -> {
                                out.writeObject(">BACKUP STARTED");
                                out.flush();

                                if (((String) in.readObject()).contentEquals("<INVIO DATI>")) {
                                    File dir = new File(Paths.get("").toAbsolutePath().toString().concat("/server/").concat(username));
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

                                        } else if ((obj instanceof String msg) && (msg.contentEquals("<FINE>"))) {
                                            out.writeObject(">BACKUP ENDED.\n>DISCONNECTED.");
                                            out.flush();
                                            break;
                                        } else {
                                            System.out.println("oggetto ricevuto non è né string né saving");
                                        }
                                    }
                                }

                            }

                            case "RECOVERY" -> {
                                System.out.println("RECOVERY");
                                out.writeObject(">RECOVERY STARTED");
                                out.flush();

                                out.writeObject("<INVIO DATI>");
                                out.flush();

                                File dir = new File(Paths.get("").toAbsolutePath().toString().concat("/server/").concat(username));
                                if (dir.listFiles().length == 0) {
                                    out.writeObject(">NESSUN SALVATAGGIO TROVATO\n<FINE>");
                                } else {
                                    for (File match : dir.listFiles()) {
                                        try (FileInputStream fileRd = new FileInputStream(match); ObjectInputStream dataRd = new ObjectInputStream(fileRd)) {
                                            out.writeObject(dataRd.readObject());
                                            out.flush();
                                        }
                                    }
                                    out.writeObject("<FINE>");
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            } finally {
                try {
                    user.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        private synchronized ResultSet DBResearch(Connection conn, String query, String id) throws SQLException {
            ResultSet rs;
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setString(1, id.toLowerCase());
            rs = pstm.executeQuery();

            return rs;
        }

        private synchronized void DBInsert(Connection conn, String query, String username, String password) throws SQLException {
            PreparedStatement pstm = conn.prepareStatement(query);
            pstm.setString(1, username);
            pstm.setString(2, password);
            pstm.executeUpdate();

        }

        private void login(ObjectOutputStream out, ObjectInputStream in, Connection conn) throws IOException, SQLException, ClassNotFoundException {
            boolean logout = false;
            boolean logged;
            do {
                logged = true;
                out.writeObject(">Inserire nome utente: ");
                out.flush();
                String line = (String) in.readObject();
                try (ResultSet rs = DBResearch(conn, "SELECT * FROM users WHERE username = ?", line)) {
                    if (rs.next()) {
                        out.writeObject(">Password: ");
                        out.flush();
                        if (rs.getString("password").contentEquals((String) in.readObject())) {
                            out.writeObject(">Accesso effettuato.\n");
                            out.flush();
                            username = line;
                        }else{
                            logged = false;
                        }
                    } else {
                        logged = false;
                        boolean pass;
                        out.writeObject(">Utente non esistente.Riprovare?");
                        out.flush();
                        line = (String) in.readObject();
                        do {
                            pass = true;
                            if (line.toLowerCase().contentEquals("no")) {
                                out.writeObject(">DISCONNECTED");
                                out.flush();
                                logout = true;
                            } else if (!line.toLowerCase().contentEquals("si")) {
                                out.writeObject("Si/No>:");
                                out.flush();
                                line = (String) in.readObject();
                                pass = false;
                            }
                        } while (!pass);
                    }
                    if (logout == true) {
                        break;
                    }
                }

            } while (!logged);

        }

        private void signin(ObjectOutputStream out, ObjectInputStream in, Connection conn) throws IOException, SQLException, ClassNotFoundException {

            out.writeObject(">Inserire nome utente: ");
            out.flush();
            String line = (String) in.readObject();
            Boolean logged;
            do {
                logged = true;
                ResultSet rs = DBResearch(conn, "SELECT * FROM users WHERE username = ?", line);
                if (rs.next()) {
                    out.writeObject(">Nome utente già esistente.\n>Inserire nome utente:");
                    out.flush();
                    line = (String) in.readObject();
                    logged = false;
                } else {
                    username = line;
                    out.writeObject(">Password:");
                    out.flush();
                    String password = (String) in.readObject();

                    DBInsert(conn, "INSERT INTO users (username, password)VALUES(?,?)", username, password);
                }

            } while (!logged);

            //crea una cartella per i salvataggi
            File dir = new File(Paths.get("").toAbsolutePath().toString().concat("/server/".concat(username.toLowerCase())));
            dir.mkdir();

        }

       
    }

    private static List<UserThread> users = Collections.synchronizedList(new ArrayList());

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(6666)) {
            System.out.println("<Server ONLINE - PORT 6666>");
            while (true) {
                try {
                    UserThread newUser = new UserThread(server.accept());
                    System.out.println("<NEW USER ATTACHED>");
                    users.add(newUser);
                    newUser.start();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
