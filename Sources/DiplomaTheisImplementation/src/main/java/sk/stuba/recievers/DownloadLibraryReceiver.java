package sk.stuba.recievers;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import terminal.common.receivers.Executable;
import terminal.common.scopes.Scope;

/**
 * @author Juraj Vraniak (xvraniak@stuba.sk)
 */

public class DownloadLibraryReceiver implements Executable {

    @Override
    public void executeCommand(String[] strings, Scope scope) {
        downloadLibrary(strings[strings.length - 1]);
    }

    public void downloadLibrary(String libraryName) {
        try {
            RestTemplate template = new RestTemplate();
            RequestCallback requestCallback = request -> request.getHeaders()
                .setAccept(Arrays.asList(MediaType.ALL));
            ResponseExtractor<Void> responseExtractor = response -> {
                // Here I write the response to a file but do what you like
                Path path = Paths.get("/home/jv/Documents/Skola/Diplomova-Praca/Sources/Commands/Libs");
                Files.copy(response.getBody(), path);
                return null;
            };
            String uriString = "http://localhost:8080/libraries/download/" + libraryName;
            template.execute(URI.create(uriString), HttpMethod.GET, requestCallback, responseExtractor);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }
}
