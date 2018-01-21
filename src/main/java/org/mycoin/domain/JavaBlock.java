package org.mycoin.domain;

import java.util.List;

import org.apache.log4j.Logger;
import org.mycoin.app.MainApp;
import org.mycoin.util.Helper;

/** =================================================================================================================
 * Interchangeable hashing implementation. (Java)
 *
 * This block implementation does hashing using pure java classes
 * ================================================================================================================== */
public class JavaBlock {
	Logger logger = Logger.getLogger(JavaBlock.class);
	
    private final Integer index;
    private final String timestamp;
    private final String data;
    private final String previousHash;
    private final String hash;
    
    private final Long proof;
    private final List<Transaction> transactions;

	public JavaBlock(final Integer index, final String timestamp, final String data, final String previousHash,
			final Long proof, final List<Transaction> transactions) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data == null ? "" : data;
        this.previousHash = previousHash;
        this.proof = proof;
        this.transactions = transactions;
        
        this.hash = this.hashBlock();
    }

    public String hashBlock() {
        try {
            String string = this.index.toString() + this.timestamp + this.data + this.previousHash;
            if (this.transactions != null && !this.transactions.isEmpty()) {
	            StringBuilder sb = new StringBuilder();
	            for (Transaction tran : this.transactions) {
	            	sb.append(tran.getSender() + tran.getRecipient() + tran.getAmount().toString());
	            }
	            string += sb.toString();
            }
            final byte[] encodedHash = Helper.getHash(string);

            return Helper.bytesToHex(encodedHash);
        } catch (Exception e) {
            logger.error("Exception caught", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Block #     : " + String.valueOf(index));
    	sb.append(",    Timestamp: " + timestamp + ",    Proof: " + proof + MainApp.LF);
    	sb.append("transactions: " + transactions + MainApp.LF);
    	sb.append("Hash        : " + hash + MainApp.LF);
    	return sb.toString();
    }
    
    /** Public Getters */
    public Integer getIndex() {
        return this.index;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getData() {
        return this.data;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public String getHash() {
        return this.hash;
    }

    public Long getProof() {
		return proof;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

}
