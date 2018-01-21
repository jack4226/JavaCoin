package org.mycoin.domain;

import java.math.BigDecimal;

import org.mycoin.util.Helper;

public class Transaction {
	
	private final String sender;
	
	// TODO allow multiple recipients
	private final String recipient;
	// TODO make sure no double spend. each output can only be used as an input once
	private final BigDecimal amount;
	
	private final String transactionId;
	
	public Transaction(String sender, String recipient, BigDecimal amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
		
		long tms = System.currentTimeMillis();
		this.transactionId = Helper.hash(this.sender + this.recipient + this.amount.toString() + tms);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sender: " + sender);
		sb.append(", Recipient: " + recipient);
		sb.append(", Amount: " + amount);
		return sb.toString();
	}
	
	public String getSender() {
		return sender;
	}
	public String getRecipient() {
		return recipient;
	}
	public BigDecimal getAmount() {
		return amount;
	}

	public String getTransactionId() {
		return transactionId;
	}

}
