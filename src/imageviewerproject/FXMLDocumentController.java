package imageviewerproject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable
{
    private final Scheduler scheduler = new Scheduler();
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;

    @FXML
    Parent root;
    @FXML
    private Label lblFilename;

    @FXML
    private Button btnStart;
    
    @FXML
    private Button btnStop;
    
    @FXML
    private ImageView imageView;
   
  
    
    @FXML
    private void handleBtnStartAction(ActionEvent event){
        
       List<String>filenames = new ArrayList<>();
       List<Image> images = new ArrayList<>();
       FileChooser fileChooser = new FileChooser();
       fileChooser.setTitle("Select image files");
       fileChooser.getExtensionFilters().add(new ExtensionFilter("Images", 
            "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));        
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        
        if (files!=null)
        {
            files.forEach((File f) ->
            {   
                filenames.add(f.getName());
                images.add(new Image(f.toURI().toString()));
            });
           // displayImage(); instead:
          
           Slideshow slideshow = new Slideshow(imageView,lblFilename, images, filenames);
           
           scheduler.addSlideshow(slideshow);
        }
       
    
    }
    
    @FXML
    private void handleBtnStopAction(ActionEvent event){
        
        scheduler.removeCurrentSlideshow();
        
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
        btnStart.setOnAction((ActionEvent event) ->
        {
            handleBtnStartAction(event);
        });
        
        btnStop.setOnAction((ActionEvent event) ->
        {
            handleBtnStopAction(event);
        });
    }

}
