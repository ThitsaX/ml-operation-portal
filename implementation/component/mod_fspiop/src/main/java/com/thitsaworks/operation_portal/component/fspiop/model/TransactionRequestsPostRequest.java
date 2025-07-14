package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.AuthenticationType;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.GeoCode;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.fspiop.model.Party;
import com.thitsaworks.operation_portal.component.fspiop.model.PartyIdInfo;
import com.thitsaworks.operation_portal.component.fspiop.model.TransactionType;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The object sent in the POST /transactionRequests request.
 **/

@JsonTypeName("TransactionRequestsPostRequest")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-11T18:18:34.338733500+09:00[Asia/Tokyo]")
public class TransactionRequestsPostRequest   {
  private @Valid String transactionRequestId;
  private @Valid Party payee;
  private @Valid PartyIdInfo payer;
  private @Valid Money amount;
  private @Valid TransactionType transactionType;
  private @Valid String note;
  private @Valid GeoCode geoCode;
  private @Valid AuthenticationType authenticationType;
  private @Valid String expiration;
  private @Valid ExtensionList extensionList;

  /**
   * Identifier that correlates all messages of the same sequence. The API data type UUID (Universally Unique Identifier) is a JSON String in canonical format, conforming to [RFC 4122](https://tools.ietf.org/html/rfc4122), that is restricted by a regular expression for interoperability reasons. A UUID is always 36 characters long, 32 hexadecimal symbols and 4 dashes (‘-‘).
   **/
  public TransactionRequestsPostRequest transactionRequestId(String transactionRequestId) {
    this.transactionRequestId = transactionRequestId;
    return this;
  }

  
  @JsonProperty("transactionRequestId")
  @NotNull
 @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")  public String getTransactionRequestId() {
    return transactionRequestId;
  }

  @JsonProperty("transactionRequestId")
  public void setTransactionRequestId(String transactionRequestId) {
    this.transactionRequestId = transactionRequestId;
  }

  /**
   **/
  public TransactionRequestsPostRequest payee(Party payee) {
    this.payee = payee;
    return this;
  }

  
  @JsonProperty("payee")
  @NotNull
  public Party getPayee() {
    return payee;
  }

  @JsonProperty("payee")
  public void setPayee(Party payee) {
    this.payee = payee;
  }

  /**
   **/
  public TransactionRequestsPostRequest payer(PartyIdInfo payer) {
    this.payer = payer;
    return this;
  }

  
  @JsonProperty("payer")
  @NotNull
  public PartyIdInfo getPayer() {
    return payer;
  }

  @JsonProperty("payer")
  public void setPayer(PartyIdInfo payer) {
    this.payer = payer;
  }

  /**
   **/
  public TransactionRequestsPostRequest amount(Money amount) {
    this.amount = amount;
    return this;
  }

  
  @JsonProperty("amount")
  @NotNull
  public Money getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Money amount) {
    this.amount = amount;
  }

  /**
   **/
  public TransactionRequestsPostRequest transactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
    return this;
  }

  
  @JsonProperty("transactionType")
  @NotNull
  public TransactionType getTransactionType() {
    return transactionType;
  }

  @JsonProperty("transactionType")
  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  /**
   * Memo assigned to transaction.
   **/
  public TransactionRequestsPostRequest note(String note) {
    this.note = note;
    return this;
  }

  
  @JsonProperty("note")
 @Size(min=1,max=128)  public String getNote() {
    return note;
  }

  @JsonProperty("note")
  public void setNote(String note) {
    this.note = note;
  }

  /**
   **/
  public TransactionRequestsPostRequest geoCode(GeoCode geoCode) {
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
   **/
  public TransactionRequestsPostRequest authenticationType(AuthenticationType authenticationType) {
    this.authenticationType = authenticationType;
    return this;
  }

  
  @JsonProperty("authenticationType")
  public AuthenticationType getAuthenticationType() {
    return authenticationType;
  }

  @JsonProperty("authenticationType")
  public void setAuthenticationType(AuthenticationType authenticationType) {
    this.authenticationType = authenticationType;
  }

  /**
   * The API data type DateTime is a JSON String in a lexical format that is restricted by a regular expression for interoperability reasons. The format is according to [ISO 8601](https://www.iso.org/iso-8601-date-and-time-format.html), expressed in a combined date, time and time zone format. A more readable version of the format is yyyy-MM-ddTHH:mm:ss.SSS[-HH:MM]. Examples are \&quot;2016-05-24T08:38:08.699-04:00\&quot;, \&quot;2016-05-24T08:38:08.699Z\&quot; (where Z indicates Zulu time zone, same as UTC).
   **/
  public TransactionRequestsPostRequest expiration(String expiration) {
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

  /**
   **/
  public TransactionRequestsPostRequest extensionList(ExtensionList extensionList) {
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
    TransactionRequestsPostRequest transactionRequestsPostRequest = (TransactionRequestsPostRequest) o;
    return Objects.equals(this.transactionRequestId, transactionRequestsPostRequest.transactionRequestId) &&
        Objects.equals(this.payee, transactionRequestsPostRequest.payee) &&
        Objects.equals(this.payer, transactionRequestsPostRequest.payer) &&
        Objects.equals(this.amount, transactionRequestsPostRequest.amount) &&
        Objects.equals(this.transactionType, transactionRequestsPostRequest.transactionType) &&
        Objects.equals(this.note, transactionRequestsPostRequest.note) &&
        Objects.equals(this.geoCode, transactionRequestsPostRequest.geoCode) &&
        Objects.equals(this.authenticationType, transactionRequestsPostRequest.authenticationType) &&
        Objects.equals(this.expiration, transactionRequestsPostRequest.expiration) &&
        Objects.equals(this.extensionList, transactionRequestsPostRequest.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionRequestId, payee, payer, amount, transactionType, note, geoCode, authenticationType, expiration, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionRequestsPostRequest {\n");
    
    sb.append("    transactionRequestId: ").append(toIndentedString(transactionRequestId)).append("\n");
    sb.append("    payee: ").append(toIndentedString(payee)).append("\n");
    sb.append("    payer: ").append(toIndentedString(payer)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    geoCode: ").append(toIndentedString(geoCode)).append("\n");
    sb.append("    authenticationType: ").append(toIndentedString(authenticationType)).append("\n");
    sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
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

