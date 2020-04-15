/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageviewerproject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author mac
 */
public class Slideshow implements Runnable {

    private final long DELAY = 5; //seconds each picture is shown
    private int index = 0;
    private ImageView imageView;
    private Label lblFilename;
    private final List<Image> images;
    private final List<String> filenames;
    private ExecutorService executor;

 
    public Slideshow(ImageView imageView, Label label, List<Image> images, List<String> filenames) {
        
        this.imageView = imageView;
        this.images = images;
        this.lblFilename = label;
        this.filenames = filenames;
        
    }

    @Override
    public void run() {
        if (!images.isEmpty()) {
            
            try {
            while (!Thread.currentThread().isInterrupted()) {
                Platform.runLater(() -> {

                    imageView.setImage(images.get(index));
                    lblFilename.setText(filenames.get(index));
                 
                });
                    index = (index + 1) % images.size();
                    TimeUnit.SECONDS.sleep(DELAY);
            }

        }
               catch(InterruptedException ex){
                 System.out.println("Slideshow was killed");
                 }
           }
    }

    public void stop() {
        executor.shutdownNow();
    }

    public void start() {
      executor = Executors.newSingleThreadExecutor();
      executor.submit(this);
    }

}


