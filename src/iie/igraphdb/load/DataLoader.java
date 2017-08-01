/**
 * 
 */
package iie.igraphdb.load;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.PropertyConfigurator;
import iie.iGraphDB.core.utils.CommonUtils;
import iie.iGraphDB.core.utils.LogbackConf;
import iie.igraphdb.load.specific.LoadThread_phoneNum_imei;

/**
 * created by ziploe ( jmernio@gmail.com ) at 2017/04/05 10:18
 * @version 1.0.0
 */
public class DataLoader {

	
	public static void main(String[] args) {
		if(args.length!=1){
			System.out.println("where is the properties file");
			System.exit(1);
		}
		
		Properties config= new Properties();
		try {
			InputStreamReader isr=null;	
			isr=new InputStreamReader(new FileInputStream(args[0].trim()));
			config.load(isr);
			isr.close();
			Resource resource=new Resource(config);
			Status status=new Status();
			LogbackConf.load(resource.secondLogConf);
			PropertyConfigurator.configure(resource.logConf);
			FileContainer fileContainer=new FileContainer(resource.dataDir);
			ArrayBlockingQueue<String> orgQueue=new ArrayBlockingQueue<>(resource.queueSize);
			
			for(int i=0;i<resource.loadThreadNum;i++){
			//	LoadThread loader=new LoadThread(orgQueue, resource,status);
				LoadThread_phoneNum_imei loader=new LoadThread_phoneNum_imei(orgQueue, resource, status);
				loader.setName("loader"+i);
				loader.start();
			}
			for(int i=0;i<resource.readThreadNum;i++){
				ReadThread reader=new ReadThread(orgQueue, resource,status,fileContainer);
				reader.setName("reader"+i);
				reader.start();
			}
		} catch (Exception e) {
			System.out.println(CommonUtils.exceptionToString(e));
			System.exit(1);
		}
		
	}
	
	
	
	
	
	
	
/*	
	public static void main(String[] args) {
		LogbackConf.load("F:\\tmp\\logback_receiver.xml");
		IGraphDBClient client=IGraphDBClient.builder().set("bootstrap.servers", "172.16.2.201:9092,172.16.2.202:9092,172.16.8.101:9092")
				.set("batchSize", "1")
				.set("metaDB_url","jdbc:mysql://172.16.2.124:3306/igraphdb")
				.set("batchSize", "100")
				.build();
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
		String data="3942683709,4212124063,1131359932,563647549,162,121,5000,735000,20160510061211,35730,8832,011000,6,0,0,0,����,����,--,--,--,--";
		try {
			PieceOfData2 pie=mapper(data, format);
			System.out.println(pie.toJSONString());
			client.put(pie);
			client.put(pie);
			client.put(pie);
			client.put(pie);
			client.commit();
			client.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static PieceOfData2 mapper(String data,SimpleDateFormat format) throws ParseException{
		String[] para=data.split(",");
		PieceOfData2 pie=new PieceOfData2("call");
		Map<String, Object> from=new HashMap<>();
		Map<String, Object> to=new HashMap<>();
		Map<String, Object> call=new HashMap<>();
		from.put("port", para[9]);
		from.put("location", para[16]);
		pie.setPoint("uid", para[1],"node", from);
		to.put("port", para[10]);
		to.put("location", para[17]);
		pie.setAnotherPoint("uid", para[2], "node", to);
		//format=new SimpleDateFormat("yyMMddHHmmss");
		//20160510094846
		call.put("time",format.parse(para[8]));
		pie.addRelation("reference", call);
		return pie;
	}*/
}
