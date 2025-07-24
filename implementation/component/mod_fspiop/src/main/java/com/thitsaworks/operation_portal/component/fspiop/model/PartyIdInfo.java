package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.PartyIdType;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type PartyIdInfo. An ExtensionList element has been added to this reqeust in version v1.1
 **/

@JsonTypeName("PartyIdInfo")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-24T23:47:57.744005+06:30[Asia/Rangoon]")
public class PartyIdInfo   {
  private @Valid PartyIdType partyIdType;
  private @Valid String partyIdentifier;
  private @Valid String partySubIdOrType;
  private @Valid String fspId;
  private @Valid ExtensionList extensionList;

  /**
   **/
  public PartyIdInfo partyIdType(PartyIdType partyIdType) {
    this.partyIdType = partyIdType;
    return this;
  }

  
  @JsonProperty("partyIdType")
  @NotNull
  public PartyIdType getPartyIdType() {
    return partyIdType;
  }

  @JsonProperty("partyIdType")
  public void setPartyIdType(PartyIdType partyIdType) {
    this.partyIdType = partyIdType;
  }

  /**
   * Identifier of the Party.
   **/
  public PartyIdInfo partyIdentifier(String partyIdentifier) {
    this.partyIdentifier = partyIdentifier;
    return this;
  }

  
  @JsonProperty("partyIdentifier")
  @NotNull
 @Size(min=1,max=128)  public String getPartyIdentifier() {
    return partyIdentifier;
  }

  @JsonProperty("partyIdentifier")
  public void setPartyIdentifier(String partyIdentifier) {
    this.partyIdentifier = partyIdentifier;
  }

  /**
   * Either a sub-identifier of a PartyIdentifier, or a sub-type of the PartyIdType, normally a PersonalIdentifierType.
   **/
  public PartyIdInfo partySubIdOrType(String partySubIdOrType) {
    this.partySubIdOrType = partySubIdOrType;
    return this;
  }

  
  @JsonProperty("partySubIdOrType")
 @Size(min=1,max=128)  public String getPartySubIdOrType() {
    return partySubIdOrType;
  }

  @JsonProperty("partySubIdOrType")
  public void setPartySubIdOrType(String partySubIdOrType) {
    this.partySubIdOrType = partySubIdOrType;
  }

  /**
   * FSP identifier.
   **/
  public PartyIdInfo fspId(String fspId) {
    this.fspId = fspId;
    return this;
  }

  
  @JsonProperty("fspId")
 @Size(min=1,max=32)  public String getFspId() {
    return fspId;
  }

  @JsonProperty("fspId")
  public void setFspId(String fspId) {
    this.fspId = fspId;
  }

  /**
   **/
  public PartyIdInfo extensionList(ExtensionList extensionList) {
    this.extensionList = extensionList;
    return this;
  }

  
  @JsonProperty("extensionList")
  public ExtensionList getExtensionList() {
    return extensionList;
  }

  @JsonProperty("extensionList")
  public void setExtensionList(ExtensionList extensionList) {
    this.extensionList = extensionList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyIdInfo partyIdInfo = (PartyIdInfo) o;
    return Objects.equals(this.partyIdType, partyIdInfo.partyIdType) &&
        Objects.equals(this.partyIdentifier, partyIdInfo.partyIdentifier) &&
        Objects.equals(this.partySubIdOrType, partyIdInfo.partySubIdOrType) &&
        Objects.equals(this.fspId, partyIdInfo.fspId) &&
        Objects.equals(this.extensionList, partyIdInfo.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyIdType, partyIdentifier, partySubIdOrType, fspId, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyIdInfo {\n");
    
    sb.append("    partyIdType: ").append(toIndentedString(partyIdType)).append("\n");
    sb.append("    partyIdentifier: ").append(toIndentedString(partyIdentifier)).append("\n");
    sb.append("    partySubIdOrType: ").append(toIndentedString(partySubIdOrType)).append("\n");
    sb.append("    fspId: ").append(toIndentedString(fspId)).append("\n");
    sb.append("    extensionList: ").append(toIndentedString(extensionList)).append("\n");
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

