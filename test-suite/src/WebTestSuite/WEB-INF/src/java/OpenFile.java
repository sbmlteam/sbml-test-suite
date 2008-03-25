package sbml.test;


import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class OpenFile extends HttpServlet
{
    private static final int BUFFER_SIZE = 4096;
    private static final String BASE_FILE_PATH = "/path/to/your/files";
     

    protected File findFile(HttpServletRequest request)
       throws IOException
    {
        // This is a reasonable default implementation.
        // Feel free to change it.
        //File file = new File(request.getPathInfo());
	String plot = request.getParameter("plot");
	File file = new File(plot);

        return file;
    }

    protected String getMimeType(HttpServletRequest request, File file)
    {
        // This is a reasonable default implementation.
        // Feel free to change it.
        //ServletContext application = request.getSession().getServletContext();
	//ServletConfig config = getServletConfig();

    	ServletContext application = super.getServletConfig().getServletContext();


        return application.getMimeType(file.getName());
	//return application.getMimeType(file);
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
                catch (IOException ioe) { System.out.println("io exception caught"); }
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
	    //ServletConfig config = getServletConfig();
	    //ServletContext application = config.getServletContext();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(getMimeType(request, file));
	    //response.setContentType(application.getMimeType(file.getName()));
	    //response.setContentType("img/jpg");
            response.setHeader("Content-Type",String.valueOf(file.length()));
            response.setHeader("Content-Disposition","attachment; filename="+ file.getName());

            sendFile(file, response);
        }
    }
}
