package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.Extension;
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
 * Data model for the complex type ExtensionList. An optional list of extensions, specific to deployment.
 **/

@JsonTypeName("ExtensionList")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-10T15:05:44.502855+07:00[Asia/Bangkok]")
public class ExtensionList   {
  private @Valid List<Extension> extension = new ArrayList<>();

  /**
   * Number of Extension elements.
   **/
  public ExtensionList extension(List<Extension> extension) {
    this.extension = extension;
    return this;
  }

  
  @JsonProperty("extension")
  @NotNull
 @Size(min=1,max=16)  public List<Extension> getExtension() {
    return extension;
  }

  @JsonProperty("extension")
  public void setExtension(List<Extension> extension) {
    this.extension = extension;
  }

  public ExtensionList addExtensionItem(Extension extensionItem) {
    if (this.extension == null) {
      this.extension = new ArrayList<>();
    }

    this.extension.add(extensionItem);
    return this;
  }

  public ExtensionList removeExtensionItem(Extension extensionItem) {
    if (extensionItem != null && this.extension != null) {
      this.extension.remove(extensionItem);
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
    ExtensionList extensionList = (ExtensionList) o;
    return Objects.equals(this.extension, extensionList.extension);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extension);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtensionList {\n");
    
    sb.append("    extension: ").append(toIndentedString(extension)).append("\n");
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

