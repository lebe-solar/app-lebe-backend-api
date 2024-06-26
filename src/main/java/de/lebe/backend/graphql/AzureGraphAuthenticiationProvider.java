package de.lebe.backend.graphql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.identity.ClientCertificateCredential;
import com.azure.identity.ClientCertificateCredentialBuilder;
import com.microsoft.graph.serviceclient.GraphServiceClient;

@Configuration
public class AzureGraphAuthenticiationProvider {

	@Value("${azure-client-id:b5e16e66-878a-41bb-bb19-f06d5670b3e4}")
	private String clientId;
	
	@Value("${azure-tenant-id:0b0e365f-09be-4291-8f1f-082f5929872d}")
	private String tenantId;
	
	@Value("${graphql-ca}")
	private String certificate;
	
	
	@Value("${spring.cloud.azure.keyvault.secret.endpoint}")
    private String keyVaultUri;
	
	@Bean
	public GraphServiceClient getClient() throws Exception {
		final String[] scopes = new String[] {"https://graph.microsoft.com/.default"};

        String pemCert = certificate;
        InputStream certStream = new ByteArrayInputStream(pemCert.getBytes(StandardCharsets.UTF_8));

		final ClientCertificateCredential credential = new ClientCertificateCredentialBuilder()
			    .clientId(clientId).tenantId(tenantId)
			    .pemCertificate(certStream)
			    .build();
		

		if (null == scopes || null == credential) {
			throw new RuntimeException("Unexpected error");
		}
		
		return new GraphServiceClient(credential, scopes);
		
	}
}
