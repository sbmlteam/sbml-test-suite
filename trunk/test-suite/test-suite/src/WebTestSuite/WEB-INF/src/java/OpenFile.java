//
//  OpenFile.java
//  
//
//  Created by Kimberly Begley 
//  Servlet to open a jpeg (a plot) and display it in the requesting jsp page (testdetails.jsp).
//


package sbml.test;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class OpenFile extends HttpServlet
{
    private static final int BUFFER_SIZE = 4096;
    
    protected File findFile(HttpServletRequest request)
       throws IOException
    {
        
	String plot = request.getParameter("plot");
	File file = new File(plot);

        return file;
    }

    protected String getMimeType(HttpServletRequest request, File file)
    {
    	ServletContext application = super.getServletConfig().getServletContext();

        return application.getMimeType(file.getName());
    } 

    protected void sendFile(File file, HttpServletResponse response)
        throws IOException
    {
       BufferedInputStream in = null;

        try {
           int count;
            byte[] buffer = new byte[BUFFER_SIZE];

            in = new BufferedInputStream(new FileInputStream(file));

            ServletOutputStream out = response.getOutputStream();

            while(-1 != (count = in.read(buffer)))
                out.write(buffer, 0, count);

            out.flush();
        }
        finally
        {
            if (in != null)
            {
                try { in.close(); }
                catch (IOException ioe) { System.err.println("IO exception caught"); }
            }
        }
    }

    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws ServletException, IOException
    {
        File file = findFile(request);

        if(null == file || !file.exists())
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        else if(!file.canRead())
        {
            // Feel free to send NOT_FOUND instead, if you don't want to
            // give up potentially sensitive security information.
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        else
        {
	  
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(getMimeType(request, file));
            response.setHeader("Content-Type",String.valueOf(file.length()));
            response.setHeader("Content-Disposition","attachment; filename="+ file.getName());

            sendFile(file, response);
        }
    }
}
