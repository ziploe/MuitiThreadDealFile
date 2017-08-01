/**
 * 
 */
package iie.igraphdb.load;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

//import com.ziploe.detector.BadNumFilter;

import iie.iGraphDB.client.LoadClient;
import iie.iGraphDB.core.utils.CommonUtils;
import iie.iGraphDB.schema.PieceOfData;

/**
 * created by ziploe ( jmernio@gmail.com ) at 2017/04/06 09:41
 * 
 */
public class LoadThread extends Thread{

	private static Logger logger = Logger.getLogger(LoadThread.class);
	Resource resource;
	Status status;
	ArrayBlockingQueue<String> orgQueue;
//	BadNumFilter filter;
	LoadClient client;
	SimpleDateFormat format;
	public LoadThread(ArrayBlockingQueue<String> orgQueue,Resource resource,Status status) throws ConfigurationException {
		this.orgQueue=orgQueue;
		this.resource=resource;
		this.status=status;
		client=new LoadClient(resource.loadClientConf);
		format=new SimpleDateFormat("yyyyMMddHHmmss");
//		filter=new BadNumFilter(resource.badDataPath);
	}
	String tmpLine;
	long current,threadProcessedDataNum=0,totalProcessedDataNum;
	@Override
	public void run(){
		logger.info(this.getName()+" started!");
		PieceOfData pie;
		long start=System.currentTimeMillis();
		try{	
			while(true){
				tmpLine = orgQueue.poll();
				if(null==tmpLine){
					if(status.readEnds()){
						break;
					}
					logger.info(this.getName()+": you are too slow, I will sleep "+resource.loadThreadSleepTime+" ms and wait you for a while");
					sleep(resource.loadThreadSleepTime);
					continue;

				}
				try{
					pie=mapper(tmpLine, format);
					if(pie==null) continue;
					client.put(pie);
					totalProcessedDataNum=status.sendedLineNum.incrementAndGet();
					threadProcessedDataNum++;
					if(totalProcessedDataNum%5000000==0){
						logger.info(totalProcessedDataNum+" pieces of data sended to server");
					}
				}catch(Exception e){
					logger.error(CommonUtils.exceptionToString(e)+" : "+tmpLine);
					continue;
				}
				
			}
			client.commit();
			client.close();
		}catch(Exception e){
			logger.error(this.getName()+": "+e.toString());
		}finally{
			logger.info(this.getName()+" ends");
			current=System.currentTimeMillis();
			logger.info(this.getName()+": threadTotal "+threadProcessedDataNum+" pieces of data had been sent to server "
					+"time costs "+(current-start)/1000+" s");
			logger.info(this.getName()+": total "+status.sendedLineNum.get()+" pieces of data had been sent to server "
					+"time costs "+(current-start)/1000+" s");
		}
	}
	
	/**
	 * 用户主要重写这个函数，负责将自己的数据格式转化为pieceOfData类型数据  <br/>
	 * 这里是一个将String类型数据解析为pieceOfData类型的示例。
	 * 将一条string类型数据解析为图数据库专用数据类型 pieceOfData
	 * @param data 原始数据
	 * @param format 
	 * @return
	 * @throws ParseException
	 */
	public PieceOfData mapper(String data,SimpleDateFormat format) throws ParseException{
		
		String[] para=data.split(",");
//		if(filter.contains(para[1])||filter.contains(para[2])) return null;
		PieceOfData pie=new PieceOfData(resource.graphName);
		Map<String, Object> from=new HashMap<>();
		Map<String, Object> to=new HashMap<>();
		Map<String, Object> call=new HashMap<>();
		from.put("port", para[9]);
		from.put("location", para[16]);
		pie.setPoint("uid", para[1],"node", from);
		to.put("port", para[10]);
		to.put("location", para[17]);
		pie.setAnotherPoint("uid", para[2], "node", to);
		call.put("time",format.parse(para[8]));
		pie.addRelation("reference", call);
		return wash(pie);
	}
	
	//可在这个函数里进行数据过滤，过滤掉不需要的一些数据
	public static PieceOfData wash(PieceOfData pie){
		
		return pie;
	}
}
