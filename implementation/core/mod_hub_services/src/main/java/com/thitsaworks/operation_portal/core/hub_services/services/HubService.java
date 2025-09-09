package com.thitsaworks.operation_portal.core.hub_services.services;

import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipant;
import com.thitsaworks.operation_portal.core.hub_services.api.GetParticipants;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsByParams;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.hub_services.api.PutParticipantStatus;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateParticipantLimit;
import com.thitsaworks.operation_portal.core.hub_services.api.PutUpdateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface HubService {

    @POST("/participants/{participantId}/accounts/{accountId}")
    Call<PostParticipantBalance.Response> postParticipantBalance(@Path("participantId") String participantId,
                                                                 @Path("accountId") String accountId,
                                                                 @Body PostParticipantBalance.Request request);

    @PUT("/participants/{participantId}/limits")
    Call<PutUpdateParticipantLimit.Response> putUpdateParticipantLimit(@Path("participantId") String participantId,
                                                                       @Body PutUpdateParticipantLimit.Request request);

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

    @GET("/v2/settlementWindows")
    Call<List<GetSettlementWindowsByParams.SettlementWindow>> getSettlementWindows(@Query("fromDateTime") String fromDate,
                                                                                   @Query("toDateTime") String toDate,
                                                                                   @Query("currency") String currency,
                                                                                   @Query("state") String state,
                                                                                   @Query("participantId") Integer participantId);

    @POST("/v2/settlements")
    Call<PostCreateSettlement.Response> postCreateSettlement(@Body PostCreateSettlement.Request request);

    @GET("/v2/settlements/{settlementId}")
    Call<Settlement> getSettlementById(@Path("settlementId") Integer settlementId);


    @GET("/v2/settlements")
    Call<List<Settlement>> getSettlementsByParam(@Query("currency") String currency,
                                                 @Query("participantId") Integer participantId,
                                                 @Query("settlementWindowId") Integer settlementWindowId,
                                                 @Query("accountId") Integer accountId,
                                                 @Query("state") String state,
                                                 @Query("fromDateTime") String fromDateTime,
                                                 @Query("toDateTime") String toDateTime,
                                                 @Query("fromSettlementWindowDateTime") String fromSettlementWindowDateTime,
                                                 @Query("toSettlementWindowDateTime") String toSettlementWindowDateTime);

    @PUT("/v2/settlements/{settlementId}")
    Call<PutUpdateSettlement.Response> putUpdateSettlement(@Path("settlementId") Integer settlementId,
                                                           @Body PutUpdateSettlement.Request request);

}
