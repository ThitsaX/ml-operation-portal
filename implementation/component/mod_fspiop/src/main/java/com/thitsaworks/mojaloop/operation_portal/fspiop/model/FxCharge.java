package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.thitsaworks.mojaloop.operation_portal.fspiop.model.FxChargeSourceAmount;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.FxChargeTargetAmount;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * An FXP will be able to specify a charge which it proposes to levy on the currency conversion operation using a FxCharge object.
 **/

@JsonTypeName("FxCharge")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-08T12:54:58.680324+06:30[Asia/Rangoon]")
public class FxCharge   {
  private @Valid String chargeType;
  private @Valid FxChargeSourceAmount sourceAmount;
  private @Valid FxChargeTargetAmount targetAmount;

  /**
   * A description of the charge which is being levied.
   **/
  public FxCharge chargeType(String chargeType) {
    this.chargeType = chargeType;
    return this;
  }

  
  @JsonProperty("chargeType")
  @NotNull
 @Size(min=1,max=32)  public String getChargeType() {
    return chargeType;
  }

  @JsonProperty("chargeType")
  public void setChargeType(String chargeType) {
    this.chargeType = chargeType;
  }

  /**
   **/
  public FxCharge sourceAmount(FxChargeSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
    return this;
  }

  
  @JsonProperty("sourceAmount")
  public FxChargeSourceAmount getSourceAmount() {
    return sourceAmount;
  }

  @JsonProperty("sourceAmount")
  public void setSourceAmount(FxChargeSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  /**
   **/
  public FxCharge targetAmount(FxChargeTargetAmount targetAmount) {
    this.targetAmount = targetAmount;
    return this;
  }

  
  @JsonProperty("targetAmount")
  public FxChargeTargetAmount getTargetAmount() {
    return targetAmount;
  }

  @JsonProperty("targetAmount")
  public void setTargetAmount(FxChargeTargetAmount targetAmount) {
    this.targetAmount = targetAmount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FxCharge fxCharge = (FxCharge) o;
    return Objects.equals(this.chargeType, fxCharge.chargeType) &&
        Objects.equals(this.sourceAmount, fxCharge.sourceAmount) &&
        Objects.equals(this.targetAmount, fxCharge.targetAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chargeType, sourceAmount, targetAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FxCharge {\n");
    
    sb.append("    chargeType: ").append(toIndentedString(chargeType)).append("\n");
    sb.append("    sourceAmount: ").append(toIndentedString(sourceAmount)).append("\n");
    sb.append("    targetAmount: ").append(toIndentedString(targetAmount)).append("\n");
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

