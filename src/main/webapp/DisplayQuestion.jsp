<%--

    Copyright 2013 deib-polimi
    Contact: deib-polimi <marco.miglierina@polimi.it>

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

           http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.

--%>

<?xml version="1.0" encoding="UTF-8" ?>

<%@ page language="java" contentType="text/html; charset=UTF-8"

	pageEncoding="UTF-8"%>

<%@ page import="it.polimi.modaclouds.cpimlibrary.entitymng.CloudEntityManager"%>

<%@ page import="it.polimi.modaclouds.cpimlibrary.mffactory.MF"%>

<%@ page import="it.polimi.modaclouds.cloudapp.mic.entity.Topic"%>

<%@ page import="java.util.ArrayList"%>

<%@ page import="java.util.List"%>

<%@ page import="it.polimi.modaclouds.cpimlibrary.exception.ParserConfigurationFileException"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<title>Answer to the questions</title>

<link href="stile.css" rel="stylesheet" type="text/css" />

<link rel="icon" href="image/favicon.png" type="image/png" />

</head>

<body>

	<div id="content">

		<div id="subcontent">

			<%

			@SuppressWarnings("unchecked")

				ArrayList<String> topicList = (ArrayList<String>) request

						.getSession(true).getAttribute("topicList");

				int pointer = (Integer) request.getSession(true).getAttribute(

						"pointer");

				String actualTopic= topicList.get(pointer);

			%>



			<div id="corpo">

				<div id="headerQUESTION">

					<center>

						<h1 id="title">How much do you like <%=actualTopic%></h1>

					</center>

				</div>

				<br />

				<form action="SaveAnswer" method="post" name="answer">

					<div id="questions">

						<table>

						<%

							MF mf=MF.getFactory();

							

							CloudEntityManager em = mf.getEntityManagerFactory()

									.createCloudEntityManager();

							@SuppressWarnings("unchecked")

							List<Topic> listOfTopics = (List<Topic>) em

									.createQuery(

											"SELECT t FROM Topic t WHERE t.topicName=:topicName")

									.setParameter("topicName", actualTopic)

									.getResultList();

							

							int count = 0;

							List<String> questions = null;

							String topicName = null;

							Topic t = listOfTopics.get(0);

							questions = t.getTopicQuestions();

							topicName = t.getTopicName();

						

							for (String question : questions) {

									count++;

						%>

						

						

						<tr>

						<td>  <%=count%>- <%=question%></td>

						<td><select id="selezione" name=<%=topicName + count%>>

									<option value="1" selected="selected">1</option>

									<option value="2">2</option>

									<option value="3">3</option>

									<option value="4">4</option>

									<option value="5">5</option>

								</select></td>

						</tr>

						<%

						

							}

							request.getSession(true).setAttribute("count",count);

							%>

						</table>	

						





					</div>

					<div id="meet">

						<%

							if((topicList.size()-1)==pointer)

							{

						%>

						<input type="image" name="submit" src="image/meet.png" alt="MEET"

							value="" />

							<%

							}else

							{

							%>

						<input type="image" name="submit" src="image/next.png" alt="NEXT"

							value="" />

							<%

							} 

							%>

					</div>

				</form>

			</div>

		</div>

	</div>



</body>

</html>