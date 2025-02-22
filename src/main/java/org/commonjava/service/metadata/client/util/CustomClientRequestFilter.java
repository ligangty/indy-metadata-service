package org.commonjava.service.metadata.client.util;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class CustomClientRequestFilter implements ClientRequestFilter
{
    private final Logger logger = LoggerFactory.getLogger( getClass() );

    @Inject
    OidcClient client;

    private volatile Tokens tokens;

    @ConfigProperty(name = "indy_security.enabled")
    boolean securityEnabled;

    @Override
    public void filter( ClientRequestContext requestContext )
    {
        if ( securityEnabled )
        {
            if ( tokens == null || tokens.isAccessTokenExpired() )
            {
                logger.debug("Security enabled, get oidc Tokens");
                tokens = client.getTokens().await().indefinitely();
            }
            requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken());
        }
    }
}
