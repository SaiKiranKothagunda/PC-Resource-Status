
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;


@WebServlet("/SystemStatus")
public class SystemStatus extends HttpServlet {


ArrayList<String> sb = new ArrayList<>();


List<List<String>> hm=new ArrayList<>();

List<String> lst =new ArrayList<>();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		sb.clear();
		String n = request.getParameter("Option");

		Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
		Session session = cluster.connect("cpustats");
		String query = "select * from agtstats LIMIT " + n + ";";
		ResultSet rs = session.execute(query);
		hm.clear();
		
		for (Row r : rs) {
			sb.add(""+r.getDouble("totalstorage"));
			sb.add(""+r.getTimestamp(1));
			sb.add(""+r.getTimestamp(2));
			sb.add(""+r.getDouble("cpuusage"));
			sb.add(""+r.getDouble("freememory"));
			sb.add(""+r.getDouble("freestorage"));
			sb.add(""+r.getDouble("totalmemory"));
			sb.add(""+r.getDouble("usedmemory"));
			
			List<String> lst =new ArrayList<>();
			lst.add(""+r.getTimestamp(1));
			lst.add(""+r.getDouble("totalstorage"));
			lst.add(""+r.getDouble("cpuusage"));
			lst.add(""+r.getDouble("freememory"));
			lst.add(""+r.getDouble("freestorage"));
			lst.add(""+r.getDouble("totalmemory"));
			lst.add(""+r.getDouble("usedmemory"));
			hm.add(lst);
			
		
			//System.out.println("\n\n\n" + r);
		}

		request.setAttribute("Cdata",hm);
		System.out.println(sb+"\n"+hm);
		request.setAttribute("data",sb);
		session.close();
		cluster.close();
		RequestDispatcher dispatcher = request.getRequestDispatcher("main.jsp");

		if (dispatcher != null)
			dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
