package com.thitsaworks.operation_portal.component.fspiop.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.thitsaworks.operation_portal.component.fspiop.model.FxRateSourceAmount;
import com.thitsaworks.operation_portal.component.fspiop.model.FxRateTargetAmount;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("QuotesPostRequest_currencyConversion")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-24T23:47:57.744005+06:30[Asia/Rangoon]")
public class QuotesPostRequestCurrencyConversion   {
  private @Valid FxRateSourceAmount sourceAmount;
  private @Valid FxRateTargetAmount targetAmount;

  /**
   **/
  public QuotesPostRequestCurrencyConversion sourceAmount(FxRateSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
    return this;
  }

  
  @JsonProperty("sourceAmount")
  @NotNull
  public FxRateSourceAmount getSourceAmount() {
    return sourceAmount;
  }

  @JsonProperty("sourceAmount")
  public void setSourceAmount(FxRateSourceAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  /**
   **/
  public QuotesPostRequestCurrencyConversion targetAmount(FxRateTargetAmount targetAmount) {
    this.targetAmount = targetAmount;
    return this;
  }

  
  @JsonProperty("targetAmount")
  @NotNull
  public FxRateTargetAmount getTargetAmount() {
    return targetAmount;
  }

  @JsonProperty("targetAmount")
  public void setTargetAmount(FxRateTargetAmount targetAmount) {
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
    QuotesPostRequestCurrencyConversion quotesPostRequestCurrencyConversion = (QuotesPostRequestCurrencyConversion) o;
    return Objects.equals(this.sourceAmount, quotesPostRequestCurrencyConversion.sourceAmount) &&
        Objects.equals(this.targetAmount, quotesPostRequestCurrencyConversion.targetAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceAmount, targetAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuotesPostRequestCurrencyConversion {\n");
    
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

