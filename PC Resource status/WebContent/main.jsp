<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="com.datastax.driver.core.ResultSet,com.datastax.driver.core.Row,java.util.*,java.util.ArrayList"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="refresh" content="30"/>
<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
	google.charts.load('current', {
		'packages' : [ 'corechart','line' ]
	});
	google.charts.setOnLoadCallback(drawCPUChart);
	google.charts.setOnLoadCallback(drawMemoryChart);
	google.charts.setOnLoadCallback(drawStorageChart);
	
   <%List<List<String>> lst = (List) request.getAttribute("Cdata");%>
   
	function drawCPUChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'Start Time', 'CPU Usage' ],
	<%for(int k=0;k<lst.size();++k)
			{%>
				
				['<%=lst.get(k).get(0)%>',
			
				<%=Double.valueOf(lst.get(k).get(2))%>,
				
					],
				<%}
			//System.out.println("\n\n\n\n\\n\n\n\n\n\n\n\n" + lst);
			%>

		]);
		var options = {
			title : 'CPU Usage Trend',
			 hAxis: {
		          title: 'Time'
		        },
		        vAxis: {
		          title: '% of Usage'
		        },
			curveType : 'function',
			legend : {
				position : 'bottom'
			}
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('CPU chart'));

		chart.draw(data, options);
		
	}
	
	function drawMemoryChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'Start Time', 'Free Memory','Total Memory','Used Memory' ],
	<%for(int k=0;k<lst.size();k++)
			
			{%>
				
				['<%=lst.get(k).get(0)%>',
		
				<%=Double.valueOf(lst.get(k).get(3))%>,
				<%=Double.valueOf(lst.get(k).get(5))%>,
				<%=Double.valueOf(lst.get(k).get(6))%>
					],
				<%
			//System.out.println(lst+"\n\n\n\n\\n"+k+"\n\n\n\n\n\n\n" + lst.get(k));
				}%>

		]);
		var options = {
			title : 'Memory Trend',
			curveType : 'function',
			 hAxis: {
		          title: 'Time'
		        },
		        vAxis: {
		          title: 'CPU Memory (MB)'
		        },
			legend : {
				position : 'bottom'
			}
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('Mem chart'));

		chart.draw(data, options);
		
	}

	
	function drawStorageChart() {
		var data = google.visualization.arrayToDataTable([
				[ 'Start Time', 'Total Storage','Free Storage' ],
	<%for(int k=0;k<lst.size();k++)
			
			{%>
				
				['<%=lst.get(k).get(0)%>',
				<%=Double.valueOf(lst.get(k).get(1))%>,
				<%=Double.valueOf(lst.get(k).get(4))%>
					],
				<%
			//System.out.println(lst+"\n\n\n\n\\n"+k+"\n\n\n\n\n\n\n" + lst.get(k));
				}%>

		]);
		var options = {
			title : ' Stats',
			curveType : 'function',
			 hAxis: {
		          title: 'Time'
		        },
		        vAxis: {
		          title: 'Storage (GB)'
		        },
			legend : {
				position : 'bottom'
			}
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('space chart'));

		chart.draw(data, options);
		
	}
	
	function drawChart1() {
		var data = google.visualization.arrayToDataTable([
				[ 'Start Time', 'Total Storage','CPU Usage','Free Memory','Free Storage','Total Memory','Used Memory' ],
	<%for(int k=0;k<lst.size();k++)
			
			{%>
				
				['<%=lst.get(k).get(0)%>',
				<%=Double.valueOf(lst.get(k).get(1))%>,
				<%=Double.valueOf(lst.get(k).get(2))%>,
				<%=Double.valueOf(lst.get(k).get(3))/1000%>,
				<%=Double.valueOf(lst.get(k).get(4))%>,
				<%=Double.valueOf(lst.get(k).get(5))/1000%>,
				<%=Double.valueOf(lst.get(k).get(6))/1000%>
					],
				<%
			//System.out.println(lst+"\n\n\n\n\\n"+k+"\n\n\n\n\n\n\n" + lst.get(k));
				}%>

		]);
		var options = {
			title : ' Stats',
			curveType : 'function',
			legend : {
				position : 'top'
			}
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('chart'));

		chart.draw(data, options);
		
	}
	
	
	
</script>

<style>
table, th, td {
	border: 1px solid black;
}
</style>

</head>
<body>


	<%
		List r = (List) request.getAttribute("data");
		
	%>


	<table>
		<caption>CPU Usage Stats</caption>
		<tr>
			<th>Total Storage(GB)</th>
			<th>Start Time</th>
			<th>End time</th>
			<th>CPU Usage %</th>
			<th>Free Primary Memory(MB)</th>
			<th>Free Storage(GB)</th>
			<th>Total Primary Memory (MB)</th>
			<th>Used Primary Memory(MB)</th>

		</tr>
		<%
			int n = Integer.parseInt(request.getParameter("Option"));
			for (int i = 0; i < 8 * n;) {
		%>

		<tr>
			<%
				for (int j = 0; j < 8; j++) {
			%>
			<td><%=r.get(i++)%></td>
			<%
				}
			%>
		</tr>

		<%
			}
		%>

	</table>


	<form action="SystemStatus" method="get">
		<select name="Option">

			<option value="1">15 min</option>
			<option value="2">30 min</option>
			<option value="4">1 hr</option>
			<option value="12">3 hr</option>

		</select> <input type="submit" value="Reload">
	</form>
<div name= "space chart" id="space chart"></div>
<div name= "space chart" id="Mem chart"></div>
<div name= "space chart" id="CPU chart"></div>
	
</body>
</html>
