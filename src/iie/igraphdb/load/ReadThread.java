package iie.igraphdb.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;



/** 
 * created by ziploe ( jmernio@gmail.com ) at 2017/05/31 05:57
 *
 * 从文件读取一行行数据，放入队列中
 */
public class ReadThread extends Thread{

	private static Logger logger = Logger.getLogger(ReadThread.class);
	Resource resource;
	Status status;
	int _M=1000000;
	FileContainer fileContainer;
	ArrayBlockingQueue<String> orgQueue;


	public ReadThread(ArrayBlockingQueue<String> orgQueue,Resource resource,Status status,FileContainer fileContainer){
		this.orgQueue=orgQueue;
		this.skipNum=resource.skipNum;
		this.resource=resource;
		this.status=status;
		this.fileContainer=fileContainer;
	}

	BufferedReader bf=null;
	String tmpLine;
	int dataSize;
	long globalReadNum;

	int skipNum=0;
	@Override
	public void run(){
		logger.info("read data thread started!");
		status.registReadThread(this);
		Optional<File> currentFile;
		while(true){
			currentFile=fileContainer.getNetFile();
			if(!currentFile.isPresent()){
				break;
			}
			logger.info("reading data from file "+currentFile.get().getName());
			dataSize=0;
			try {
				bf=new BufferedReader(new InputStreamReader(new FileInputStream(currentFile.get())));
				while((tmpLine=bf.readLine())!=null){
					dataSize++;
					globalReadNum=status.readLineNum.incrementAndGet();
					if(globalReadNum<skipNum){
						continue;
					}

					orgQueue.put(tmpLine);
					if(globalReadNum%_M==0){
						logger.info("total "+globalReadNum+" pieces of data has been read to queue");
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					bf=null;
				}
			}	
			logger.info("file ["+currentFile.get().getName()+"] read complete. "+dataSize+" pieces of data read to queue");
		}
		status.unregistReadThread(this);
		
	}



}
