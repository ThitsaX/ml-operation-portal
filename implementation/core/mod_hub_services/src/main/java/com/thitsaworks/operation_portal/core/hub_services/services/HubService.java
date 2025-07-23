package com.thitsaworks.operation_portal.core.hub_services.services;

import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface HubService {

    @POST("/participants/{participantId}/accounts/{accountId}")
    Call<PostParticipantBalance.Response> postParticipantBalance(@Path("participantId") String participantId,
                                                       @Path("accountId") String accountId,
                                                       @Body PostParticipantBalance.Request request);

}
