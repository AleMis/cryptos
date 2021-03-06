package crypto.oanda.client;

import crypto.apikeys.ApiKeys;
import crypto.apikeys.ApiKeysRepository;
import crypto.oanda.authentication.*;
import crypto.oanda.domain.positions.OandaPosition;
import crypto.oanda.domain.positions.OandaPositionsList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class OandaPositionsManager {

    private OandaAuthentication authentication;
    private OandaUrlCreator urlCreation;
    private ApiKeysRepository apiKeysRepository;

    public OandaPositionsManager(OandaAuthentication authentication, OandaUrlCreator urlCreation, ApiKeysRepository apiKeysRepository) {
        this.authentication = authentication;
        this.urlCreation = urlCreation;
        this.apiKeysRepository = apiKeysRepository;
    }

    private ApiKeys getApiKeys() {
        return apiKeysRepository.getByExchange("oanda");
    }

    private OandaPositionsList getAllPositions() {
        String accountId = getApiKeys().getClientId();
        String token = getApiKeys().getApiKey();
        OandaUrlParameters urlParameters = new OandaUrlParameters(OandaUrlType.POSITIONS.getUrlType(),accountId);
        OandaHeadersParameters parameters = new OandaHeadersParameters(token,OandaRequestType.STANDARD_REQUEST.getRequestType());
        HttpEntity entity = authentication.createHeaders(parameters);
        String url = urlCreation.createUrl(urlParameters).orElse(new String());
        Optional<OandaPositionsList> positions  = authentication.getResponse(url, entity, OandaPositionsList.class, HttpMethod.GET);
        positions.get().forEach(System.out::println);
        return positions.orElse(new OandaPositionsList());
    }

    private OandaPositionsList getOpenPositions() {
        String accountId = getApiKeys().getClientId();
        String token = getApiKeys().getApiKey();
        OandaUrlParameters urlParameters = new OandaUrlParameters(OandaUrlType.POSITIONS_OPEN.getUrlType(),accountId);
        OandaHeadersParameters parameters = new OandaHeadersParameters(token,OandaRequestType.STANDARD_REQUEST.getRequestType());
        HttpEntity entity = authentication.createHeaders(parameters);
        String url = urlCreation.createUrl(urlParameters).orElse(new String());
        Optional<OandaPositionsList> positions = authentication.getResponse(url, entity, OandaPositionsList.class, HttpMethod.GET);
        positions.get().forEach(System.out::println);
        return positions.orElse(new OandaPositionsList());
    }

    private OandaPosition getPosition(String instrument) {
        String accountId = getApiKeys().getClientId();
        String token = getApiKeys().getApiKey();
        OandaUrlParameters urlParameters = new OandaUrlParameters(OandaUrlType.POSITIONS.getUrlType(),accountId);
        OandaHeadersParameters parameters = new OandaHeadersParameters(token,OandaRequestType.STANDARD_REQUEST.getRequestType());
        HttpEntity entity = authentication.createHeaders(parameters);
        String url = urlCreation.createUrl(urlParameters).orElse(new String());
        Optional<OandaPosition> position = authentication.getResponse(url, entity, OandaPosition.class, HttpMethod.GET);
        System.out.println(position.get());
        return position.orElse(new OandaPosition());
    }

}
