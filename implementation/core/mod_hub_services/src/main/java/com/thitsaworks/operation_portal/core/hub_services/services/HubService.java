package com.thitsaworks.operation_portal.core.hub_services.services;

import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipants;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.api.PostUpdateSettlementByParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.PutParticipantStatus;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateSettlement;
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

    @GET("/participants")
    Call<GetParticipants.Response> getParticipants();

    @POST("/v2/settlementWindows/{settlementWindowId}")
    Call<PostCloseSettlementWindows.Response> postCloseSettlementWindows(@Path("settlementWindowId") int settlementWindowId,
                                                                         @Body
                                                                         PostCloseSettlementWindows.Request request);

    @POST("/v2/settlements")
    Call<PostCreateSettlement.Response> postCreateSettlement(@Body PostCreateSettlement.Request request);

    @GET("/v2/settlements/{settlementId}")
    Call<GetSettlement.Response> getSettlement(@Path("settlementId") Integer settlementId);

    @PUT("/v2/settlements/{settlementId}")
    Call<PutUpdateSettlement.Response> putUpdateSettlement(@Path("settlementId") Integer settlementId,
                                                           @Body
                                                           PutUpdateSettlement.Request request);

    @POST("/participants/{dfspCode}/accounts/{accountId}")
    Call<PostUpdateSettlementByParticipant.Response> postUpdateSettlementByParticipant(
            @Path("dfspCode") String dfspCode,
            @Path("accountId") Integer accountId,
            @Body
            PostUpdateSettlementByParticipant.Request request);


}
