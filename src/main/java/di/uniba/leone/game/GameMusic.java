/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package di.uniba.leone.game;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author feder
 */
public class GameMusic implements Runnable {

    private final String SOUNDGAME = "./src/main/resources/music/background_soundtrack.wav";
    private boolean playing;
    private Thread playertThread;
    private Clip audioClip;

    /**
     *
     */
    public GameMusic() {
        this.playing = false;
    }

    /**
     *
     */
    @Override
    public void run() {

        try {
            File audio = new File(SOUNDGAME);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio);
            AudioFormat format = audioStream.getFormat();
            
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            while (playing) {
                audioClip.setFramePosition(0);
                audioClip.start();
                while (playing && audioClip.isRunning()) {
                    Thread.sleep(10);
                }
                audioClip.stop();
            }
            audioClip.close();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
            System.out.println("Il file audio non puo' essere riprodotto, controllare");
        }
    }

    /**
     *
     */
    public void stopMusic() {
        playing = false;
        
        if(audioClip != null){
            audioClip.stop();
            audioClip.close();
        }
        if(playertThread != null){
            playertThread.interrupt();
            try{
                playertThread.join();
            }catch(InterruptedException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     *
     */
    public void startMusic(){
        if(!playing){
            playing = true;
            playertThread = new Thread(this);
            playertThread.start();
        }
    }

}
