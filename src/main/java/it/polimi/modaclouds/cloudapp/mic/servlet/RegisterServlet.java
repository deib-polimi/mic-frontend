/**
 * Copyright 2013 deib-polimi
 * Contact: deib-polimi <marco.miglierina@polimi.it>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.polimi.modaclouds.cloudapp.mic.servlet;

import it.polimi.modaclouds.cpimlibrary.mffactory.MF;
import it.polimi.tower4clouds.java_app_dc.Monitor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Servlet implementation class LoginServlet
 */
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 5909797442154638761L;
	private static final Logger logger = LoggerFactory.getLogger(RegisterServlet.class);

	/**
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
	}

	/**
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	@Monitor(type = "register")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		parseReq(request, response);
	}

	private void parseReq(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			MF mf = MF.getFactory();

			req.setCharacterEncoding("UTF-8");

			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iterator = upload.getItemIterator(req);

			HashMap<String, String> map = new HashMap<String, String>();

			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					String field = item.getFieldName();
					String value = Streams.asString(stream);
					map.put(field, value);
					stream.close();
				} else {
					String filename = item.getName();
					String[] extension = filename.split("\\.");
					String mail = map.get("mail");

					if (mail != null)
						filename = mail + "_"
								+ String.valueOf(filename.hashCode()) + "."
								+ extension[extension.length - 1];
					else
						filename = String.valueOf(filename.hashCode()) + "."
								+ extension[extension.length - 1];

					map.put("filename", filename);

					byte[] buffer = IOUtils.toByteArray(stream);

					mf.getBlobManagerFactory().createCloudBlobManager()
							.uploadBlob(buffer, filename);

					stream.close();
				}
			}

			String email = map.get("mail");
			String firstName = map.get("firstName");
			String lastName = map.get("lastName");
			String dayS = map.get("day");
			String monthS = map.get("month");
			String yearS = map.get("year");
			String password = map.get("password");
			String filename = map.get("filename");
			String date = yearS + "-" + monthS + "-" + dayS;
			char gender = map.get("gender").charAt(0);

			Connection c = mf.getSQLService().getConnection();
			Statement statement = c.createStatement();

			String stm = "INSERT INTO UserProfile VALUES('" + email + "', '"
					+ password + "', '" + firstName + "', '" + lastName
					+ "', '" + date + "', '" + gender + "', '" + filename
					+ "')";

			statement.executeUpdate(stm);
			statement.close();
			c.close();

			req.getSession(true).setAttribute("actualUser", email);
			req.getSession(true).setAttribute("edit", "false");
			RequestDispatcher disp = req
					.getRequestDispatcher("SelectTopic.jsp");
			disp.forward(req, response);
		} catch (Exception e) {
			logger.error("Error while parsing the request.", e);
		}
	}

}