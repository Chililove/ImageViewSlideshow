/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageviewerproject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author mac
 */
public class Scheduler implements Runnable{
    private Slideshow currentSlideshow = null;
    private BlockingQueue <Slideshow> queue = new LinkedBlockingQueue();
    private final int TIMESLICE = 4; 
    private ExecutorService executor = null;
    private int queueEmpty = 0;
  
   @Override
    public void run() {
        
        try{
          while(true){
          if(!queue.isEmpty()){
             runNextSlideshow(); 
          }
           TimeUnit.SECONDS.sleep(TIMESLICE);
          }
        }
        catch(InterruptedException ex){
           System.out.println("Slideshow scheduler was killed");
        }
        
    }
    
    public synchronized void runNextSlideshow()throws InterruptedException{
        currentSlideshow.stop();
        queue.put(currentSlideshow);
        currentSlideshow = queue.take();
        currentSlideshow.start();
    
    
    }
    
    public synchronized void addSlideshow(Slideshow slideshow){
           
        if(executor ==null || executor.isShutdown()){
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
        
        }
        
        if(currentSlideshow ==null && queue.isEmpty()){
        currentSlideshow = slideshow;
        currentSlideshow.start();
        }
        else{
           try{
              queue.put(slideshow);
           } 
           catch(InterruptedException ex){
            System.out.println("System scheduler was killed");
           }
        }
    
    }
    public synchronized boolean isQueueEmpty(){
    
        if(queueEmpty==1){
        return true;
        }
        
        return false;
    }
    
    public synchronized void removeCurrentSlideshow(){
      if(currentSlideshow!=null){
        currentSlideshow.stop();
        currentSlideshow = null; 
      }
      if(queue.isEmpty()){
          queueEmpty = 1;
         executor.shutdownNow();
      }
      else{
        
          try{
            currentSlideshow = queue.take();
            currentSlideshow.start();
          }
          catch(InterruptedException ex){
           System.out.println("System scheduler was killed");
      }
    } 
  }
}
