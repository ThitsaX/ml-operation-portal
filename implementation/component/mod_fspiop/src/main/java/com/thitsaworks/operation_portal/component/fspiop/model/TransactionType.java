package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.Refund;
import com.thitsaworks.operation_portal.component.fspiop.model.TransactionInitiator;
import com.thitsaworks.operation_portal.component.fspiop.model.TransactionInitiatorType;
import com.thitsaworks.operation_portal.component.fspiop.model.TransactionScenario;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type TransactionType.
 **/

@JsonTypeName("TransactionType")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-24T23:47:57.744005+06:30[Asia/Rangoon]")
public class TransactionType   {
  private @Valid TransactionScenario scenario;
  private @Valid String subScenario;
  private @Valid TransactionInitiator initiator;
  private @Valid TransactionInitiatorType initiatorType;
  private @Valid Refund refundInfo;
  private @Valid String balanceOfPayments;

  /**
   **/
  public TransactionType scenario(TransactionScenario scenario) {
    this.scenario = scenario;
    return this;
  }

  
  @JsonProperty("scenario")
  @NotNull
  public TransactionScenario getScenario() {
    return scenario;
  }

  @JsonProperty("scenario")
  public void setScenario(TransactionScenario scenario) {
    this.scenario = scenario;
  }

  /**
   * Possible sub-scenario, defined locally within the scheme (UndefinedEnum Type).
   **/
  public TransactionType subScenario(String subScenario) {
    this.subScenario = subScenario;
    return this;
  }

  
  @JsonProperty("subScenario")
 @Pattern(regexp="^[A-Z_]{1,32}$")  public String getSubScenario() {
    return subScenario;
  }

  @JsonProperty("subScenario")
  public void setSubScenario(String subScenario) {
    this.subScenario = subScenario;
  }

  /**
   **/
  public TransactionType initiator(TransactionInitiator initiator) {
    this.initiator = initiator;
    return this;
  }

  
  @JsonProperty("initiator")
  @NotNull
  public TransactionInitiator getInitiator() {
    return initiator;
  }

  @JsonProperty("initiator")
  public void setInitiator(TransactionInitiator initiator) {
    this.initiator = initiator;
  }

  /**
   **/
  public TransactionType initiatorType(TransactionInitiatorType initiatorType) {
    this.initiatorType = initiatorType;
    return this;
  }

  
  @JsonProperty("initiatorType")
  @NotNull
  public TransactionInitiatorType getInitiatorType() {
    return initiatorType;
  }

  @JsonProperty("initiatorType")
  public void setInitiatorType(TransactionInitiatorType initiatorType) {
    this.initiatorType = initiatorType;
  }

  /**
   **/
  public TransactionType refundInfo(Refund refundInfo) {
    this.refundInfo = refundInfo;
    return this;
  }

  
  @JsonProperty("refundInfo")
  public Refund getRefundInfo() {
    return refundInfo;
  }

  @JsonProperty("refundInfo")
  public void setRefundInfo(Refund refundInfo) {
    this.refundInfo = refundInfo;
  }

  /**
   * (BopCode) The API data type [BopCode](https://www.imf.org/external/np/sta/bopcode/) is a JSON String of 3 characters, consisting of digits only. Negative numbers are not allowed. A leading zero is not allowed.
   **/
  public TransactionType balanceOfPayments(String balanceOfPayments) {
    this.balanceOfPayments = balanceOfPayments;
    return this;
  }

  
  @JsonProperty("balanceOfPayments")
 @Pattern(regexp="^[1-9]\\d{2}$")  public String getBalanceOfPayments() {
    return balanceOfPayments;
  }

  @JsonProperty("balanceOfPayments")
  public void setBalanceOfPayments(String balanceOfPayments) {
    this.balanceOfPayments = balanceOfPayments;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionType transactionType = (TransactionType) o;
    return Objects.equals(this.scenario, transactionType.scenario) &&
        Objects.equals(this.subScenario, transactionType.subScenario) &&
        Objects.equals(this.initiator, transactionType.initiator) &&
        Objects.equals(this.initiatorType, transactionType.initiatorType) &&
        Objects.equals(this.refundInfo, transactionType.refundInfo) &&
        Objects.equals(this.balanceOfPayments, transactionType.balanceOfPayments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scenario, subScenario, initiator, initiatorType, refundInfo, balanceOfPayments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionType {\n");
    
    sb.append("    scenario: ").append(toIndentedString(scenario)).append("\n");
    sb.append("    subScenario: ").append(toIndentedString(subScenario)).append("\n");
    sb.append("    initiator: ").append(toIndentedString(initiator)).append("\n");
    sb.append("    initiatorType: ").append(toIndentedString(initiatorType)).append("\n");
    sb.append("    refundInfo: ").append(toIndentedString(refundInfo)).append("\n");
    sb.append("    balanceOfPayments: ").append(toIndentedString(balanceOfPayments)).append("\n");
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

