package com.thitsaworks.mojaloop.operation_portal.fspiop.model;

import com.thitsaworks.mojaloop.operation_portal.fspiop.model.AuthenticationType;
import com.thitsaworks.mojaloop.operation_portal.fspiop.model.AuthenticationValue;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type AuthenticationInfo.
 **/

@JsonTypeName("AuthenticationInfo")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-08T12:54:58.680324+06:30[Asia/Rangoon]")
public class AuthenticationInfo   {
  private @Valid AuthenticationType authentication;
  private @Valid AuthenticationValue authenticationValue;

  /**
   **/
  public AuthenticationInfo authentication(AuthenticationType authentication) {
    this.authentication = authentication;
    return this;
  }

  
  @JsonProperty("authentication")
  @NotNull
  public AuthenticationType getAuthentication() {
    return authentication;
  }

  @JsonProperty("authentication")
  public void setAuthentication(AuthenticationType authentication) {
    this.authentication = authentication;
  }

  /**
   **/
  public AuthenticationInfo authenticationValue(AuthenticationValue authenticationValue) {
    this.authenticationValue = authenticationValue;
    return this;
  }

  
  @JsonProperty("authenticationValue")
  @NotNull
  public AuthenticationValue getAuthenticationValue() {
    return authenticationValue;
  }

  @JsonProperty("authenticationValue")
  public void setAuthenticationValue(AuthenticationValue authenticationValue) {
    this.authenticationValue = authenticationValue;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthenticationInfo authenticationInfo = (AuthenticationInfo) o;
    return Objects.equals(this.authentication, authenticationInfo.authentication) &&
        Objects.equals(this.authenticationValue, authenticationInfo.authenticationValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authentication, authenticationValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthenticationInfo {\n");
    
    sb.append("    authentication: ").append(toIndentedString(authentication)).append("\n");
    sb.append("    authenticationValue: ").append(toIndentedString(authenticationValue)).append("\n");
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

