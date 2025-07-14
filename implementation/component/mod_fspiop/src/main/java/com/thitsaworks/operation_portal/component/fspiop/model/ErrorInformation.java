package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type ErrorInformation.
 **/

@JsonTypeName("ErrorInformation")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-11T18:18:34.338733500+09:00[Asia/Tokyo]")
public class ErrorInformation   {
  private @Valid String errorCode;
  private @Valid String errorDescription;
  private @Valid ExtensionList extensionList;

  /**
   * The API data type ErrorCode is a JSON String of four characters, consisting of digits only. Negative numbers are not allowed. A leading zero is not allowed. Each error code in the API is a four-digit number, for example, 1234, where the first number (1 in the example) represents the high-level error category, the second number (2 in the example) represents the low-level error category, and the last two numbers (34 in the example) represent the specific error.
   **/
  public ErrorInformation errorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  
  @JsonProperty("errorCode")
  @NotNull
 @Pattern(regexp="^[1-9]\\d{3}$")  public String getErrorCode() {
    return errorCode;
  }

  @JsonProperty("errorCode")
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * Error description string.
   **/
  public ErrorInformation errorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
    return this;
  }

  
  @JsonProperty("errorDescription")
  @NotNull
 @Size(min=1,max=128)  public String getErrorDescription() {
    return errorDescription;
  }

  @JsonProperty("errorDescription")
  public void setErrorDescription(String errorDescription) {
    this.errorDescription = errorDescription;
  }

  /**
   **/
  public ErrorInformation extensionList(ExtensionList extensionList) {
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
    ErrorInformation errorInformation = (ErrorInformation) o;
    return Objects.equals(this.errorCode, errorInformation.errorCode) &&
        Objects.equals(this.errorDescription, errorInformation.errorDescription) &&
        Objects.equals(this.extensionList, errorInformation.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorCode, errorDescription, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorInformation {\n");
    
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    errorDescription: ").append(toIndentedString(errorDescription)).append("\n");
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

