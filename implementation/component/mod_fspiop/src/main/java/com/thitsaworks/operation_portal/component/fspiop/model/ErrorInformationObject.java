package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformation;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type object that contains ErrorInformation.
 **/

@JsonTypeName("ErrorInformationObject")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-10T18:45:52.776834300+09:00[Asia/Tokyo]")
public class ErrorInformationObject   {
  private @Valid ErrorInformation errorInformation;

  /**
   **/
  public ErrorInformationObject errorInformation(ErrorInformation errorInformation) {
    this.errorInformation = errorInformation;
    return this;
  }

  
  @JsonProperty("errorInformation")
  @NotNull
  public ErrorInformation getErrorInformation() {
    return errorInformation;
  }

  @JsonProperty("errorInformation")
  public void setErrorInformation(ErrorInformation errorInformation) {
    this.errorInformation = errorInformation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorInformationObject errorInformationObject = (ErrorInformationObject) o;
    return Objects.equals(this.errorInformation, errorInformationObject.errorInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorInformation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorInformationObject {\n");
    
    sb.append("    errorInformation: ").append(toIndentedString(errorInformation)).append("\n");
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

