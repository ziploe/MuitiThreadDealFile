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

	//���������ļ���
	public String dataDir;
	//��־�����ļ�
	public String logConf;
	//��������־���������ļ�
	public String secondLogConf;
	//���ؿͻ��������ļ�
	public String loadClientConf;
	
	public int readThreadNum;
	public int loadThreadNum;
	//����Ҫ�����ͼ������
	public String graphName;
	//����������
	public int skipNum;
	public int queueSize;
	public int loadThreadSleepTime;
	
	
	//��ʼ��label
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
