package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Demo extends CRUDUtils {

		public List < Object > ioc ( Class classes , String sql , Object[] objects ) {
				ResultSet rs = QueryFunction ( sql , objects );
				List < Object > list = new ArrayList <> ( );
				try {
						while ( rs.next ( ) ) {
								//得到元数据
								ResultSetMetaData rsmd = rs.getMetaData ( );
								//获取列数
								int rows = rsmd.getColumnCount ( );
								//实例化传进来的对象
								Object obj = classes.getDeclaredConstructor ( ).newInstance ( );
								//获取每一行的每一列
								for ( int i = 1 ; i <= rows ; i++ ) {
										String colums = rsmd.getColumnName ( i );
										//获取每一列对应的值
										Object columsValue = rs.getObject ( colums );
										//获取对象中的所有方法
										Method[] methods = classes.getDeclaredMethods ( );
										for ( Method method : methods ) {
												//获取所有的方法名称
												String methodName = method.getName ( );
												//查找匹配的方法 (" set " + colums )
												if ( methodName.equalsIgnoreCase ( "set" + colums ) ) {
														//把这些值加载到对应的 " set " + colums  方法中去
														method.invoke ( obj , columsValue );
														break;
												}
										}
								}
								list.add ( obj );
						}
				} catch ( SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e ) {
						e.printStackTrace ( );
				} finally {
						closeResultSet ( rs );
				}
				return list;
		}

		public static void main ( String[] args ) {
				Demo demo = new Demo ( );
				String sql = "select * from person";
//				sql = "insert into person(personName,personEmail) values('xiaoming','xiaoming@qq.com')";
				List < Object > info = demo.ioc ( Person.class , sql , null );
				for ( Object values : info ) {
						Person person = ( Person ) values;
						System.out.println ( person.getPersonId ( ) + "-------" + person.getPersonName ( ) + "-------" + person.getPersonEmail ( ) );
				}
		}
}
