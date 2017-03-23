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
            response.setHeader("Access-Control-Allow-Origin", "*");// * or origin as u prefer
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
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
