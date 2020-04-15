import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class VideoDownloader{
    private HashMap<String,ArrayList<String>> RSSFeedsPaths = new HashMap<>();                                               //stores the name of the class, the URL of the RSS feed and the Path you wish to download too. Uses the name as a key
    private HashMap<String,LinkedHashSet<String>> videos = new HashMap<>();                                                  //stores all the video URLs from the class, uses the name as a key
    private Set<String> alreadyDownloaded = new HashSet<>();                                                                 //A set to store all of the already downloaded videos


    private String mainFilePath = "mainFile";                                                                                //path to the mainFile
    private String loadedFilePath = "written";                                                                               //path to the file containing the already downloaded videos

    

    /**
     * used for the main running of the program
     * will call all required methods to download items it needs
     */
    public VideoDownloader(){
        loadMainFile();
        downloadRSSFeed();
        downloadVideo();
        writeAlreadyAdded();

    }

    /**
     * Writes to a file a collection of already downloaded videos
     */
    public void writeAlreadyAdded(){
        System.out.println("Writing to file to store whats already been downloaded");
        try{
            File file = new File(loadedFilePath);                                                                                
            FileWriter fWriter = new FileWriter(file);                                                                           //writes the videos that have already been downloaded to a file
            for(String s : alreadyDownloaded){                                                                                   // loops through the videos that have been downloaded
                fWriter.write(s+'\n');
            }
            fWriter.close();
            System.out.println("Successfully written to file");
        }catch(IOException e){
            System.out.println("Error writing to file");
            System.out.println(e);
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
            int i =0;
            for(String url : videos.get(s)){                                                                                      // loops throgu all of the video URLs for that class then downloads the video
                try{
                    System.out.println("Downloading "+ videos.get(s));
                    BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                    File file = new File(RSSFeedsPaths.get(s).get(2),s+i+".mp4");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte dataBuffer[] = new byte[8000];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 8000)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    alreadyDownloaded.add(url);
                    System.out.println("Successfully downloaded " + s+i+".mp4");

                }catch(IOException e){
                    System.out.println("Something went wrong when downloading the video");
                    System.out.println(e);
                }
                i++;
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

                while(sc.hasNextLine()){                                                                                            //iterates through the RSS feed
                    String line = sc.nextLine();
                    if(line.contains(".mp4") && line.contains("url")){                                                              //searches for lines that contain .mp4 and URL
                        String a[] = line.split("\"");
                        if(!alreadyDownloaded.contains(a[1])){                                                                      // if the video has already been downloaded, dont add it to the videos that you will download
                            videos.get(s).add(a[1]);
                        }
                        
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
                    videos.put(line,new LinkedHashSet<String>());                                                                   //adds the information to a map to use at a later time
                }
            }
            
            sc.close();
            System.out.println("mainFile loaded successfully");
        }catch(IOException e){
            System.out.println("Either the file was not found successfully or the file is not formatted correctly");
            System.out.println("Please check that there is a file called mainFile in the directory of the program");
            System.out.println(e);
        }



        File alreadyDownloadedFile = new File(loadedFilePath);                                                                       // loads the file that contains the URLs that have already Been downloaded
        try{
            System.out.println("Loaded already downloaded videos");                                                 
            Scanner sc = new Scanner(alreadyDownloadedFile);

            while(sc.hasNextLine()){                                                                                                 // loops through the file to read what videos have already been downloaded
               alreadyDownloaded.add(sc.nextLine());    
            }
            sc.close();
            System.out.println("Successfully loaded already downloaded videos");

        }catch(IOException e){
            System.out.println("There was an issue while loading the already downloaded videos");
            System.out.println(e);
        }



    }



    public static void main(String[] args) {
        new VideoDownloader(); 
    }
}