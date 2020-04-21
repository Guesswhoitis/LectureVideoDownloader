import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Scanner;


public class VideoDownloader{
    private HashMap<String,ArrayList<String>> RSSFeedsPaths = new HashMap<>();                                               //stores the name of the class, the URL of the RSS feed and the Path you wish to download too. Uses the name as a key
    private HashMap<String,List<videoData>> videos = new HashMap<>();                                                        //stores all the video URLs from the class, uses the name as a key

    private String mainFilePath = "mainFile";                                                                                //path to the mainFile
    
    private boolean found = false;
    

    /**
     * used for the main running of the program
     * will call all required methods to download items it needs
     */
    public VideoDownloader(){
        loadMainFile();
        if(found){
            downloadRSSFeed();
            downloadVideo();
        }
    }

   

    /**
     * Downloads all of the videos
     * loops through the amount of RSS feeds
     * the loops through all of the videos inside that RSS feed
     * Downloads the videos using the supplied URL in the mainFile and stores it in path provided in mainFile
     */
    
    public void downloadVideo(){
        System.out.println("Attempting to download videos");
        for(String s : videos.keySet()){                                                                                          //loops through all of the classes you are downloading videos for
            
            for(videoData vD : videos.get(s)){                                                                                    // loops throgu all of the video URLs for that class then downloads the video
                try{
                    System.out.println("Downloading "+ vD.getName());
                    BufferedInputStream in = new BufferedInputStream(new URL(vD.getURL()).openStream());
                    

                    File file = new File(RSSFeedsPaths.get(s).get(2)+"/",vD.getName()+".mp4");

                    if(!file.exists()){
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte dataBuffer[] = new byte[8000];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 8000)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }
                        fileOutputStream.close();
                        System.out.println("Successfully downloaded " + vD.getName() + ".mp4");
                    }else{
                        System.out.println("already downloaded " + vD.getName());
                    }

                }catch(IOException e){
                    System.out.println("Something went wrong when downloading the video");
                    System.out.println(e);
                }
                
            }
        }
    }


    

    
    /**
     * Used to download the RSS feed
     */
    public void downloadRSSFeed(){
        System.out.println("Attempting to download RSS feeds");
        for(String s : RSSFeedsPaths.keySet()){                                                                                    //loops through the lecture classes you want to download
            System.out.println("Downloading "+s+" RSS feed");

            File file = new File(RSSFeedsPaths.get(s).get(2));                                                                     //creates a file of the path you want to store it at
            if(!file.isDirectory()){                                                                                               //checks if the directory exsist, if it doesnt it will create it
                System.out.println("Directory for " + s + " does not exsist creating it for you now");
                file.mkdir();
            }
            
            try {
                BufferedInputStream in = new BufferedInputStream(new URL(RSSFeedsPaths.get(s).get(1)).openStream());               // Creates a BufferedInputStream to connect and download the file in the URL
                file = new File(RSSFeedsPaths.get(s).get(2),s+".ashx");                                                            //creates the file that you will download into                                                  
                FileOutputStream fileOutputStream = new FileOutputStream(file);                                                    //dont quiet know what this does but Im assuming it is the file you write too
                byte dataBuffer[] = new byte[1024];                                                                                //again not sure but could be the byte size you download?
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {                                                         //Dont even ask me what this does haha
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                in.close();
                fileOutputStream.close();
                System.out.println("Download of " +s+" RSS feed is complete");
            } catch (IOException e) {
                System.out.println("Download failed");
                System.out.println("Please check the mainFile is formatted correctly");
                System.out.println(e);
            }

            
            System.out.println("Reading RSS feed from "+ s+" class");                                                               //used for loading the links to the lecture videos from the RSS feed into the program
            try{
                Scanner sc = new Scanner(file);

                String name = null;
                String URL = null;

                while(sc.hasNextLine()){ 
                                                                                                                                    //iterates through the RSS feed
                    String line = sc.nextLine();
                    if(line.contains(".mp4") && line.contains("url")){                                                              //searches for lines that contain .mp4 and URL
                        String a[] = line.split("\"");
                        URL = a[1];
                        
                    }else if(line.contains("title") && line.contains("/title")){
                        String a = line.substring(line.indexOf(">")+1,line.length());
                        a = a.substring(0,a.indexOf("<"));
                        a=a.replace("/","_");
                        a=a.replace(" ", "_");
                        
                        name = a;

                    }

                    if(URL != null && name != null){

                        videoData vD = new videoData(name, URL);
                        
                        
                            videos.get(s).add(vD);
                        
                        
                     name = null;
                     URL = null;
                    }
                }
                sc.close();
                System.out.println("Finished reading file successfully");
            }catch(IOException e){
                System.out.println("An issue has occured while reading the file and loading the videos");
                System.out.println(e);
            }
        }
    }



    /**
     * loads the main file containing the path videos 
     * will download to as well as the URLs to the 
     * RSS feeds you wish to download
     */

    public void loadMainFile(){
        System.out.println("Attempting to load mainfile");
        File mainFile = new File(mainFilePath);
        try{
            Scanner sc = new Scanner(mainFile);
           
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                if(!line.startsWith("#")){
                    ArrayList<String> data = new ArrayList<>();                                                                     //creates arraylist to store the name, the link, and the path
                    data.add(line);                                                                                                 //first line will be the name
                    data.add(sc.nextLine());                                                                                        //second will be the link to the RSS Feed
                    data.add(sc.nextLine() +"/"+ line);                                                                             //Third wil be the path on your device you with to store
                    RSSFeedsPaths.put(line, data);
                    videos.put(line,new ArrayList<videoData>());                                                                //adds the information to a map to use at a later time      
                }
            }
            
            sc.close();
            System.out.println("mainFile loaded successfully");
            found = true;
        }catch(IOException e){
            System.out.println("Either the file was not found successfully or the file is not formatted correctly");
            System.out.println("Please check that there is a file called mainFile in the directory of the program");
            System.out.println(e);
        }
    }



    public static void main(String[] args) {
        new VideoDownloader(); 
    }
}