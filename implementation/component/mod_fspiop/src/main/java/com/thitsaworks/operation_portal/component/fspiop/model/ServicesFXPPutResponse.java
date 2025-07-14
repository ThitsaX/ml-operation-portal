package com.thitsaworks.operation_portal.component.fspiop.model;

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
 * The object sent in the PUT /services/FXP callback.
 **/

@JsonTypeName("ServicesFXPPutResponse")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-10T18:45:52.776834300+09:00[Asia/Tokyo]")
public class ServicesFXPPutResponse   {
  private @Valid List<String> providers = new ArrayList<>();

  /**
   * The FSP Id(s) of the participant(s) who offer currency conversion services.
   **/
  public ServicesFXPPutResponse providers(List<String> providers) {
    this.providers = providers;
    return this;
  }

  
  @JsonProperty("providers")
  @NotNull
 @Size(min=0,max=16)  public List< @Size(min=1,max=32)String> getProviders() {
    return providers;
  }

  @JsonProperty("providers")
  public void setProviders(List<String> providers) {
    this.providers = providers;
  }

  public ServicesFXPPutResponse addProvidersItem(String providersItem) {
    if (this.providers == null) {
      this.providers = new ArrayList<>();
    }

    this.providers.add(providersItem);
    return this;
  }

  public ServicesFXPPutResponse removeProvidersItem(String providersItem) {
    if (providersItem != null && this.providers != null) {
      this.providers.remove(providersItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServicesFXPPutResponse servicesFXPPutResponse = (ServicesFXPPutResponse) o;
    return Objects.equals(this.providers, servicesFXPPutResponse.providers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(providers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServicesFXPPutResponse {\n");
    
    sb.append("    providers: ").append(toIndentedString(providers)).append("\n");
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

