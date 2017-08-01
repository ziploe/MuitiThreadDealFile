/**
 * 
 */
package iie.igraphdb.load.specific;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.json.JSONException;

//import com.ziploe.detector.BadNumFilter;

import iie.iGraphDB.client.LoadClient;
import iie.iGraphDB.core.utils.CommonUtils;
import iie.iGraphDB.schema.PieceOfData;
import iie.iGraphDB.utils.MapUtil;
import iie.igraphdb.load.Resource;
import iie.igraphdb.load.Status;

/**
 * created by ziploe ( jmernio@gmail.com ) at 2017/04/06 09:41
 * 
 */
public class LoadThread_phoneNum_imei extends Thread{

	private static Logger logger = Logger.getLogger(LoadThread_phoneNum_imei.class);
	Resource resource;
	Status status;
	ArrayBlockingQueue<String> orgQueue;
//	BadNumFilter filter;
	LoadClient client;
	public LoadThread_phoneNum_imei(ArrayBlockingQueue<String> orgQueue,Resource resource,Status status) throws ConfigurationException {
		this.orgQueue=orgQueue;
		this.resource=resource;
		this.status=status;
		client=new LoadClient(resource.loadClientConf);
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
					pie=map_phoneNum_imei(tmpLine,resource.fromLabel);
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
	
	
	public  static PieceOfData map_phoneNum_imei(String data,String fromLabel){
		String[] para=data.split(",");
		if(para.length!=4) throw new IllegalArgumentException("data is not a legal one");
		String toLabel=para[1].substring(8, para[1].length()-1);
		PieceOfData pie=new PieceOfData("test");
		pie.setPoint(fromLabel, para[0].substring(7, para[0].length()),fromLabel, MapUtil.newEmptyMap());
		pie.setAnotherPoint(toLabel, para[2], toLabel, MapUtil.newEmptyMap());
		pie.addRelation(para[3], MapUtil.newEmptyMap());
		return pie;
	}
	public  static String map_phoneNum_imeiis(String data,String fromLabel){
		String s=null;
		try {
			s =map_phoneNum_imei(data,fromLabel).toJSONString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) {
		String data="ACCOUNT15003587,\"ACCOUNTIMEI\",86454349866,KNOWS";
		String data2="ACCOUNT132587,\"ACCOUNTIMSI\",86134134866,KNOWS";
		String data3="ACCOUNT1533587,\"ACCOUNTIMAC\",81DWD4349866,KNOWS";
		String data4="ACCOUNT1526003587,\"ACCOUNTQQ\",83413866,KNOWS";
		try {
			System.out.println(map_phoneNum_imei(data,"ACCOUNT").toJSONString());
			System.out.println(map_phoneNum_imei(data2,"ACCOUNT").toJSONString());
			System.out.println(map_phoneNum_imei(data3,"ACCOUNT").toJSONString());
			System.out.println(map_phoneNum_imei(data4,"ACCOUNT").toJSONString());
			System.out.println("----------------------------");
			Files.lines(Paths.get("E:\\split.txt")).map(x->map_phoneNum_imeiis(x,"ACCOUNT")).forEach(System.out::println);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//可在这个函数里进行数据过滤，过滤掉不需要的一些数据
	public static PieceOfData wash(PieceOfData pie){
		
		return pie;
	}
}
