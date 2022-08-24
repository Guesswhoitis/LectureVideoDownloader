# This was a project I created years ago. I am making it public purely to show it on my GitHub

# LectureVideoDownloader
A small commandLine program to download all of your lectures from a supplied RSS feed.

All information on how to run the program is given in the supplied readMe

Fair warning, may not work with all RSS feeds

To run the program you need to have a text file called "mainFile" in the same directory as the program.

The contents must be played out in a specific way in the "mainFile" with the first line of what you want being the name of the lecture, the second line being the URL to the RSS feed and the third being the path you with to download too.
e.g 
    ClassName
    Url Of Rss feed
    Path you wish to store at



e.g
    ComputerScience
    https://YourRssFeedAtWhatEverAddress.com/Whatever
    /home/userName/Documents/yourCoolDirectory

You can add multiple different groups or different lecture classes by just repeating the 3 lines
e.g
    Networking
    https://NetworkingThingIdontKnowThisIsAnExample
    /home/userName/Documents/yourCoolDirectory2
    ComputerScience
    https://YourRssFeedAtWhatEverAddress.com/Whatever
    /home/userName/Documents/yourCoolDirectory
