package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext; // 이름을 찾기 위한 작업을 위해 사용하는 클래스
import javax.naming.NamingException;
import javax.sql.DataSource; // 실질적인 어떤 데이터인지를 연결하는 객체 

public class MemberDAO {
//	필
	private Connection conn;
	private DataSource dataFactory;
	private PreparedStatement pstmt;

//	생

	public MemberDAO() {
		// JNDI방식의 연결로 MemberDAO 객체를 초기화
		try {
			Context ctx = new InitialContext(); // 컨텍스트 작업을 위한 객체
			Context envContext = (Context) ctx.lookup("java:/comp/env"); // 오라클인지 mysql인지 환경을 찾기 위한 컨텍스트
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (NamingException e) {
			System.out.println("톰캣의 context.xml에 정의 되어있는 이름부분에서 정확하지 않은 에러");
//			e.printStackTrace();
		}
	}

//	메
	public List<MemberBean> listMembers() {
		List<MemberBean> membersList = new ArrayList();
		try {
			conn = dataFactory.getConnection();
			String query = "select * from t_member order by joinDate desc";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String userId = rs.getString("userId");
				String userPass = rs.getString("userPass");
				String userName = rs.getString("userName");
				String userEmail = rs.getString("userEmail");
				Date joinDate = rs.getDate("joinDate");
				MemberBean memberVO = new MemberBean(userId, userPass, userName, userEmail, joinDate);
				membersList.add(memberVO);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return membersList;
	}

//	private void connDB() {} // connectionDB이므로 미리 연결 객체를 만들어 놨기 때문에 필요 없음

	public void addMember(MemberBean memberBean) {
		try {
			conn = dataFactory.getConnection();
			String query = "insert into t_member";
			query += "(userId,userPass,userName,userEmail)";
			query += " values(?,?,?,?)";
			System.out.println("prepareStatememt: " + query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, memberBean.getUserId());
			pstmt.setString(2, memberBean.getUserPass());
			pstmt.setString(3, memberBean.getUserName());
			pstmt.setString(4, memberBean.getUserEmail());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MemberBean findMember(String _id) {
		MemberBean memInfo = null;
		try {
			conn = dataFactory.getConnection();
			String query = "select * from t_member where userId=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, _id);
			System.out.println(query);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String userId = rs.getString("userId");
			String userPass = rs.getString("userPass");
			String userName = rs.getString("userName");
			String userEmail = rs.getString("userEmail");
			Date joinDate = rs.getDate("joinDate");
			memInfo = new MemberBean(userId, userPass, userName, userEmail, joinDate);
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memInfo;
	}

	public void modMember(MemberBean memberBean) {
		String userId = memberBean.getUserId();
		String userPass = memberBean.getUserPass();
		String userName = memberBean.getUserName();
		String userEmail = memberBean.getUserEmail();
		try {
			conn = dataFactory.getConnection();
			String query = "update t_member set userPass=?,userName=?,userEmail=? where userId=?";
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userPass);
			pstmt.setString(2, userName);
			pstmt.setString(3, userEmail);
			pstmt.setString(4, userId);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delMember(String userId) {
		try {
			conn = dataFactory.getConnection();
			String query = "delete from t_member where userId = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}