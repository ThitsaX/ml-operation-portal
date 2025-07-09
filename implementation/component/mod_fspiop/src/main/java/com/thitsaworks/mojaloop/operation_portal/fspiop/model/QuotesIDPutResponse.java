package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.thitsaworks.mojaloop.operation_portal.fspiop.model.ExtensionList;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.GeoCode;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.Money;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The object sent in the PUT /quotes/{ID} callback.
 **/

@JsonTypeName("QuotesIDPutResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-08T18:26:12.180501+06:30[Asia/Rangoon]")
public class QuotesIDPutResponse   {
  private @Valid Money transferAmount;
  private @Valid Money payeeReceiveAmount;
  private @Valid Money payeeFspFee;
  private @Valid Money payeeFspCommission;
  private @Valid String expiration;
  private @Valid GeoCode geoCode;
  private @Valid String ilpPacket;
  private @Valid String condition;
  private @Valid ExtensionList extensionList;

  /**
   **/
  public QuotesIDPutResponse transferAmount(Money transferAmount) {
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
   **/
  public QuotesIDPutResponse payeeReceiveAmount(Money payeeReceiveAmount) {
    this.payeeReceiveAmount = payeeReceiveAmount;
    return this;
  }

  
  @JsonProperty("payeeReceiveAmount")
  public Money getPayeeReceiveAmount() {
    return payeeReceiveAmount;
  }

  @JsonProperty("payeeReceiveAmount")
  public void setPayeeReceiveAmount(Money payeeReceiveAmount) {
    this.payeeReceiveAmount = payeeReceiveAmount;
  }

  /**
   **/
  public QuotesIDPutResponse payeeFspFee(Money payeeFspFee) {
    this.payeeFspFee = payeeFspFee;
    return this;
  }

  
  @JsonProperty("payeeFspFee")
  public Money getPayeeFspFee() {
    return payeeFspFee;
  }

  @JsonProperty("payeeFspFee")
  public void setPayeeFspFee(Money payeeFspFee) {
    this.payeeFspFee = payeeFspFee;
  }

  /**
   **/
  public QuotesIDPutResponse payeeFspCommission(Money payeeFspCommission) {
    this.payeeFspCommission = payeeFspCommission;
    return this;
  }

  
  @JsonProperty("payeeFspCommission")
  public Money getPayeeFspCommission() {
    return payeeFspCommission;
  }

  @JsonProperty("payeeFspCommission")
  public void setPayeeFspCommission(Money payeeFspCommission) {
    this.payeeFspCommission = payeeFspCommission;
  }

  /**
   * The API data type DateTime is a JSON String in a lexical format that is restricted by a regular expression for interoperability reasons. The format is according to [ISO 8601](https://www.iso.org/iso-8601-date-and-time-format.html), expressed in a combined date, time and time zone format. A more readable version of the format is yyyy-MM-ddTHH:mm:ss.SSS[-HH:MM]. Examples are \&quot;2016-05-24T08:38:08.699-04:00\&quot;, \&quot;2016-05-24T08:38:08.699Z\&quot; (where Z indicates Zulu time zone, same as UTC).
   **/
  public QuotesIDPutResponse expiration(String expiration) {
    this.expiration = expiration;
    return this;
  }

  
  @JsonProperty("expiration")
  @NotNull
 @Pattern(regexp="^(?:[1-9]\\d{3}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1\\d|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[1-9]\\d(?:0[48]|[2468][048]|[13579][26])|(?:[2468][048]|[13579][26])00)-02-29)T(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d(?:(\\.\\d{3}))(?:Z|[+-][01]\\d:[0-5]\\d)$")  public String getExpiration() {
    return expiration;
  }

  @JsonProperty("expiration")
  public void setExpiration(String expiration) {
    this.expiration = expiration;
  }

  /**
   **/
  public QuotesIDPutResponse geoCode(GeoCode geoCode) {
    this.geoCode = geoCode;
    return this;
  }

  
  @JsonProperty("geoCode")
  public GeoCode getGeoCode() {
    return geoCode;
  }

  @JsonProperty("geoCode")
  public void setGeoCode(GeoCode geoCode) {
    this.geoCode = geoCode;
  }

  /**
   * Information for recipient (transport layer information).
   **/
  public QuotesIDPutResponse ilpPacket(String ilpPacket) {
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
  public QuotesIDPutResponse condition(String condition) {
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
  public QuotesIDPutResponse extensionList(ExtensionList extensionList) {
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
    QuotesIDPutResponse quotesIDPutResponse = (QuotesIDPutResponse) o;
    return Objects.equals(this.transferAmount, quotesIDPutResponse.transferAmount) &&
        Objects.equals(this.payeeReceiveAmount, quotesIDPutResponse.payeeReceiveAmount) &&
        Objects.equals(this.payeeFspFee, quotesIDPutResponse.payeeFspFee) &&
        Objects.equals(this.payeeFspCommission, quotesIDPutResponse.payeeFspCommission) &&
        Objects.equals(this.expiration, quotesIDPutResponse.expiration) &&
        Objects.equals(this.geoCode, quotesIDPutResponse.geoCode) &&
        Objects.equals(this.ilpPacket, quotesIDPutResponse.ilpPacket) &&
        Objects.equals(this.condition, quotesIDPutResponse.condition) &&
        Objects.equals(this.extensionList, quotesIDPutResponse.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transferAmount, payeeReceiveAmount, payeeFspFee, payeeFspCommission, expiration, geoCode, ilpPacket, condition, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuotesIDPutResponse {\n");
    
    sb.append("    transferAmount: ").append(toIndentedString(transferAmount)).append("\n");
    sb.append("    payeeReceiveAmount: ").append(toIndentedString(payeeReceiveAmount)).append("\n");
    sb.append("    payeeFspFee: ").append(toIndentedString(payeeFspFee)).append("\n");
    sb.append("    payeeFspCommission: ").append(toIndentedString(payeeFspCommission)).append("\n");
    sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
    sb.append("    geoCode: ").append(toIndentedString(geoCode)).append("\n");
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

