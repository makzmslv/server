package backend.server.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CorsFilter extends OncePerRequestFilter
{
    static final String ORIGIN = "Origin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        System.out.println(request.getHeader(ORIGIN));
        System.out.println(request.getMethod());
        if (request.getHeader(ORIGIN).equals("null"))
        {
            String origin = request.getHeader(ORIGIN);

            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
            response.setHeader("Access-Control-Allow-Headers",
                    "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        }
        if (request.getMethod().equals("OPTIONS"))
        {
            try
            {
                response.getWriter().print("OK");
                response.getWriter().flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            filterChain.doFilter(request, response);
        }
    }
}
