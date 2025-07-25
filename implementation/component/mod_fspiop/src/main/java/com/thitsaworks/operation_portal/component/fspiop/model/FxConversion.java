package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.AmountType;
import com.thitsaworks.operation_portal.component.fspiop.model.FxCharge;
import com.thitsaworks.operation_portal.component.fspiop.model.FxConversionExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.FxConversionSourceAmount;
import com.thitsaworks.operation_portal.component.fspiop.model.FxConversionTargetAmount;
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
 * A DFSP will be able to request a currency conversion, and an FX provider will be able to describe its involvement in a proposed transfer, using a FxConversion object.
 **/

@JsonTypeName("FxConversion")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-24T23:47:57.744005+06:30[Asia/Rangoon]")
public class FxConversion   {
  private @Valid String conversionId;
  private @Valid String determiningTransferId;
  private @Valid String initiatingFsp;
  private @Valid String counterPartyFsp;
  private @Valid AmountType amountType;
  private @Valid FxConversionSourceAmount sourceAmount;
  private @Valid FxConversionTargetAmount targetAmount;
  private @Valid String expiration;
  private @Valid List<FxCharge> charges;
  private @Valid FxConversionExtensionList extensionList;

  /**
   **/
  public FxConversion conversionId(String conversionId) {
    this.conversionId = conversionId;
    return this;
  }

  
  @JsonProperty("conversionId")
  @NotNull
  public String getConversionId() {
    return conversionId;
  }

  @JsonProperty("conversionId")
  public void setConversionId(String conversionId) {
    this.conversionId = conversionId;
  }

  /**
   **/
  public FxConversion determiningTransferId(String determiningTransferId) {
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
  public FxConversion initiatingFsp(String initiatingFsp) {
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
  public FxConversion counterPartyFsp(String counterPartyFsp) {
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
  public FxConversion amountType(AmountType amountType) {
    this.amountType = amountType;
    return this;
  }

  
  @JsonProperty("amountType")
  @NotNull
  public AmountType getAmountType() {
    return amountType;
  }

  @JsonProperty("amountType")
  public void setAmountType(AmountType amountType) {
    this.amountType = amountType;
  }

  /**
   **/
  public FxConversion sourceAmount(FxConversionSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
    return this;
  }

  
  @JsonProperty("sourceAmount")
  @NotNull
  public FxConversionSourceAmount getSourceAmount() {
    return sourceAmount;
  }

  @JsonProperty("sourceAmount")
  public void setSourceAmount(FxConversionSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  /**
   **/
  public FxConversion targetAmount(FxConversionTargetAmount targetAmount) {
    this.targetAmount = targetAmount;
    return this;
  }

  
  @JsonProperty("targetAmount")
  @NotNull
  public FxConversionTargetAmount getTargetAmount() {
    return targetAmount;
  }

  @JsonProperty("targetAmount")
  public void setTargetAmount(FxConversionTargetAmount targetAmount) {
    this.targetAmount = targetAmount;
  }

  /**
   **/
  public FxConversion expiration(String expiration) {
    this.expiration = expiration;
    return this;
  }

  
  @JsonProperty("expiration")
  @NotNull
  public String getExpiration() {
    return expiration;
  }

  @JsonProperty("expiration")
  public void setExpiration(String expiration) {
    this.expiration = expiration;
  }

  /**
   * One or more charges which the FXP intends to levy as part of the currency conversion, or which the payee DFSP intends to add to the amount transferred.
   **/
  public FxConversion charges(List<FxCharge> charges) {
    this.charges = charges;
    return this;
  }

  
  @JsonProperty("charges")
 @Size(min=0,max=16)  public List<FxCharge> getCharges() {
    return charges;
  }

  @JsonProperty("charges")
  public void setCharges(List<FxCharge> charges) {
    this.charges = charges;
  }

  public FxConversion addChargesItem(FxCharge chargesItem) {
    if (this.charges == null) {
      this.charges = new ArrayList<>();
    }

    this.charges.add(chargesItem);
    return this;
  }

  public FxConversion removeChargesItem(FxCharge chargesItem) {
    if (chargesItem != null && this.charges != null) {
      this.charges.remove(chargesItem);
    }

    return this;
  }
  /**
   **/
  public FxConversion extensionList(FxConversionExtensionList extensionList) {
    this.extensionList = extensionList;
    return this;
  }

  
  @JsonProperty("extensionList")
  public FxConversionExtensionList getExtensionList() {
    return extensionList;
  }

  @JsonProperty("extensionList")
  public void setExtensionList(FxConversionExtensionList extensionList) {
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
    FxConversion fxConversion = (FxConversion) o;
    return Objects.equals(this.conversionId, fxConversion.conversionId) &&
        Objects.equals(this.determiningTransferId, fxConversion.determiningTransferId) &&
        Objects.equals(this.initiatingFsp, fxConversion.initiatingFsp) &&
        Objects.equals(this.counterPartyFsp, fxConversion.counterPartyFsp) &&
        Objects.equals(this.amountType, fxConversion.amountType) &&
        Objects.equals(this.sourceAmount, fxConversion.sourceAmount) &&
        Objects.equals(this.targetAmount, fxConversion.targetAmount) &&
        Objects.equals(this.expiration, fxConversion.expiration) &&
        Objects.equals(this.charges, fxConversion.charges) &&
        Objects.equals(this.extensionList, fxConversion.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conversionId, determiningTransferId, initiatingFsp, counterPartyFsp, amountType, sourceAmount, targetAmount, expiration, charges, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FxConversion {\n");
    
    sb.append("    conversionId: ").append(toIndentedString(conversionId)).append("\n");
    sb.append("    determiningTransferId: ").append(toIndentedString(determiningTransferId)).append("\n");
    sb.append("    initiatingFsp: ").append(toIndentedString(initiatingFsp)).append("\n");
    sb.append("    counterPartyFsp: ").append(toIndentedString(counterPartyFsp)).append("\n");
    sb.append("    amountType: ").append(toIndentedString(amountType)).append("\n");
    sb.append("    sourceAmount: ").append(toIndentedString(sourceAmount)).append("\n");
    sb.append("    targetAmount: ").append(toIndentedString(targetAmount)).append("\n");
    sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
    sb.append("    charges: ").append(toIndentedString(charges)).append("\n");
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

