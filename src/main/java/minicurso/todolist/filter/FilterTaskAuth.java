package minicurso.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import minicurso.todolist.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component // classe generica de gerenciamento do spring, tem que botar senão ele não entende que tem que passar por ele
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/")) {
            // Pegar a autenticação (user e senha)
            var authorization = request.getHeader("Authorization");

            var userPasswordEncoded = authorization.substring("Basic".length()).trim();

            byte[] userPasswordDecoded = Base64.getDecoder().decode(userPasswordEncoded);

            var userPasswordString = new String(userPasswordDecoded);
            String[] credentials = userPasswordString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401);
            } else {
                var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerified.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
