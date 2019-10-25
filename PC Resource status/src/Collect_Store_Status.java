import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.*;
import java.sql.*;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class Collect_Store_Status implements Runnable {
	int mb = 1000 * 1000;
	double d = 0;
	static double cl, fm, um, tm, fs, ts;
	double cpuload = 0,freememory = 0,usedmemory = 0,totalmemory = 0,freespace = 0,totalspace = 0;
	

	OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();


	static int it = 0;

	@Override
	public void run() {
		while(true){

		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {

			method.setAccessible(true);
			if (method.getName().startsWith("getSystemCpuLoad") || method.getName().startsWith("getFreePhysicalMemorySize")
					|| method.getName().startsWith("getTotalPhysicalMemorySize")) {
				Object value;

				try {

					value = method.getName().startsWith("getSystem") ? (double) method.invoke(operatingSystemMXBean) * 100 : (long) method
							.invoke(operatingSystemMXBean) / mb;
				}
				catch (Exception e) {
					value = e;
				}
				if (method.getName().startsWith("getSys")) {
					System.out.println("->>>" + method.getName() + " = " + (double) value + "%\n");
					cpuload = (double) value;
				}

				else {
					if (d == 0) {
						d = ((double) value);
						System.out.println("->>>" + method.getName() + " = " + value + " MB\n");
						freememory = (double) value;
					}
					else {

						System.out.println("->>>" + method.getName() + " = " + value + " MB\n");
						totalmemory = (double) value;
						usedmemory =  totalmemory -d;
						d=0;
					}

				}
			}
		}
			System.out.println("->>>Used Physical MemorySize = " + d + " MB\n");

			/* Get a list of all filesystem roots on this system */
			File[] roots = File.listRoots();


			for (File root : roots) {
				System.out.println("File system root: " + root.getAbsolutePath());
				System.out.println("Total space (GB): " + (float) root.getTotalSpace() / (mb * 1000));
				totalspace = (float) root.getTotalSpace() / (mb * 1000);
				System.out.println("Free space (GB):  " + (float) root.getFreeSpace() / (mb * 1000));
				freespace = (float) root.getFreeSpace() / (mb * 1000);
				System.out.println("Usable space (GB): " + (float) root.getUsableSpace() / (mb * 1000));

			}
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/INC_DB", "sai", "123");
				Statement stmt = con.createStatement();

				String sql = "insert into osstats values(" + cpuload + "," + freememory + "," + totalmemory + "," + usedmemory + "," + freespace
						+ "," + totalspace + ",NULL);";
				stmt.executeUpdate(sql);

				con.close();
				stmt.close();
				
				cl+=cpuload;
				fm+=freememory;
				tm+=totalmemory;
				um+=usedmemory;
				fs+=freespace;
				ts+=totalspace;
				Thread.sleep(60000);
			}

			catch (Exception e) {
				System.out.println(e);
			}
			
			if (it == 0) {
				try {
					cl=cpuload;
					fm=freememory;
					tm=totalmemory;
					um=usedmemory;
					fs=freespace;
					ts=totalspace;
					
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/INC_DB", "sai", "123");
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("Select max(timer) from osstats;");
					if (rs.next()) starttime = rs.getString(1)+"+0530";
					else
						System.out.println("unable to fetch time"+rs.getString(1));

				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			it++;
			if (it == 15) {
				try {
					cl/=15;
					fm/=15;
					tm/=15;
					um/=15;
					fs/=15;
					ts/=15;
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/INC_DB", "sai", "123");
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("Select max(timer) from osstats;");
					if (rs.next()) endtime = rs.getString(1)+"+0530";
					insertintocas();
					it=0;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			
		}
		}

	}

	private static String starttime;
	private  String endtime;

	private void insertintocas() {
		String query = "insert into agtstats (starttime , endtime , cpuusage , freememory , totalmemory , usedmemory , freestorage , totalstorage ) VALUES ('"
				+ starttime + "','" + endtime + "'," + cl + "," + fm + "," + tm + "," + um + "," + fs + "," + ts + ");";
		System.out.println(query);

		Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
		Session session = cluster.connect("cpustats");
		session.execute(query);
		session.close();
		cluster.close();

	}

	public static void main(String[] args) throws Exception {
		
		
			Thread st = new Thread(new Collect_Store_Status());			
			st.start();
			st.join();
			
		

		

	}
}