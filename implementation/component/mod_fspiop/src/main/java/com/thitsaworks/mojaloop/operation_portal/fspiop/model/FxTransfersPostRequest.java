package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.thitsaworks.mojaloop.operation_portal.fspiop.model.FxTransfersPostRequestSourceAmount;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.FxTransfersPostRequestTargetAmount;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The object sent in the POST /fxTransfers request.
 **/

@JsonTypeName("FxTransfersPostRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-09T22:29:32.007258+06:30[Asia/Rangoon]")
public class FxTransfersPostRequest   {
  private @Valid String commitRequestId;
  private @Valid String determiningTransferId;
  private @Valid String initiatingFsp;
  private @Valid String counterPartyFsp;
  private @Valid FxTransfersPostRequestSourceAmount sourceAmount;
  private @Valid FxTransfersPostRequestTargetAmount targetAmount;
  private @Valid String condition;
  private @Valid String expiration;

  /**
   **/
  public FxTransfersPostRequest commitRequestId(String commitRequestId) {
    this.commitRequestId = commitRequestId;
    return this;
  }

  
  @JsonProperty("commitRequestId")
  @NotNull
  public String getCommitRequestId() {
    return commitRequestId;
  }

  @JsonProperty("commitRequestId")
  public void setCommitRequestId(String commitRequestId) {
    this.commitRequestId = commitRequestId;
  }

  /**
   **/
  public FxTransfersPostRequest determiningTransferId(String determiningTransferId) {
    this.determiningTransferId = determiningTransferId;
    return this;
  }

  
  @JsonProperty("determiningTransferId")
  public String getDeterminingTransferId() {
    return determiningTransferId;
  }

  @JsonProperty("determiningTransferId")
  public void setDeterminingTransferId(String determiningTransferId) {
    this.determiningTransferId = determiningTransferId;
  }

  /**
   **/
  public FxTransfersPostRequest initiatingFsp(String initiatingFsp) {
    this.initiatingFsp = initiatingFsp;
    return this;
  }

  
  @JsonProperty("initiatingFsp")
  @NotNull
  public String getInitiatingFsp() {
    return initiatingFsp;
  }

  @JsonProperty("initiatingFsp")
  public void setInitiatingFsp(String initiatingFsp) {
    this.initiatingFsp = initiatingFsp;
  }

  /**
   **/
  public FxTransfersPostRequest counterPartyFsp(String counterPartyFsp) {
    this.counterPartyFsp = counterPartyFsp;
    return this;
  }

  
  @JsonProperty("counterPartyFsp")
  @NotNull
  public String getCounterPartyFsp() {
    return counterPartyFsp;
  }

  @JsonProperty("counterPartyFsp")
  public void setCounterPartyFsp(String counterPartyFsp) {
    this.counterPartyFsp = counterPartyFsp;
  }

  /**
   **/
  public FxTransfersPostRequest sourceAmount(FxTransfersPostRequestSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
    return this;
  }

  
  @JsonProperty("sourceAmount")
  @NotNull
  public FxTransfersPostRequestSourceAmount getSourceAmount() {
    return sourceAmount;
  }

  @JsonProperty("sourceAmount")
  public void setSourceAmount(FxTransfersPostRequestSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  /**
   **/
  public FxTransfersPostRequest targetAmount(FxTransfersPostRequestTargetAmount targetAmount) {
    this.targetAmount = targetAmount;
    return this;
  }

  
  @JsonProperty("targetAmount")
  @NotNull
  public FxTransfersPostRequestTargetAmount getTargetAmount() {
    return targetAmount;
  }

  @JsonProperty("targetAmount")
  public void setTargetAmount(FxTransfersPostRequestTargetAmount targetAmount) {
    this.targetAmount = targetAmount;
  }

  /**
   **/
  public FxTransfersPostRequest condition(String condition) {
    this.condition = condition;
    return this;
  }

  
  @JsonProperty("condition")
  @NotNull
  public String getCondition() {
    return condition;
  }

  @JsonProperty("condition")
  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   * The API data type DateTime is a JSON String in a lexical format that is restricted by a regular expression for interoperability reasons. The format is according to [ISO 8601](https://www.iso.org/iso-8601-date-and-time-format.html), expressed in a combined date, time and time zone format. A more readable version of the format is yyyy-MM-ddTHH:mm:ss.SSS[-HH:MM]. Examples are \&quot;2016-05-24T08:38:08.699-04:00\&quot;, \&quot;2016-05-24T08:38:08.699Z\&quot; (where Z indicates Zulu time zone, same as UTC).
   **/
  public FxTransfersPostRequest expiration(String expiration) {
    this.expiration = expiration;
    return this;
  }

  
  @JsonProperty("expiration")
 @Pattern(regexp="^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:(\\.\\d{3}))(?:Z|[+-][01]\\d:[0-5]\\d)$")  public String getExpiration() {
    return expiration;
  }

  @JsonProperty("expiration")
  public void setExpiration(String expiration) {
    this.expiration = expiration;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FxTransfersPostRequest fxTransfersPostRequest = (FxTransfersPostRequest) o;
    return Objects.equals(this.commitRequestId, fxTransfersPostRequest.commitRequestId) &&
        Objects.equals(this.determiningTransferId, fxTransfersPostRequest.determiningTransferId) &&
        Objects.equals(this.initiatingFsp, fxTransfersPostRequest.initiatingFsp) &&
        Objects.equals(this.counterPartyFsp, fxTransfersPostRequest.counterPartyFsp) &&
        Objects.equals(this.sourceAmount, fxTransfersPostRequest.sourceAmount) &&
        Objects.equals(this.targetAmount, fxTransfersPostRequest.targetAmount) &&
        Objects.equals(this.condition, fxTransfersPostRequest.condition) &&
        Objects.equals(this.expiration, fxTransfersPostRequest.expiration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commitRequestId, determiningTransferId, initiatingFsp, counterPartyFsp, sourceAmount, targetAmount, condition, expiration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FxTransfersPostRequest {\n");
    
    sb.append("    commitRequestId: ").append(toIndentedString(commitRequestId)).append("\n");
    sb.append("    determiningTransferId: ").append(toIndentedString(determiningTransferId)).append("\n");
    sb.append("    initiatingFsp: ").append(toIndentedString(initiatingFsp)).append("\n");
    sb.append("    counterPartyFsp: ").append(toIndentedString(counterPartyFsp)).append("\n");
    sb.append("    sourceAmount: ").append(toIndentedString(sourceAmount)).append("\n");
    sb.append("    targetAmount: ").append(toIndentedString(targetAmount)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

