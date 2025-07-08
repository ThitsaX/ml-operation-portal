package com.thitsaworks.operation_portal.core.hub_services.services;

import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface HubService {

    @POST
    Call<PostParticipantBalance.Response> postParticipantBalance(@Url String endpoint,
                                                                 @Body String request);

}
