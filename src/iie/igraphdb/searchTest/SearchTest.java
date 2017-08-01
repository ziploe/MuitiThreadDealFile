package iie.igraphdb.searchTest;
public class SearchTest {
	
}
/*
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import com.google.common.collect.Collections2;
import com.thinkaurelius.titan.core.attribute.Text;

import iie.iGraphDB.core.utils.LogbackConf;
import iie.iGraphDB.schema.IGraph;
import iie.iGraphDB.schema.IGraphDBFactory;

*//**
 * created by ziploe ( jmernio@gmail.com ) at 2017/05/27 04:20
 *//*
public class SearchTest {

	
	public static void main(String[] args) {
		LogbackConf.load("f:/conf/logback.xml");
		String graphConf="f:/conf/first_graph.properties";
		//��һ��ͼ
		IGraph graph=IGraphDBFactory.open(graphConf);
		//s���еļ�����������࿪ʼ
		GraphTraversalSource g=graph.traversal();
		Set<Vertex> s;
		Set<Edge> edges;
		Set<Object> v;
		try {

			Vertex tmpV=null;
			
			//�ҳ�����name����pulu�ĵ� ������set��
			s=g.V().has("name",Text.textContains("pulu")).toSet();
			//�ҳ�����name����pulu�ĵ� ��Ȼ���ҳ�����������knows��ϵ�ĵ㣬������set��
			s=g.V().has("name",Text.textContains("pulu")).both("knows").toSet();
			//�ҳ�����name����pulu�ĵ� ��Ȼ���ҳ�����������knows��ϵ�ĵ㣬������
			long counts=g.V().has("name",Text.textContains("pulu")).both("knows").count().next();
			//�ҳ�����name����pulu�ĵ� ��Ȼ���ҳ�����������knows��ϵ�ĵ�,Ȼ���ҳ���Щ���name
			v=g.V().has("name",Text.textContains("pulu")).both("knows").values("name").toSet();
			//�ҳ�����name����pulu�ĵ� ��Ȼ���ҳ����������й�ϵ�ĵ�,ȥ�أ�Ȼ�����Щ�㰴�����ѵĶ��ٽ��н�������������list��
			g.V().has("name",Text.textContains("pulu")).both().dedup().order().by(__.both("friends").dedup().count(),Order.decr).toList();
			
			//�ҵ�age����22~24 [22,24)֮��ĵ� (����ҿ�)
			s=g.V().has("age",P.between(22, 24)).toSet();
			
			//��ע��������Խ�����composite index ����ôֱ����.has("name","pulu")����
			//���������mix ���͵�ȫ�������� ��ô��Ҫʹ��.has("name",Text.textContains("pulu"))
			//�ҳ�����name����pulu�ĵ�,Ȼ���ҳ�������������������knows��ϵ�ĵ㣬ȥ���м�������ȥ�����ս��
			s=g.V().has("name","pulu").repeat(__.both("knows").dedup()).times(2).dedup().toSet();
			
			//�ҳ�����name����pulu�ĵ�,Ȼ���ҳ�������������������knows��ϵ�ĵ�,�������ҵĹ����У���������Щ����knows��ϵ�ĵ������������22
			s=g.V().has("name","pulu").repeat(__.both("knows").dedup().where(__.both("knows").count().is(P.lt(22)))).times(2).dedup().toSet();
			
			//�ҵ�uidΪ1823 ����31241�ĵ㣬������list��
			List<Vertex> list=g.V().has("uid",P.within("1823","31241")).toList();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdfd=new SimpleDateFormat("yyyy-MM-dd");
			//�ҵ�����2017-11-12 22:22:11 ~ 2017-12-12 22:22:11֮��ĵ�
			g.V().has("birth",P.between(sdf.parse("2017-11-12 22:22:11"), sdf.parse("2017-12-12 22:22:11"))).toSet();
			
			//�ҵ�����contents�а���Hadoop�ı�
			edges=g.E().has("contents",Text.textContains("hadoop")).toSet();
			
			//�ҵ�����contents�а���Hadoop�ı�,Ȼ���ҳ���Щ�����˵ĵ�
			s=g.E().has("contents",Text.textContains("hadoop")).both().toSet();
			//�ҵ��ֻ���Ϊ110�ĵ㣬Ȼ���ҳ������������Ķ���
			edges=g.V().has("phoneNum","110").outE("message").toSet();
			//�ҵ��ֻ���Ϊ110�ĵ㣬Ȼ���ҳ�����2017-11-12 ~ 2017-12-12֮�䷢���Ķ���
			edges=g.V().has("phoneNum","110").outE("message").has("time",P.between(sdfd.parse("2017-11-12"), sdfd.parse("2017-12-12"))).toSet();
			//�ҳ�����title�к���Hadoop�ĵ�
			s=g.V().has("title",Text.textContains("hadoop")).toSet();
			//�ҳ�����title�к���Hadoop�ĵ�,Ȼ���ҳ���reference�ĵ�
			s=g.V().has("title",Text.textContains("hadoop")).out("reference").toSet();
			//�ҳ�����labelΪreason�Ұ���hdfs�ַ����ı� ������set��   textContains��ȫ�ļ���
			Set<Edge> e=g.E().has("reason",Text.textContains("hdfs")).toSet();
			
			//�ҳ�title�а���"bkd tree"�ĵ㣬����Ϊb���棬�ҳ�b���õĵ���Ϊc��ȡ��b��c��title
			g.V().has("title",Text.textContains("bkd tree")).as("b").out("reference").as("c").select("b","c").by("title");
			g.V().has("title",Text.textContains("bkd tree")).as("a","b","c").select("a", "b", "c").by("title");
			g.V().has("title",Text.textContains("bkd tree")).as("a","b","c")
				.select("a", "b", "c").by("title")
				.by("year").by(__.out("reference").fold());
			
			
			g.V().has("title",Text.textContains("bkd tree")).out("reference").groupCount("c").by("year").cap("c").toList();
			
			g.V().has("title",Text.textContains("bkd tree")).in("reference").group().by(__.inE().count());
	
			
			
			
			
			g.V().has("rank",13444).group().by("year").by("title");
			g.V().has("rank",P.lt(12000)).timeLimit(20000).values("year").is(P.lt(2002));
			g.V().has("rank",17723).range(1000,1300);
			
			g.V().has("title",Text.textContains("hypnosis")).order().by("year",Order.incr).values("year");

			g.V().has("title",Text.textContains("hypnosis")).order().by(__.inE().count(),Order.decr).values("year","title");
			
			g.V().has("title",Text.textContains("hypnosis")).group().by("year").order().by("year",Order.incr);

			g.V().has("title",Text.textContains("yoga")).limit(10).as("a")
				.select("a").by("title").as("b").select("a").by("year").as("c").select("b","c");
			
			//Keys.class;
			
			g.V().has("rank",14443).group().by(__.bothE().count()).next();
			g.V().has("rank",14443).group().by(__.bothE().count()).by("uid").next();
			
			s=g.V().has("name",Text.textContains("pulu")).toSet();
			System.out.println("vertex size: "+s.size());
			Iterator<Vertex> iter=s.iterator();
			
			while(iter.hasNext()){
				tmpV=iter.next();
				
				if(tmpV.keys().contains("age")&&tmpV.keys().contains("name")){
					System.out.println("[name: "+tmpV.<String>value("name")+", age: "+tmpV.<Integer>value("age")+" ]");

				}else{
					System.out.println("[�����˸������������...]");
				}
			}
			P.between(2, 3);
			
			System.out.println("-----����22~24֮��---------");
			s=g.V().has("age",P.<Integer>between(22, 24)).toSet();
			iter=s.iterator();
			while(iter.hasNext()){
				tmpV=iter.next();
				System.out.println("[name: "+tmpV.<String>value("name")+", age: "+tmpV.<Integer>value("age")+" ]");
			}
			System.out.println("--------------------");
			s=g.V().has("age",P.neq(23)).toSet();
			iter=s.iterator();
			while(iter.hasNext()){
				tmpV=iter.next();
			
				System.out.println("[name: "+tmpV.<String>value("name")+", age: "+tmpV.<Integer>value("age")+" ]");
			}
			
			System.out.println("--------------------");
			s=g.V().has("name",Text.textContains("a1")).toSet();
			iter=s.iterator();
			while(iter.hasNext()){
				tmpV=iter.next();
				System.out.println("[name: "+tmpV.<String>value("name")+", age: "+tmpV.<Integer>value("age")+" ]");
			}
				

		} catch (Exception e) {

			e.printStackTrace();
		}finally {

			graph.close();
		}
	}
}
*/