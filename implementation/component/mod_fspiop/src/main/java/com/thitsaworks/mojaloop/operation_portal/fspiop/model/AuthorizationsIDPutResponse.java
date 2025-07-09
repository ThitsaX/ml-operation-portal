package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.thitsaworks.mojaloop.operation_portal.fspiop.model.AuthenticationInfo;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.AuthorizationResponse;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The object sent in the PUT /authorizations/{ID} callback.
 **/

@JsonTypeName("AuthorizationsIDPutResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-09T22:29:32.007258+06:30[Asia/Rangoon]")
public class AuthorizationsIDPutResponse   {
  private @Valid AuthenticationInfo authenticationInfo;
  private @Valid AuthorizationResponse responseType;

  /**
   **/
  public AuthorizationsIDPutResponse authenticationInfo(AuthenticationInfo authenticationInfo) {
    this.authenticationInfo = authenticationInfo;
    return this;
  }

  
  @JsonProperty("authenticationInfo")
  public AuthenticationInfo getAuthenticationInfo() {
    return authenticationInfo;
  }

  @JsonProperty("authenticationInfo")
  public void setAuthenticationInfo(AuthenticationInfo authenticationInfo) {
    this.authenticationInfo = authenticationInfo;
  }

  /**
   **/
  public AuthorizationsIDPutResponse responseType(AuthorizationResponse responseType) {
    this.responseType = responseType;
    return this;
  }

  
  @JsonProperty("responseType")
  @NotNull
  public AuthorizationResponse getResponseType() {
    return responseType;
  }

  @JsonProperty("responseType")
  public void setResponseType(AuthorizationResponse responseType) {
    this.responseType = responseType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthorizationsIDPutResponse authorizationsIDPutResponse = (AuthorizationsIDPutResponse) o;
    return Objects.equals(this.authenticationInfo, authorizationsIDPutResponse.authenticationInfo) &&
        Objects.equals(this.responseType, authorizationsIDPutResponse.responseType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationInfo, responseType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthorizationsIDPutResponse {\n");
    
    sb.append("    authenticationInfo: ").append(toIndentedString(authenticationInfo)).append("\n");
    sb.append("    responseType: ").append(toIndentedString(responseType)).append("\n");
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

