/**
 * 
 */
package iie.igraphdb.load;

import java.util.Properties;


/**
 * created by ziploe ( jmernio@gmail.com ) at 2017/04/05 02:30
 * @version 1.0.0
 */
public class Resource {

	//数据所在文件夹
	public String dataDir;
	//日志配置文件
	public String logConf;
	//第三方日志控制配置文件
	public String secondLogConf;
	//加载客户端配置文件
	public String loadClientConf;
	
	public int readThreadNum;
	public int loadThreadNum;
	//数据要加入的图的名称
	public String graphName;
	//跳过多少行
	public int skipNum;
	public int queueSize;
	public int loadThreadSleepTime;
	
	
	//起始点label
	public String fromLabel;
	
	
	public Resource(Properties prop){
		//this.dataPath=prop.getProperty("dataPath");
		
		
		this.dataDir=prop.getProperty("dataDir");
		this.logConf=prop.getProperty("logConf");
		this.secondLogConf=prop.getProperty("secondLogConf");
		this.loadClientConf=prop.getProperty("loadClientConf");
		this.readThreadNum=Integer.parseInt(prop.getProperty("readThreadNum", "1"));
		this.loadThreadNum=Integer.parseInt(prop.getProperty("loadThreadNum", "1"));
		this.graphName=prop.getProperty("graphName");
		this.skipNum=Integer.parseInt(prop.getProperty("skipNum", "0"));
		this.queueSize=Integer.parseInt(prop.getProperty("queueSize", "1000000"));
		this.loadThreadSleepTime=Integer.parseInt(prop.getProperty("loadThreadSleepTime", "500"));
		
		
		this.fromLabel=prop.getProperty("fromLabel");
	}
}
