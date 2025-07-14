package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformation;
import com.thitsaworks.operation_portal.component.fspiop.model.PartyIdInfo;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type PartyResult.
 **/

@JsonTypeName("PartyResult")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-11T18:18:34.338733500+09:00[Asia/Tokyo]")
public class PartyResult   {
  private @Valid PartyIdInfo partyId;
  private @Valid ErrorInformation errorInformation;

  /**
   **/
  public PartyResult partyId(PartyIdInfo partyId) {
    this.partyId = partyId;
    return this;
  }

  
  @JsonProperty("partyId")
  @NotNull
  public PartyIdInfo getPartyId() {
    return partyId;
  }

  @JsonProperty("partyId")
  public void setPartyId(PartyIdInfo partyId) {
    this.partyId = partyId;
  }

  /**
   **/
  public PartyResult errorInformation(ErrorInformation errorInformation) {
    this.errorInformation = errorInformation;
    return this;
  }

  
  @JsonProperty("errorInformation")
  public ErrorInformation getErrorInformation() {
    return errorInformation;
  }

  @JsonProperty("errorInformation")
  public void setErrorInformation(ErrorInformation errorInformation) {
    this.errorInformation = errorInformation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyResult partyResult = (PartyResult) o;
    return Objects.equals(this.partyId, partyResult.partyId) &&
        Objects.equals(this.errorInformation, partyResult.errorInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, errorInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyResult {\n");
    
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    errorInformation: ").append(toIndentedString(errorInformation)).append("\n");
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

