package com.example.gui.ui;

import com.vaadin.server.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Bartek on 2017-01-22.
 */
@Controller
@Configuration
public class GlobalResourceHandler implements RequestHandler {

    private static final long serialVersionUID = 1L;
    private static final String PATH = UUID.randomUUID().toString();
    private static final Map<String, String> mapResourceIdMIMEType = new HashMap<>();
    private static final Map<String, byte[]> mapResourceIdBytes = new HashMap<>();

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response)
            throws IOException {
        if (request.getPathInfo().startsWith("/" + PATH + "/")) {
            String resourceId = request.getPathInfo().substring(PATH.length() + 2);
            byte[] bytes = mapResourceIdBytes.get(resourceId);
            if (bytes == null)
                return false;
            response.setContentType(mapResourceIdMIMEType.get(resourceId));
            response.getOutputStream().write(bytes);
            return true;
        }
        return false;
    }

    public static Resource createResource(byte[] bytes, String MIMEType) {
        String resourceId = UUID.randomUUID().toString();
        mapResourceIdMIMEType.put(resourceId, MIMEType);
        mapResourceIdBytes.put(resourceId, bytes);
        return new ExternalResource(PATH + "/" + resourceId);
    }

}
