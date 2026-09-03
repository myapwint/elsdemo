package com.es.elsdemo.standard;

import com.es.elsdemo.graphql.Request;
import com.es.elsdemo.graphql.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class StandardHandler {

    private final Properties genericGraphQlProperties;
    private final ApplicationContext context;
    private final ObjectMapper mapper = new ObjectMapper();

    public Response fetch(Request request) {
        return handle(request, true);
    }

    public Response save(Request request) {
        return handle(request, false);
    }

    private Response handle(Request request, boolean fetch) {
        try {
            String api = request.getApiName();
            String serviceName = genericGraphQlProperties.getProperty(api + ".serviceName");

            StandardApiService service =
                    (StandardApiService) context.getBean(serviceName);

            Class<?> dtoClass =
                    Class.forName(genericGraphQlProperties.getProperty(api + ".requestDTO"));

            Object dto = mapper.readValue(request.getPayload(), dtoClass);

            StandardRequestContainer container = new StandardRequestContainer();
            container.setApiName(api);
            container.setPayload((StandardRequestDTO<?>) dto);

            StandardResponseContainer response =
                    fetch ? service.fetch(container) : service.save(container);

            return Response.success(mapper.writeValueAsString(response.getResult()));
        } catch (Exception e) {
            return Response.error(e.getMessage());
        }
    }
}
