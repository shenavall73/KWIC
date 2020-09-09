Key Word in Context project
---

Running the applicaiton
---

This application will produce a single text file listing all concordances parsed either from a single specified xml file 
or from all xml files found in a specified directory and recursively through its subdirectories

The XML files are expected to have the format used in the xml files found in the provided download/Texts folder of the provided zip file ota_20.500.12024_2554.zip 

The jar should be executed from the command line using the following syntax:
java -jar KWIC-1.0-SNAPSHOT.jar [filepath] [node] [window size]
Where [filepath] is the relative path to a single xml file or a directory that should be recursively searched for xml files
[node] is the node name
[window size] is the window size
EG issuing the command 'java -jar KWIC-1.0.SNAPSHOT.jar Texts/A blood 5' from the command line located in a directory containing both the jar and a folder structure Texts/A
would recursively search all XML files within Texts/A, find all sentences with the word 'blood' in each processed file, and add a concordance line to the output file 
with a window size of 5 words either side of the word 'blood' (or up to the beginning/end of the sentence where these are closer)

The output file will be named 'concordances.txt' and placed in the directory location from which the command is run.

The application will handle the whole dataset if required.

For example, running the applicaiton for node 'blood' over the whole of the 'Texts' folder took just under 2 minutes on a development-spec Windows 10 machine
and successfully parsed all files in the dataset.

Searching for common words or stopwords should not cause memory issues.  Searching the whole of Texts/A for node 'the' took around 16 seconds on the same machine as above
and succsessfully parsed all files, but producing an extremely high volume of output as would be expected.
   
Build
---

Development of the parsing and concordance logic for this project was test-driven - please see the tests in src/main/java/org/bamford/KWIC that assert the expected logic

This project is built in Maven to produce the standalone executable jar as described above in the target folder  
   
For users unfamiliar with Maven, you will need to download and unpack the latest (or recent) version of Maven (https://maven.apache.org/download.cgi) , 
create a MAVEN_HOME environment variable for the installed directory and add this to your path.
Now you can run `mvn clean install` in the top-level directory containing the pom.xml file and Maven will compile the project and put the jar file in the `target` directory.