package com.thitsaworks.operation_portal.component.fspiop.model;

import com.thitsaworks.operation_portal.component.fspiop.model.CurrencyConverter;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.component.fspiop.model.Party;
import com.thitsaworks.operation_portal.component.fspiop.model.QuotesPostRequestCurrencyConversion;
import com.thitsaworks.operation_portal.component.fspiop.model.TransactionPayeeReceiveAmount;
import com.thitsaworks.operation_portal.component.fspiop.model.TransactionType;
import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Data model for the complex type Transaction. The Transaction type is used to carry end-to-end data between the Payer FSP and the Payee FSP in the ILP Packet. Both the transactionId and the quoteId in the data model are decided by the Payer FSP in the POST /quotes request.
 **/

@JsonTypeName("Transaction")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-07-24T23:47:57.744005+06:30[Asia/Rangoon]")
public class Transaction   {
  private @Valid String transactionId;
  private @Valid String quoteId;
  private @Valid Party payee;
  private @Valid Party payer;
  private @Valid Money amount;
  private @Valid TransactionPayeeReceiveAmount payeeReceiveAmount;
  private @Valid CurrencyConverter converter;
  private @Valid QuotesPostRequestCurrencyConversion currencyConversion;
  private @Valid TransactionType transactionType;
  private @Valid String note;
  private @Valid ExtensionList extensionList;

  /**
   * Identifier that correlates all messages of the same sequence. The API data type UUID (Universally Unique Identifier) is a JSON String in canonical format, conforming to [RFC 4122](https://tools.ietf.org/html/rfc4122), that is restricted by a regular expression for interoperability reasons. A UUID is always 36 characters long, 32 hexadecimal symbols and 4 dashes (‘-‘).
   **/
  public Transaction transactionId(String transactionId) {
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
   * Identifier that correlates all messages of the same sequence. The API data type UUID (Universally Unique Identifier) is a JSON String in canonical format, conforming to [RFC 4122](https://tools.ietf.org/html/rfc4122), that is restricted by a regular expression for interoperability reasons. A UUID is always 36 characters long, 32 hexadecimal symbols and 4 dashes (‘-‘).
   **/
  public Transaction quoteId(String quoteId) {
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
   **/
  public Transaction payee(Party payee) {
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
  public Transaction payer(Party payer) {
    this.payer = payer;
    return this;
  }

  
  @JsonProperty("payer")
  @NotNull
  public Party getPayer() {
    return payer;
  }

  @JsonProperty("payer")
  public void setPayer(Party payer) {
    this.payer = payer;
  }

  /**
   **/
  public Transaction amount(Money amount) {
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
  public Transaction payeeReceiveAmount(TransactionPayeeReceiveAmount payeeReceiveAmount) {
    this.payeeReceiveAmount = payeeReceiveAmount;
    return this;
  }

  
  @JsonProperty("payeeReceiveAmount")
  public TransactionPayeeReceiveAmount getPayeeReceiveAmount() {
    return payeeReceiveAmount;
  }

  @JsonProperty("payeeReceiveAmount")
  public void setPayeeReceiveAmount(TransactionPayeeReceiveAmount payeeReceiveAmount) {
    this.payeeReceiveAmount = payeeReceiveAmount;
  }

  /**
   **/
  public Transaction converter(CurrencyConverter converter) {
    this.converter = converter;
    return this;
  }

  
  @JsonProperty("converter")
  public CurrencyConverter getConverter() {
    return converter;
  }

  @JsonProperty("converter")
  public void setConverter(CurrencyConverter converter) {
    this.converter = converter;
  }

  /**
   **/
  public Transaction currencyConversion(QuotesPostRequestCurrencyConversion currencyConversion) {
    this.currencyConversion = currencyConversion;
    return this;
  }

  
  @JsonProperty("currencyConversion")
  public QuotesPostRequestCurrencyConversion getCurrencyConversion() {
    return currencyConversion;
  }

  @JsonProperty("currencyConversion")
  public void setCurrencyConversion(QuotesPostRequestCurrencyConversion currencyConversion) {
    this.currencyConversion = currencyConversion;
  }

  /**
   **/
  public Transaction transactionType(TransactionType transactionType) {
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
  public Transaction note(String note) {
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
  public Transaction extensionList(ExtensionList extensionList) {
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
    Transaction transaction = (Transaction) o;
    return Objects.equals(this.transactionId, transaction.transactionId) &&
        Objects.equals(this.quoteId, transaction.quoteId) &&
        Objects.equals(this.payee, transaction.payee) &&
        Objects.equals(this.payer, transaction.payer) &&
        Objects.equals(this.amount, transaction.amount) &&
        Objects.equals(this.payeeReceiveAmount, transaction.payeeReceiveAmount) &&
        Objects.equals(this.converter, transaction.converter) &&
        Objects.equals(this.currencyConversion, transaction.currencyConversion) &&
        Objects.equals(this.transactionType, transaction.transactionType) &&
        Objects.equals(this.note, transaction.note) &&
        Objects.equals(this.extensionList, transaction.extensionList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, quoteId, payee, payer, amount, payeeReceiveAmount, converter, currencyConversion, transactionType, note, extensionList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transaction {\n");
    
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    quoteId: ").append(toIndentedString(quoteId)).append("\n");
    sb.append("    payee: ").append(toIndentedString(payee)).append("\n");
    sb.append("    payer: ").append(toIndentedString(payer)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    payeeReceiveAmount: ").append(toIndentedString(payeeReceiveAmount)).append("\n");
    sb.append("    converter: ").append(toIndentedString(converter)).append("\n");
    sb.append("    currencyConversion: ").append(toIndentedString(currencyConversion)).append("\n");
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

