<%-- 
 * @file    process-selections.jsp
 * @brief   JSP file to process user's test results.
 * @author  Michael Hucka
 * @author  Kimberly Begley
 * @date    Created Feb 27, 2008, 9:25:21 AM
 *
 * ----------------------------------------------------------------------------
 * This file is part of the SBML Test Suite.  Please visit http://sbml.org for
 * more information about SBML, and the latest version of the SBML Test Suite.
 *
 * Copyright (C) 2010-2015 jointly by the following organizations: 
 *     1. California Institute of Technology, Pasadena, CA, USA
 *     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
 *     3. University of Heidelberg, Heidelberg, Germany
 *
 * Copyright (C) 2008-2009 California Institute of Technology (USA).
 *
 * Copyright (C) 2004-2007 jointly by the following organizations:
 *     1. California Institute of Technology (USA) and
 *     2. University of Hertfordshire (UK).
 * 
 * The SBML Test Suite is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation.  A copy of the license
 * agreement is provided in the file named "LICENSE.txt" included with
 * this software distribution and also available on the Web at
 * http://sbml.org/Software/SBML_Test_Suite/License
 * ----------------------------------------------------------------------------
 *
 * This file handles interpreting the user's selections from selecttests.jsp
 * and building a zip file of test cases to return to the user.
 *
--%>

<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="sbml.test.*" %>

<%@ page errorPage="/web/error.jsp" %>

<%
// 1. Start by logging that we've been invoked.

OnlineSTS.init();
OnlineSTS.logInvocation(request);

//
// 2. Get the user's selection from the previous screen.
//

// For debugging:
// for (Enumeration e = request.getParameterNames(); e.hasMoreElements(); )
//     OnlineSTS.logInfo((String) e.nextElement());

String[] ctags         = request.getParameterValues("ctags");
String[] ttags         = request.getParameterValues("ttags");
String[] packages      = request.getParameterValues("packages");
String levelAndVersion = request.getParameter("levelAndVersion");

//
// 3. Build up a list of case files that we will zip up and return.
//

ServletContext context = getServletContext();
File casesRootDir      = new File(context.getRealPath("/test-cases"));
CaseSummaryMap cases   = new CaseSummaryMap(casesRootDir); 
 
// For debugging:
//
// OnlineSTS.logInfo(request, "Retaining cases with level+version " + levelAndVersion);
// cases.retainIfHasLevelAndVersion(levelAndVersion); 
//
// if (ctags != null)
// {
//     OnlineSTS.logInfo(request, "Removing cases with ctags: ");
//     for (int i = 0; i < ctags.length; i++)
//         OnlineSTS.logInfo(request, ctags[i]);
// }
//
// if (ttags != null)
// {
//     OnlineSTS.logInfo(request, "Removing cases with ttags: ");
//     for (int i = 0; i < ttags.length; i++)
//         OnlineSTS.logInfo(request, ttags[i]);
// }

cases.retainIfHasLevelAndVersion(levelAndVersion);
cases.removeIfInvolvesPackagesOtherThan(packages);
cases.removeIfTagged(ctags); 
cases.removeIfTagged(ttags); 
 
OnlineSTS.logInfo(request, "Selection yielded " + cases.size()
                  + " cases " + "of level/version " + levelAndVersion);

String pkgsRequested = new String();
for (String pkg : packages)
    pkgsRequested += pkg + " ";

OnlineSTS.logInfo(request, "Packages included: " + pkgsRequested);

if (cases.size() < 1)
    throw new JspException("STS has no cases to put into archive");

// For debugging:
//
// for (Map.Entry<Integer, CaseSummary> e : cases.entrySet())
//     OnlineSTS.logInfo(request, "case " + e.getKey() + ": " + e.getValue().getCasePackages());

//
// 5. Call our zip file builder with the results and some additional param.
//

session.putValue("casesRootDir"   , casesRootDir);
session.putValue("casesToReturn"  , cases.getAllCaseNames());
session.putValue("levelAndVersion", levelAndVersion);

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");       // HTTP 1.0
response.setDateHeader("Expires", 0);           // Prevent caching by proxy server

response.sendRedirect(OnlineSTS.getServiceRootURL(request) + "/servlet/ZipServlet");
%>
