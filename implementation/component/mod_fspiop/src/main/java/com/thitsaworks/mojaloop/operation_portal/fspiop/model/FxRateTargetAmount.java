package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.Currency;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("FxRate_targetAmount")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-08T12:54:58.680324+06:30[Asia/Rangoon]")
public class FxRateTargetAmount   {
  private @Valid Currency currency;
  private @Valid String amount;

  /**
   **/
  public FxRateTargetAmount currency(Currency currency) {
    this.currency = currency;
    return this;
  }

  
  @JsonProperty("currency")
  @NotNull
  public Currency getCurrency() {
    return currency;
  }

  @JsonProperty("currency")
  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  /**
   * The API data type Amount is a JSON String in a canonical format that is restricted by a regular expression for interoperability reasons. This pattern does not allow any trailing zeroes at all, but allows an amount without a minor currency unit. It also only allows four digits in the minor currency unit; a negative value is not allowed. Using more than 18 digits in the major currency unit is not allowed.
   **/
  public FxRateTargetAmount amount(String amount) {
    this.amount = amount;
    return this;
  }

  
  @JsonProperty("amount")
  @NotNull
 @Pattern(regexp="^([0]|([1-9][0-9]{0,17}))([.][0-9]{0,3}[1-9])?$")  public String getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(String amount) {
    this.amount = amount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FxRateTargetAmount fxRateTargetAmount = (FxRateTargetAmount) o;
    return Objects.equals(this.currency, fxRateTargetAmount.currency) &&
        Objects.equals(this.amount, fxRateTargetAmount.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currency, amount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FxRateTargetAmount {\n");
    
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

