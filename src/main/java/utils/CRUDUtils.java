package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUDUtils extends DBUtils {

	private static Connection con = null;
	private static PreparedStatement pstmt = null;

	public CRUDUtils () {

	}

	public boolean CUDFunction ( String sql, Object[] objects) {
		boolean b = false;
		int row = 0;
		con = getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			if (objects != null) {
				for (int i = 0; i < objects.length; i++) {
					pstmt.setObject(i + 1, objects[i]);
				}
			}
			row = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close(null, pstmt, con);
		}
		if (row > 0) {
			b = true;
		}

		return b;
	}
	
	public ResultSet QueryFunction ( String sql, Object[] objects) {
		boolean b = false;
		int row = 0;
		con = getConnection();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			if (objects != null) {
				for (int i = 0; i < objects.length; i++) {
					pstmt.setObject(i + 1, objects[i]);
				}
			}
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rs;
	}
	
	public void closeResultSet(ResultSet rs) {
		close(rs, pstmt, con);
	}

}
