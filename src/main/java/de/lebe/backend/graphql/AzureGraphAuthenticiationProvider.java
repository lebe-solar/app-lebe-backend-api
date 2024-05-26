package de.lebe.backend.graphql;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.azure.identity.ClientCertificateCredential;
import com.azure.identity.ClientCertificateCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;

@Configuration
public class AzureGraphAuthenticiationProvider {

	final String clientId = "b5e16e66-878a-41bb-bb19-f06d5670b3e4";
	final String tenantId = "0b0e365f-09be-4291-8f1f-082f5929872d";

	@Bean
	public GraphServiceClient getClient() throws FileNotFoundException {

		final String[] scopes = new String[] {"https://graph.microsoft.com/.default"};

		
		File file = ResourceUtils.getFile("classpath:lebe.pem");
		
		final ClientCertificateCredential credential = new ClientCertificateCredentialBuilder()
			    .clientId(clientId).tenantId(tenantId).pemCertificate(file.getAbsolutePath())
			    .build();
		

		if (null == scopes || null == credential) {
			throw new RuntimeException("Unexpected error");
		}
		
		return new GraphServiceClient(credential, scopes);
		
	}
}
