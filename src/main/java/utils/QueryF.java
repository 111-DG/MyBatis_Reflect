package utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class QueryF extends CRUDUtils {
		public void ioc ( Class classes , String sql , Object[] object ) {
				ResultSet rs = QueryFunction ( sql , object );
				try {
						while ( rs.next ( ) ) {
								//得到对象的元数据
								ResultSetMetaData rsmd = rs.getMetaData ( );

								//实例化传进来的对象
								Object obj = classes.getDeclaredConstructor ( ).newInstance ( );

								//获取所有的行
								int rows = rsmd.getColumnCount ( );

								//获取行内的所有列名
								for ( int i = 1 ; i <= rows ; i++ ) {
										String columnsName = rsmd.getColumnName ( i );

										//获取列名的值
										Object columnValues = rs.getObject ( columnsName );

										Method[] methods = classes.getDeclaredMethods ( );
										for ( Method method : methods ) {
												//获取所有的方法名称
												String methodName = method.getName ( );
												//匹配到 get 方法
												if ( methodName.equalsIgnoreCase ( "get" + columnValues ) ) {
														method.invoke ( obj , columnValues );
												}
										}
								}
						}
				} catch ( SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e ) {
						e.printStackTrace ( );
				}
		}

		public static void main ( String[] args ) {
				QueryF queryF = new QueryF ( );
				String sql = "select * from phone";

				queryF.ioc ( Phone.class , sql , null );
		}
}
