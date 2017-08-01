/**
 * 
 */
package iie.igraphdb.load;

import java.io.File;
import java.util.Optional;

/**
 * created by ziploe ( jmernio@gmail.com ) at 2017/07/31 10:41
 */
public class FileContainer {

	private File[] files;
	private int index=0;
	public FileContainer(String fileDir){
		File dir=new File(fileDir);
		if(!dir.isDirectory()){
			System.out.println(fileDir +" is not a dir");
			System.out.println("system exiting...");
			System.exit(1);
		}
		files=dir.listFiles();
	}
	
	public synchronized Optional<File> getNetFile(){
		File file=null;
		if(index<files.length){
			file=files[index];
			index++;
		}
		return Optional.ofNullable(file);
	}
}
