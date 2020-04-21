import java.util.Set;

public class videoData {
    private String name;
    private String URL;


    public videoData(String name, String URL){
        this.name = name;
        this.URL = URL;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the uRL
     */
    public String getURL() {
        return URL;
    }

    
    public boolean isObjectContained(Set<videoData> s){
        
        for(videoData vD : s){
            if(this.getURL() == vD.getURL()){
                return true;
            }
        }
            return false;
       
    }
}