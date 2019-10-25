<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
Status obj=new Status();
function f1(){
	obj.Pr
}
function f2(){
	;
}
</script>
</head>
<body>
<form action="Status.Process" method ="POST">
  <select name="Option">
  <option value="start">Start</option>
  <option value="stop">Stop</option>
 

</select>
<input type="submit" value="Perform Capturing"/>
</form>
<button onclick="Status.Process"> Button</button>

<form action = "CPUStats" method = "GET">
  <select name="Option">
  <option value="1">15 min</option>
  <option value="2">30 min</option>
  <option value="4">1 hr</option>
  <option value="12">3 hr</option>

</select> 
         <input type = "submit" value = "Submit"  />
      </form>

</body>
</html>