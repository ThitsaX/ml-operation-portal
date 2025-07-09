package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.thitsaworks.mojaloop.operation_portal.fspiop.model.FxQuotesIDPutResponseConversionTerms;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The object sent in the PUT /fxQuotes/{ID} callback.
 **/

@JsonTypeName("FxQuotesIDPutResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-08T18:26:12.180501+06:30[Asia/Rangoon]")
public class FxQuotesIDPutResponse   {
  private @Valid String condition;
  private @Valid FxQuotesIDPutResponseConversionTerms conversionTerms;

  /**
   **/
  public FxQuotesIDPutResponse condition(String condition) {
    this.condition = condition;
    return this;
  }

  
  @JsonProperty("condition")
  public String getCondition() {
    return condition;
  }

  @JsonProperty("condition")
  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   **/
  public FxQuotesIDPutResponse conversionTerms(FxQuotesIDPutResponseConversionTerms conversionTerms) {
    this.conversionTerms = conversionTerms;
    return this;
  }

  
  @JsonProperty("conversionTerms")
  @NotNull
  public FxQuotesIDPutResponseConversionTerms getConversionTerms() {
    return conversionTerms;
  }

  @JsonProperty("conversionTerms")
  public void setConversionTerms(FxQuotesIDPutResponseConversionTerms conversionTerms) {
    this.conversionTerms = conversionTerms;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FxQuotesIDPutResponse fxQuotesIDPutResponse = (FxQuotesIDPutResponse) o;
    return Objects.equals(this.condition, fxQuotesIDPutResponse.condition) &&
        Objects.equals(this.conversionTerms, fxQuotesIDPutResponse.conversionTerms);
  }

  @Override
  public int hashCode() {
    return Objects.hash(condition, conversionTerms);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FxQuotesIDPutResponse {\n");
    
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    conversionTerms: ").append(toIndentedString(conversionTerms)).append("\n");
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

