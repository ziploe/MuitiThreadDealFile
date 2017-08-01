/**
 * 
 */
package iie.igraphdb.load;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * created by ziploe ( jmernio@gmail.com ) at 2017/07/31 10:58
 */
public class Status {

	public AtomicLong readLineNum;
	public AtomicLong sendedLineNum;
	public Status(){
		
		readLineNum=new AtomicLong(0);
		sendedLineNum=new AtomicLong(0);
	}
	
	
	Map<String, ReadThread> readThreads;
	public synchronized void registReadThread(ReadThread readThread){
		if(readThreads==null) readThreads=new HashMap<>();
		readThreads.put(readThread.getName(), readThread);
	}
	public synchronized void unregistReadThread(ReadThread readThread){
		readThreads.remove(readThread.getName());
	}
	public boolean readEnds(){
		return readThreads!=null&&readThreads.isEmpty();
	}
}
