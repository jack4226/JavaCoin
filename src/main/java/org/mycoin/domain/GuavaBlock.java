package org.mycoin.domain;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;

/** =================================================================================================================
 * Interchangeable hashing implementation. (Guava)
 *
 * This block implementation does hashing using Google's Guava library as it is a bit more succinct.
 * ================================================================================================================== */
public class GuavaBlock {
	Logger logger = Logger.getLogger(GuavaBlock.class);
	
    private Integer index = null;
    private String timestamp = null;
    private String data = null;
    private String previousHash = null;
    private String hash = null;

    public GuavaBlock() { /* Default Constructor Unused */}

    public GuavaBlock(final Integer index, final String timestamp, final String data, final String previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = this.hashBlock();
    }

    private String hashBlock() {
        return Hashing.sha256().hashString(this.getIndex().toString() + this.getTimestamp() + this.getData() + this.getPreviousHash(), StandardCharsets.UTF_8).toString();
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
}
