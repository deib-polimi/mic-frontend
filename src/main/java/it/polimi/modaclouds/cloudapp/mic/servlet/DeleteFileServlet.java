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



import java.io.IOException;



import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



public class DeleteFileServlet extends HttpServlet {



	private static final long serialVersionUID = 1L;



	public void doGet(HttpServletRequest req, HttpServletResponse resp)

			throws ServletException, IOException {

		MF mf=MF.getFactory();

		

		String fileName=req.getParameter("fileName");

        mf.getBlobManagerFactory().createCloudBlobManager().deleteBlob(fileName);

        RequestDispatcher disp;

		disp = req.getRequestDispatcher("Download.jsp");

		disp.forward(req, resp);

	    }

	



	public void doPost(HttpServletRequest req, HttpServletResponse resp)

			throws ServletException, IOException {



	}





}

