
class FileLoader {

  String folderPath;

  FileLoader() {

  } 

  File[] filePaths( String subFolder ) {

    folderPath = sketchPath + subFolder;

    if ( folderPath != null ) {
      File file = new File(folderPath);
      File[] files = file.listFiles();

      if ( files != null ) {
        return files;
      } 
      else {
        println( "No Files Found Here:" ); 
        println(  folderPath );
        return null;
      }
    } 
    else {
      println( "No Such Folder" ); 
      return null;
    }
  } // End of FilePaths
}

