package sec01.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MemberDAO memberDAO;

	public void init() throws ServletException {
		memberDAO = new MemberDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	private void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action = request.getPathInfo();
		System.out.println("action:" + action);
		if (action == null || action.equals("/listMembers.do")) {
			List<MemberBean> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/listMembers.jsp";
		} else if (action.equals("/addMember.do")) {
			String userId = request.getParameter("userId");
			String userPass = request.getParameter("userPass");
			String userName = request.getParameter("userName");
			String userEmail = request.getParameter("userEmail");
			MemberBean memberVO = new MemberBean(userId, userPass, userName, userEmail);
			memberDAO.addMember(memberVO);
			request.setAttribute("msg", "addMember");
			nextPage = "/member/listMembers.do";

		} else if (action.equals("/memberForm.do")) {
			nextPage = "/memberForm.jsp";

		} else if (action.equals("/modMemberForm.do")) {
			String userId = request.getParameter("userId");
			MemberBean memInfo = memberDAO.findMember(userId);
			request.setAttribute("memInfo", memInfo);
			nextPage = "/modMemberForm.jsp";

		} else if (action.equals("/modMember.do")) {
			String userId = request.getParameter("userId");
			String userPass = request.getParameter("userPass");
			String userName = request.getParameter("userName");
			String userEmail = request.getParameter("userEmail");
			MemberBean memberVO = new MemberBean(userId, userPass, userName, userEmail);
			memberDAO.modMember(memberVO);
			request.setAttribute("msg", "modified");
			nextPage = "/member/listMembers.do";

		} else if (action.equals("/delMember.do")) {
			String userId = request.getParameter("userId");
			memberDAO.delMember(userId);
			request.setAttribute("msg", "deleted");
			nextPage = "/member/listMembers.do";

		} else {
			List<MemberBean> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/listMembers.jsp";
		}
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

}
