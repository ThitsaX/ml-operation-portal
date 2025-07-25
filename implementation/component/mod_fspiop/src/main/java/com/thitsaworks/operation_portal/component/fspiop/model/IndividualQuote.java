package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.AmountType;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.fspiop.model.Party;
import com.thitsaworks.operation_portal.component.fspiop.model.TransactionType;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type IndividualQuote.
 **/

@JsonTypeName("IndividualQuote")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-24T23:47:57.744005+06:30[Asia/Rangoon]")
public class IndividualQuote   {
  private @Valid String quoteId;
  private @Valid String transactionId;
  private @Valid Party payee;
  private @Valid AmountType amountType;
  private @Valid Money amount;
  private @Valid Money fees;
  private @Valid TransactionType transactionType;
  private @Valid String note;
  private @Valid ExtensionList extensionList;

  /**
   * Identifier that correlates all messages of the same sequence. The API data type UUID (Universally Unique Identifier) is a JSON String in canonical format, conforming to [RFC 4122](https://tools.ietf.org/html/rfc4122), that is restricted by a regular expression for interoperability reasons. A UUID is always 36 characters long, 32 hexadecimal symbols and 4 dashes (‘-‘).
   **/
  public IndividualQuote quoteId(String quoteId) {
    this.quoteId = quoteId;
    return this;
  }

  
  @JsonProperty("quoteId")
  @NotNull
 @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")  public String getQuoteId() {
    return quoteId;
  }

  @JsonProperty("quoteId")
  public void setQuoteId(String quoteId) {
    this.quoteId = quoteId;
  }

  /**
   * Identifier that correlates all messages of the same sequence. The API data type UUID (Universally Unique Identifier) is a JSON String in canonical format, conforming to [RFC 4122](https://tools.ietf.org/html/rfc4122), that is restricted by a regular expression for interoperability reasons. A UUID is always 36 characters long, 32 hexadecimal symbols and 4 dashes (‘-‘).
   **/
  public IndividualQuote transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  
  @JsonProperty("transactionId")
  @NotNull
 @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")  public String getTransactionId() {
    return transactionId;
  }

  @JsonProperty("transactionId")
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  /**
   **/
  public IndividualQuote payee(Party payee) {
    this.payee = payee;
    return this;
  }

  
  @JsonProperty("payee")
  @NotNull
  public Party getPayee() {
    return payee;
  }

  @JsonProperty("payee")
  public void setPayee(Party payee) {
    this.payee = payee;
  }

  /**
   **/
  public IndividualQuote amountType(AmountType amountType) {
    this.amountType = amountType;
    return this;
  }

  
  @JsonProperty("amountType")
  @NotNull
  public AmountType getAmountType() {
    return amountType;
  }

  @JsonProperty("amountType")
  public void setAmountType(AmountType amountType) {
    this.amountType = amountType;
  }

  /**
   **/
  public IndividualQuote amount(Money amount) {
    this.amount = amount;
    return this;
  }

  
  @JsonProperty("amount")
  @NotNull
  public Money getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Money amount) {
    this.amount = amount;
  }

  /**
   **/
  public IndividualQuote fees(Money fees) {
    this.fees = fees;
    return this;
  }

  
  @JsonProperty("fees")
  public Money getFees() {
    return fees;
  }

  @JsonProperty("fees")
  public void setFees(Money fees) {
    this.fees = fees;
  }

  /**
   **/
  public IndividualQuote transactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
    return this;
  }

  
  @JsonProperty("transactionType")
  @NotNull
  public TransactionType getTransactionType() {
    return transactionType;
  }

  @JsonProperty("transactionType")
  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  /**
   * Memo assigned to transaction.
   **/
  public IndividualQuote note(String note) {
    this.note = note;
    return this;
  }

  
  @JsonProperty("note")
 @Size(min=1,max=128)  public String getNote() {
    return note;
  }

  @JsonProperty("note")
  public void setNote(String note) {
    this.note = note;
  }

  /**
   **/
  public IndividualQuote extensionList(ExtensionList extensionList) {
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
    IndividualQuote individualQuote = (IndividualQuote) o;
    return Objects.equals(this.quoteId, individualQuote.quoteId) &&
        Objects.equals(this.transactionId, individualQuote.transactionId) &&
        Objects.equals(this.payee, individualQuote.payee) &&
        Objects.equals(this.amountType, individualQuote.amountType) &&
        Objects.equals(this.amount, individualQuote.amount) &&
        Objects.equals(this.fees, individualQuote.fees) &&
        Objects.equals(this.transactionType, individualQuote.transactionType) &&
        Objects.equals(this.note, individualQuote.note) &&
        Objects.equals(this.extensionList, individualQuote.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quoteId, transactionId, payee, amountType, amount, fees, transactionType, note, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndividualQuote {\n");
    
    sb.append("    quoteId: ").append(toIndentedString(quoteId)).append("\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    payee: ").append(toIndentedString(payee)).append("\n");
    sb.append("    amountType: ").append(toIndentedString(amountType)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    fees: ").append(toIndentedString(fees)).append("\n");
    sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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

