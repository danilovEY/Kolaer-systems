package ru.kolaer.server.webportal.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.kolaer.common.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.server.webportal.common.servirces.ExceptionHandlerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	private final ExceptionHandlerService exceptionHandlerService;
	private final ObjectMapper objectMapper;

	public UnauthorizedEntryPoint(ExceptionHandlerService exceptionHandlerService) {
		this.exceptionHandlerService = exceptionHandlerService;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		ServerExceptionMessage serverExceptionMessage = exceptionHandlerService
				.authorization(request);

		objectMapper.writeValue(response.getWriter(), serverExceptionMessage);
	}

}
