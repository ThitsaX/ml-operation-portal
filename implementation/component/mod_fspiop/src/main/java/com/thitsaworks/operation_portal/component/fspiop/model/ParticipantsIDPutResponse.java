package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.Currency;
import com.thitsaworks.operation_portal.component.fspiop.model.PartyResult;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The object sent in the PUT /participants/{ID} callback.
 **/

@JsonTypeName("ParticipantsIDPutResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-10T18:45:52.776834300+09:00[Asia/Tokyo]")
public class ParticipantsIDPutResponse   {
  private @Valid List<PartyResult> partyList = new ArrayList<>();
  private @Valid Currency currency;

  /**
   * List of PartyResult elements that were either created or failed to be created.
   **/
  public ParticipantsIDPutResponse partyList(List<PartyResult> partyList) {
    this.partyList = partyList;
    return this;
  }

  
  @JsonProperty("partyList")
  @NotNull
 @Size(min=1,max=10000)  public List<PartyResult> getPartyList() {
    return partyList;
  }

  @JsonProperty("partyList")
  public void setPartyList(List<PartyResult> partyList) {
    this.partyList = partyList;
  }

  public ParticipantsIDPutResponse addPartyListItem(PartyResult partyListItem) {
    if (this.partyList == null) {
      this.partyList = new ArrayList<>();
    }

    this.partyList.add(partyListItem);
    return this;
  }

  public ParticipantsIDPutResponse removePartyListItem(PartyResult partyListItem) {
    if (partyListItem != null && this.partyList != null) {
      this.partyList.remove(partyListItem);
    }

    return this;
  }
  /**
   **/
  public ParticipantsIDPutResponse currency(Currency currency) {
    this.currency = currency;
    return this;
  }

  
  @JsonProperty("currency")
  public Currency getCurrency() {
    return currency;
  }

  @JsonProperty("currency")
  public void setCurrency(Currency currency) {
    this.currency = currency;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParticipantsIDPutResponse participantsIDPutResponse = (ParticipantsIDPutResponse) o;
    return Objects.equals(this.partyList, participantsIDPutResponse.partyList) &&
        Objects.equals(this.currency, participantsIDPutResponse.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyList, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParticipantsIDPutResponse {\n");
    
    sb.append("    partyList: ").append(toIndentedString(partyList)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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

