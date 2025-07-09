package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.thitsaworks.mojaloop.operation_portal.fspiop.model.ExtensionList;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.Money;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type IndividualTransfer.
 **/

@JsonTypeName("IndividualTransfer")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-09T22:29:32.007258+06:30[Asia/Rangoon]")
public class IndividualTransfer   {
  private @Valid String transferId;
  private @Valid Money transferAmount;
  private @Valid String ilpPacket;
  private @Valid String condition;
  private @Valid ExtensionList extensionList;

  /**
   * Identifier that correlates all messages of the same sequence. The API data type UUID (Universally Unique Identifier) is a JSON String in canonical format, conforming to [RFC 4122](https://tools.ietf.org/html/rfc4122), that is restricted by a regular expression for interoperability reasons. A UUID is always 36 characters long, 32 hexadecimal symbols and 4 dashes (‘-‘).
   **/
  public IndividualTransfer transferId(String transferId) {
    this.transferId = transferId;
    return this;
  }

  
  @JsonProperty("transferId")
  @NotNull
 @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")  public String getTransferId() {
    return transferId;
  }

  @JsonProperty("transferId")
  public void setTransferId(String transferId) {
    this.transferId = transferId;
  }

  /**
   **/
  public IndividualTransfer transferAmount(Money transferAmount) {
    this.transferAmount = transferAmount;
    return this;
  }

  
  @JsonProperty("transferAmount")
  @NotNull
  public Money getTransferAmount() {
    return transferAmount;
  }

  @JsonProperty("transferAmount")
  public void setTransferAmount(Money transferAmount) {
    this.transferAmount = transferAmount;
  }

  /**
   * Information for recipient (transport layer information).
   **/
  public IndividualTransfer ilpPacket(String ilpPacket) {
    this.ilpPacket = ilpPacket;
    return this;
  }

  
  @JsonProperty("ilpPacket")
  @NotNull
 @Pattern(regexp="^[A-Za-z0-9-_]+[=]{0,2}$") @Size(min=1,max=32768)  public String getIlpPacket() {
    return ilpPacket;
  }

  @JsonProperty("ilpPacket")
  public void setIlpPacket(String ilpPacket) {
    this.ilpPacket = ilpPacket;
  }

  /**
   * Condition that must be attached to the transfer by the Payer.
   **/
  public IndividualTransfer condition(String condition) {
    this.condition = condition;
    return this;
  }

  
  @JsonProperty("condition")
  @NotNull
 @Pattern(regexp="^[A-Za-z0-9-_]{43}$") @Size(max=48)  public String getCondition() {
    return condition;
  }

  @JsonProperty("condition")
  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   **/
  public IndividualTransfer extensionList(ExtensionList extensionList) {
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
    IndividualTransfer individualTransfer = (IndividualTransfer) o;
    return Objects.equals(this.transferId, individualTransfer.transferId) &&
        Objects.equals(this.transferAmount, individualTransfer.transferAmount) &&
        Objects.equals(this.ilpPacket, individualTransfer.ilpPacket) &&
        Objects.equals(this.condition, individualTransfer.condition) &&
        Objects.equals(this.extensionList, individualTransfer.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transferId, transferAmount, ilpPacket, condition, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualTransfer {\n");
    
    sb.append("    transferId: ").append(toIndentedString(transferId)).append("\n");
    sb.append("    transferAmount: ").append(toIndentedString(transferAmount)).append("\n");
    sb.append("    ilpPacket: ").append(toIndentedString(ilpPacket)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
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

