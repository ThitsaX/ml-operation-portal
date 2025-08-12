package com.thitsaworks.operation_portal.core.hub_services.services;

import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.api.PutParticipantStatus;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface HubService {

    @POST("/participants/{participantId}/accounts/{accountId}")
    Call<PostParticipantBalance.Response> postParticipantBalance(@Path("participantId") String participantId,
                                                                 @Path("accountId") String accountId,
                                                                 @Body PostParticipantBalance.Request request);

    @PUT("/participants/{participantId}/accounts/{accountId}")
    Call<PutParticipantStatus.Response> putParticipantStatus(@Path("participantId") String participantId,
                                                             @Path("accountId") int accountId,
                                                             @Body ParticipantHubClient.RequestToHub request);

    @GET("/participants/{participantId}")
    Call<GetParticipant.Response> getParticipant(@Path("participantId") String participantId

                                                );

    @POST("/v2/settlementWindows/{settlementWindowId}")
    Call<PostCloseSettlementWindows.Response> postCloseSettlementWindows(@Path("settlementWindowId") int settlementWindowId,
                                                                         @Body
                                                                         PostCloseSettlementWindows.Request request);

    @POST("/v2/settlements")
    Call<PostCreateSettlement.Response> postCreateSettlement(@Body PostCreateSettlement.Request request);

}
